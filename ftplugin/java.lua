-- See https://github.com/mfussenegger/nvim-jdtls?tab=readme-ov-file

-- See `:help vim.lsp.start_client` for an overview of the supported `config` options.

-- local mason_registry = require("mason-registry")
local mason_pkg_root = vim.fn.expand("$MASON/packages")
local jdtls_path = mason_pkg_root .. "/jdtls"
local java_debug_adapter_path = mason_pkg_root .. "/java-debug-adapter"
local java_test_path = mason_pkg_root .. "/java-test"
local lombok_jar = jdtls_path .. "/lombok.jar"
local project_name = vim.fn.fnamemodify(vim.fn.getcwd(), ':p:h:t')
local workspace_dir = vim.fn.stdpath('cache') .. '/jdtls.prjs/' .. project_name
-- local java_home = vim.fn.getenv('JAVA_HOME')
-- local src_zip = java_home .. '/lib/src.zip'

local bundles = {
  vim.fn.glob(java_debug_adapter_path .. '/extension/server/com.microsoft.java.debug.plugin-*.jar', true),
}

-- NOTE: Do I really need java-test for in-IDE test runner? java.debug seems
-- to be enough.
vim.list_extend(bundles, vim.split(vim.fn.glob(java_test_path .. '/extension/server/*.jar', true), '\n'))

local rlib = require('rlib')

-------------------------------------------------------------------------------
-- Temporary solution: ignoring non-OSGI bundle in java-test extension
-- https://github.com/mfussenegger/nvim-jdtls/issues/746#issuecomment-2666183375
-- Following filters out unwanted bundles
local ignored_bundles = { "com.microsoft.java.test.runner-jar-with-dependencies.jar", "jacocoagent.jar" }
local find = string.find
local function should_ignore_bundle(bundle)
    for _, ignored in ipairs(ignored_bundles) do
        if find(bundle, ignored, 1, true) then
            return true
        end
    end
end
bundles = vim.tbl_filter(function(bundle) return bundle ~= "" and not should_ignore_bundle(bundle) end, bundles)
-- https://github.com/mfussenegger/nvim-jdtls/issues/746#issuecomment-2666183375
-------------------------------------------------------------------------------

local config = {
  -- The command that starts the language server
  -- See: https://github.com/eclipse/eclipse.jdt.ls#running-from-the-command-line
  -- Use `htop` to inspect all the default arguments of the running process
  cmd = {
    vim.fn.exepath('jdtls'),
    string.format("--jvm-arg=-javaagent:%s", lombok_jar),
    '-data', workspace_dir,
    -- '-sourcepath', src_zip,
  },

  -- 💀
  -- This is the default if not provided, you can remove it. Or adjust as needed.
  -- One dedicated LSP server & client will be started per unique root_dir
  --
  -- vim.fs.root requires Neovim 0.10.
  -- If you're using an earlier version, use: require('jdtls.setup').find_root({'.git', 'mvnw', 'gradlew'}),
  root_dir = vim.fs.root(0, {"pom.xml", "build.xml"}),

  -- Here you can configure eclipse.jdt.ls specific settings
  -- See https://github.com/eclipse/eclipse.jdt.ls/wiki/Running-the-JAVA-LS-server-from-the-command-line#initialize-request
  -- for a list of options
  settings = {
    java = {
    },
  },

  -- Language server `initializationOptions`
  -- You need to extend the `bundles` with paths to jar files
  -- if you want to use additional eclipse.jdt.ls plugins.
  --
  -- See https://github.com/mfussenegger/nvim-jdtls#java-debug-installation
  --
  -- If you don't plan on using the debugger or other eclipse.jdt.ls plugins you can remove this
  init_options = {
    bundles = bundles,
  },

  autostart=true,
}

require('jdtls').start_or_attach(config)


local map = vim.keymap.set
map( {'n'}, '<leader>cjf', "<Cmd>lua require'jdtls'.compile('full')<CR>", {noremap = true})
map( {'n'}, '<leader>cjo', "<Cmd>lua require'jdtls'.organize_imports()<CR>", {noremap = true, desc = 'Jdtls: organize imports'})
map( {'n'}, '<leader>cjv', "<Cmd>lua require('jdtls').extract_variable()<CR>", {noremap = true} )
map( {'v'}, '<leader>cjv', "<Esc><Cmd>lua require('jdtls').extract_variable(true)<CR>", {noremap = true})
map( {'n'}, '<leader>cjc', "<Cmd>lua require('jdtls').extract_constant()<CR>", {noremap = true})
map( {'v'}, '<leader>cjc', "<Esc><Cmd>lua require('jdtls').extract_constant(true)<CR>", {noremap = true})
map( {'v'}, '<leader>cjm', "<Esc><Cmd>lua require('jdtls').extract_method(true)<CR>", {noremap = true})


-- If using nvim-dap
-- This requires java-debug and vscode-java-test bundles, see install steps in this README further below.
map( {'n'}, '<leader>df', "<Cmd>lua require'jdtls'.test_class()<CR>", {noremap = true} )
map( {'n'}, '<leader>dn', "<Cmd>lua require'jdtls'.test_nearest_method()<CR>", {noremap = true})


