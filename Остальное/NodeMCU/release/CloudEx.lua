-- initiate the mqtt client and set keepalive timer to 120sec
mqtt = mqtt.Client("esp8266", 120, MQTT_USER, MQTT_PASS)

mqtt:on("connect", function(con) print ("connected") end)
mqtt:on("offline", function(con) print ("offline") end)

-- on receive message
mqtt:on("message", function(conn, topic, data)
  --print(topic .. ":" )
  if data ~= nil then
      --print(data)
      
      local t = cjson.decode(data)            
      if (t.Target == "arduino") then
          
          gpio.write(8, gpio.LOW)
          gpio.write(0, gpio.LOW)      
          tmr.delay(1000)
          gpio.write(0, gpio.HIGH)
          gpio.write(8, gpio.HIGH)
          
          uart.write(0, t.Command .. "\n")
      end
   
      if(t.Target == "nodemcu") then
          if (t.Type == "simple") then
              loadstring(t.Command)()    
          end
  
          if (t.Type == "composite") then
              -- тут для сложных команд в nodemcu
          end          
      end    
      dofile ("SingleBlink.lua")
  else
      -- inform MQTT broker that something went wrong
      -- print error to console
      --mqtt:publish("status", "--> Error: Unknown Command", 0, 0,
        --    function(m) print("ERROR: UNKNOWN COMMAND") end)
  end
end)

mqtt:connect("m21.cloudmqtt.com", 19348, 0, function(conn)
  -- subscribe topic with qos = 0
  mqtt:subscribe("toController",0)
  print("CloudMQTT connected")
end)

dofile("UART.lua")
dofile("StationBlink.lua")

