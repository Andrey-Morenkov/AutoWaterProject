-- Initialize
dofile("config.lua")

-- Put radio into station mode to connect to network
wifi.setmode(wifi.STATION)

-- Debug info
print('\n\nSTATION Mode:',  'mode='..wifi.getmode())
print('MAC Address: ',      wifi.sta.getmac())
print('Chip ID: ',          node.chipid())
print('Heap Size: ',        node.heap(),'\n')

-- Start the connection attempt
wifi.sta.config(WIFI_SSID, WIFI_PASS)

-- Count how many times you tried to connect to the network
local wifi_counter = 0

-- Create an alarm to poll the wifi.sta.getip() function once a second
-- If the device hasn't connected yet, blink through the LED colors. If it
-- has, turn the LED white
if ((WIFI_SSID == "none") or (WIFI_SSID == "nowifi"))
    then
        print("No wifi or not SSID")
        dofile("StandaloneMode.lua")
    else
        tmr.alarm(0, 1000, 1, function()
        if ((wifi.sta.getip() == nil) and (wifi_counter < 12))
            then
                print("Connecting to Router...\n")        
                wifi_counter = wifi_counter + 1;     
            else if (wifi_counter < WIFI_WAIT )
                then
                    ip, nm, gw = wifi.sta.getip()

                    -- Debug info
                    print("\n\nIP Info: \nIP Address: ",ip)
                    print("Netmask: ",nm)
                    print("Gateway Addr: ",gw,'\n')
                    tmr.stop(0)     -- Stop the polling loop

                    -- Continue to main function after network connection
                    dofile("CloudEx.lua")   
                else
                    print('Cant connect to router with given SSID and PASS')
                    print('Set AP mode, please configure')
                    tmr.stop(0)
                    dofile("StandaloneMode.lua")
            end       
        end
    end)
end
