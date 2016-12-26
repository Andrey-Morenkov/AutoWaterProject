print(wifi.sta.getip())
--nil
wifi.setmode(wifi.STATION)
wifi.sta.config("MyNodeMCU","1234")
print(wifi.sta.getip())