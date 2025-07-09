local map = vim.keymap.set

-- mvn test current class
map('n', '<leader>mt', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.api.nvim_feedkeys(':!mvn test -Dtest="' .. pkg .. '/' .. cls .. '"\n', 'n', false)
end, { noremap = true, desc = "Run maven tests on the current class." })

-- mvn test current class (args)
map('n', '<leader>mtit', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.api.nvim_feedkeys(':!mvn test -Dtest=' .. pkg .. '/' .. cls .. '\\#', 'n', false)
end, { noremap = true, desc = "Run maven tests on specified methods of the current class." })

-- measure time consumption of each test methods
map('n', '<leader>mtp', function()
  local pkg = vim.fn.expand("%:h:t")
  local cls = vim.fn.expand("%:t:r")
  vim.api.nvim_feedkeys(':!mvn test -Dstopwatch="Y" -Dtest="' .. pkg .. '/' .. cls .. '"\n', 'n', false)
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