-- View Java class bytecode using `javap`
vim.api.nvim_create_user_command("Javap", function(opts)
  local verbose = vim.tbl_contains(opts.fargs, '-v')
  local class
  for _, a in ipairs(opts.fargs) do
    if a ~= '-v' then
      class = a
      break
    end
  end

  if not class or class == '' then
     vim.notify("Error: Missing filename (usage: :Javap [-v] filename)", vim.log.levels.ERROR)
    return
  end
  local stat = vim.uv.fs_stat(class)
  if not stat or stat.type ~= 'file' then
     vim.notify("Error: File not exists or not a file type (usage: :Javap [-v] filename)", vim.log.levels.ERROR)
    return
  end

  vim.cmd('set splitright')
  vim.cmd("vnew")  -- open vertical split
  if verbose then
    vim.cmd("read !javap -c -l -p -v " .. class)
  else
    vim.cmd("read !javap -c -l -p " .. class)
  end
  -- mark it as scratch
  vim.bo.buftype = "nofile"
  vim.bo.bufhidden = "wipe"
  vim.bo.swapfile = false
  vim.bo.readonly = true
end, { nargs = '+', complete = 'file', desc = 'Decompile Java class (Javap)' })
map( {'n'}, '<leader>cjb', "<Cmd>Javap " .. vim.fn.expand('%') .. "<CR>", {noremap = true, desc = 'JavaP: view class bytecode'})
map( {'n'}, '<leader>cjB', "<Cmd>Javap " .. " -v " .. vim.fn.expand('%') .. "<CR>", {noremap = true, desc = 'JavaP: view class bytecode (VERBOSE)'})

local bytecode_help = {
  getstatic = {
    "Opcode: getstatic",
    "Get value of a static field from a class.",
    "Stack: → value",
    "Example: System.out"
  },
  putstatic = {
    "Opcode: putstatic",
    "Set value of a static field.",
    "Stack: value →",
    "Example: MyClass.value = 5"
  },
  getfield = {
    "Opcode: getfield",
    "Get instance field from object.",
    "Stack: object_ref → field_value"
  },
  putfield = {
    "Opcode: putfield",
    "Set instance field on object.",
    "Stack: object_ref, value →"
  },
  invokevirtual = {
    "Opcode: invokevirtual",
    "Call instance method (dynamic dispatch).",
    "Stack: object_ref + args → return_value"
  },
  invokespecial = {
    "Opcode: invokespecial",
    "Call constructor, private, or super method.",
    "Stack: object_ref + args → return_value"
  },
  invokestatic = {
    "Opcode: invokestatic",
    "Call static method.",
    "Stack: args → return_value"
  },
  invokeinterface = {
    "Opcode: invokeinterface",
    "Call interface method (dynamic dispatch).",
    "Stack: object_ref + args → return_value"
  },
  invokedynamic = {
    "Opcode: invokedynamic",
    "Dynamic method resolution (e.g. lambdas).",
    "Stack: args → return_value"
  },
}

function ShowOpcodeHelp()
  local word = vim.fn.expand("<cword>")  -- get word under cursor
  local lines = bytecode_help[word]
  if not lines then
    lines = { "No help for: " .. word }
  end

  local buf = vim.api.nvim_create_buf(false, true)
  vim.api.nvim_buf_set_lines(buf, 0, -1, false, lines)

  local opts = {
    relative = "cursor",
    row = 1,
    col = 1,
    width = 50,
    height = #lines,
    style = "minimal",
    border = "rounded",
  }

  local win = vim.api.nvim_open_win(buf, false, opts)

  -- Automatically close the popup on CursorMoved, BufLeave, or InsertCharPre
  vim.api.nvim_create_autocmd({ "CursorMoved", "BufLeave", "InsertCharPre" }, {
    once = true,
    callback = function()
      if vim.api.nvim_win_is_valid(win) then
        vim.api.nvim_win_close(win, true)
      end
    end,
  })
end

-- View Java class bytecode using `cfr.jar`
vim.api.nvim_create_user_command("Cfr", function(opts)
  local cfr = "~/.m2/repository/org/benf/cfr/0.152/cfr-0.152.jar"
  local class = opts.fargs[1]
  if not rlib.fexists(class) then
     vim.notify("Error: File not exists or not a file type (usage: :Cfr classfile [options])", vim.log.levels.ERROR)
  end

  local options = table.concat(opts.fargs, " ", 2)

  vim.cmd('set splitright')
  vim.cmd('vnew')  -- open vertical split
  vim.cmd('read !java -jar ' .. cfr .. ' ' .. class .. " " .. options)
  -- mark it as scratch
  vim.bo.buftype = "nofile"
  vim.bo.bufhidden = "wipe"
  vim.bo.swapfile = false
  vim.bo.readonly = true
end, { nargs = '+', complete = 'file', desc = 'Decompile Java class (CFR)' })
map( {'n'}, '<leader>cjd', "<Cmd>Cfr " .. vim.fn.expand('%') .. "<CR>", {noremap = true, desc = 'Cfr: Decompile Java class'})
map( {'n'}, '<leader>cjD', ":Cfr " .. vim.fn.expand('%') .. " ", {noremap = true, desc = 'Cfr: Decompile Java class (args)'})

