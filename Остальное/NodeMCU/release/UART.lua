--uart.alt(0)
uart.setup(0, 115200, 8, uart.PARITY_NONE, uart.STOPBITS_1, 0)
uart.on("data", "\n",
  function(data)
    if (data:sub(1,1) == '.') then
        print("==> " .. data)
        local t = {}
        t["Target"]  = "phone"
        t["Command"] = data
        local jsonstr = cjson.encode(t)
        mqtt:publish("toPhone", jsonstr, 0, 0)
    end    
end, 0)
print("UART callback initalized")

