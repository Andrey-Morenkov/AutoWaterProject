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
if ((WIFI_SSID == "none") or (WIFI_SSID == "nowifi"))
    then
        print("No wifi or not SSID")
        dofile("StandaloneMode.lua")
    else
        tmr.alarm(0, 1000, 1, function()
        if ((wifi.sta.getip() == nil) and (wifi_counter < WIFI_WAIT))
            then
                print("Connecting to Router...\n")
                do
                    local state = 0
                    tmr.alarm(1, 100, 1, function()
                        if state == 0 then
                            state = 1
                            gpio.write(0, gpio.LOW)
                        else
                            state = 0
                            tmr.stop(1)
                            gpio.write(0, gpio.HIGH)
                        end
                    end)
                end
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
