--[[
CloudEx.lua
40robbers
--]]
-- initiate the mqtt client and set keepalive timer to 120sec
mqtt = mqtt.Client("esp8266", 120, MQTT_USER, MQTT_PASS)

mqtt:on("connect", function(con) print ("connected") end)
mqtt:on("offline", function(con) print ("offline") end)

-- on receive message
mqtt:on("message", function(conn, topic, data)
  print(topic .. ":" )
  if data ~= nil then
    print(data)

    if data == "0" then
    gpio.write(0, gpio.HIGH)

    elseif data ==  "1" then
    gpio.write(0, gpio.LOW)

    -- in case of requested red light
    elseif data == "red" then
      -- inform MQTT broker that something went wrong
      -- print error to console
      mqtt:publish("status", "--> RGB_LED set to RED", 0, 0,
            function(m) print("RGB_LED set to RED") end)
      -- set RGB led to RED
      rgb_solid(0)

    -- in case of requested green light
    elseif data == "green" then
      -- inform MQTT broker that something went wrong
      -- print error to console
      mqtt:publish("status", "--> RGB_LED set to GREEN", 0, 0,
            function(m) print("RGB_LED set to GREEN") end)
      -- set RGB led to GREEN
      rgb_solid(1)


    -- in case of requested blue light
    elseif data == "blue" then
      -- inform MQTT broker that something went wrong
      -- print error to console
      mqtt:publish("status", "--> RGB_LED set to BLUE", 0, 0,
            function(m) print("RGB_LED set to BLUE") end)
      -- set RGB led to BLUE
      rgb_solid(2)

    -- in case of unknown command
    else
      -- inform MQTT broker that something went wrong
      -- print error to console
      mqtt:publish("status", "--> Error: Unknown Command", 0, 0,
            function(m) print("ERROR: UNKNOWN COMMAND") end)
    end
  end
end)

mqtt:connect("m21.cloudmqtt.com", 19348, 0, function(conn)
  -- subscribe topic with qos = 0
  mqtt:subscribe("main_topic",0)
  print("CloudMQTT connected")
  dofile("blink.lua")
end)

