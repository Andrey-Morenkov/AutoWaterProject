-- Server
WIFI_SSID = nil
WIFI_PASS = nil
sv = net.createServer(net.TCP, 300)

function receiver(sck, data)
    print(data)
    
    local t = cjson.decode(data)
    for k,v in pairs(t) 
        do
            if (k == "WIFI_SSID") then
                WIFI_SSID = v
            end

            if (k == "WIFI_PASS") then
                WIFI_PASS = v
            end        
        end        
    
    if file.open("WifiConfig.lua", "w+") then
        file.writeline(WIFI_SSID)
        file.writeline(WIFI_PASS)
        file.close()
    end
  dofile("StationMode.lua")
end

if sv then
  sv:listen(25565, function(conn)
    conn:on("receive", receiver)    
  end)
end
