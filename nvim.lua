local map = vim.keymap.set

-- mvn test current class
map('n', '<leader>mt', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.api.nvim_feedkeys(':! mvn test -Dtest="' .. pkg .. '/' .. cls .. '"\n', 'n', false)
end, { noremap = true, desc = "Run maven tests on the current class." })

-- mvn test current class (args)
map('n', '<leader>mtit', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.api.nvim_feedkeys(':! mvn test -Dtest=' .. pkg .. '/' .. cls .. '\\#', 'n', false)
end, { noremap = true, desc = "Run maven tests on specified methods of the current class." })

-- measure time consumption of each test methods
map('n', '<leader>mtp', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.api.nvim_feedkeys(':! mvn test -Dstopwatch="Y" -Dtest="' .. pkg .. '/' .. cls .. '"\n', 'n', false)
end, { noremap = true, desc = "Run maven tests with profiling on the current class." })

-- measure time consumption of each test methods (args)
map('n', '<leader>mtip', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.api.nvim_feedkeys(':!mvn test -Dstopwatch="Y" -Dtest=' .. pkg .. '/' .. cls .. '\\#', 'n', false)
end, { noremap = true, desc = "Run maven tests on specified methods with profiling on the current class." })

-- enable verbose log during tests
map('n', '<leader>mtv', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.cmd('!mvn test -Dverbose="Y" -Dtest="' .. pkg .. '/' .. cls .. '"')
end, { noremap = true, desc = "Run maven tests with verbose information on the current class." })

-- mvn test all
map('n', '<leader>mta', function()
  vim.cmd('!mvn test')
end, { noremap = true, desc = "Run maven tests." })

-- mvn clean
map('n', '<leader>mc', function()
  vim.cmd('!mvn clean')
end, { noremap = true, desc = "Run maven cleanup job." })

-- mvn test current class with enough stack size: 8M
map('n', '<leader>mtss', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.cmd('!mvn test -DargLine="-Xss8M" -Dtest="' .. pkg .. '/' .. cls .. '"')
end, { noremap = true, desc = "Run maven tests on the current class with enough stack size." })

-- mvn clean && mvn compile test-compile
map('n', '<leader>mcp', function()
  vim.cmd(':!mvn clean && mvn compile test-compile')
end, { noremap = true, desc = "Run maven clean and compile job." })

-- project specific template
-- (required) Enable project specific code template
vim.g.use_prj_tmpl = true
-- (required) Substitution meta-data
vim.g.prj_tmpl_subs = {
  author = 'rltyty',
  email = 'except10n.rlt@gmail.com',
  year = function()
    return vim.fn.strftime('%Y')
  end,
  created = function()
    return vim.fn.strftime('%Y/%m/%d %T')
  end,
  ['copyright holder'] = 'rltyty',

  package = function()
    local fullpath = vim.fn.expand('%:p') -- full path of current buffer
    local project_root = vim.fn.getcwd()  -- assuming this is your project root
    local relpath = fullpath:sub(#project_root + 2) -- remove project root + "/"

    -- Try to extract part after src/main/java or src/main
    local pkg = relpath
      :gsub("^src/test/java/", "")
      :gsub("^src/main/java/", "")
      :gsub("^src/main/", "")
      :gsub("/[^/]*%.java$", "")  -- remove trailing /File.java
      :gsub("/", ".")

    return pkg
  end,
  classname = function ()
    return vim.fn.expand('%:t:r')
  end,
}
-- (optional) Substitution pattern (default, <<keyword>>)
-- vim.g.prj_tmpl_pat = '<<\\([^>]*\\)>>'

-- (optional) Directory to search for templates (`skel.*`)
vim.g.prj_tmpl_root = 'src/templates'

