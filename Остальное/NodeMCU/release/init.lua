-- 0 - poweron 4 - software restart 6 - external restart
_, reset_reason = node.bootreason()

if reset_reason == 0 then
    print("Power UP")
elseif reset_reason == 4 then
    print("Software restart")
elseif reset_reason == 6 then
    print("Hardware restart")
    file.remove("WifiConfig.lua")
end

dofile("config.lua")
dofile("StationMode.lua")
