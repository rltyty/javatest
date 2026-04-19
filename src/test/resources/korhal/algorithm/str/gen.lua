-- In Lua, strings are immutable, so s = s .. 'a' does:
-- 1. Allocates a brand new string of length #s + 1
-- 2. Copies all #s existing characters into it
-- 3. Appends the new character
-- 4. Discards the old string
-- DO NOT USE:
-- ```Lua
-- for i = 1, slen do
--   s = s .. 'a' -- allocates slen times, O(n^2)
-- end
-- ```
-- Use `string.rep` for same chars and `table.concat` for general string

local slen = 10^6 - 1
local s = string.rep('a', slen) .. 'b'

local tlen = 10^2 - 1
local t = string.rep('a', tlen) .. 'b'

print(s)
print(t)
