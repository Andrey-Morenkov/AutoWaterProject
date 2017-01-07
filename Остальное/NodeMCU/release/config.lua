-- WiFi
if file.open("WifiConfig.lua", "r") then
    print("WifiConfig.lua exists")
    
    WIFI_SSID = file.read('\n')
    WIFI_SSID = string.gsub(WIFI_SSID, "\n", "")
    print('SSID:<',WIFI_SSID,'>')
    WIFI_PASS = file.read('\n')
    WIFI_PASS = string.gsub(WIFI_PASS, "\n", "")
    print('PASS:<',WIFI_PASS,'>')
    file.close()
elseif file.open("WifiConfig.lua", "w") then
    print("WifiConfig.lua doesn't exists")
    file.writeline('none')
    WIFI_SSID = "none"
    file.writeline('nonenone')
    WIFI_PASS = "nonenone"
    file.close()
end
WIFI_WAIT = 20

-- Pin Declarations
LED_ON = 1
LED_OFF = 0
uart.alt(0)
-- default uart is 115 200

-- MQTT
MQTT_CLIENTID = "esp-8266"
MQTT_HOST = "m21.cloudmqtt.com"
MQTT_PORT = 19348
MQTT_USER = "Test"
MQTT_PASS = "test"

-- Access Point
AP_SSID = "AutoWaterProject"
AP_PASS = "awpawpawp"
AP_AUTH = "wifi.WPA_WPA2_PSK"

-- Confirmation message
print("\nGlobal variables loaded...\n")

