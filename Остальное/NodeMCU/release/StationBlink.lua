blink_counter = 0
state = 0

tmr.alarm(0, 200, 1, function()
    if state == 0 then
        state = 1
        gpio.write(0, gpio.LOW)
    else
        state = 0
        blink_counter = blink_counter + 1
        gpio.write(0, gpio.HIGH)
        if blink_counter > 1 then
            tmr.stop(0)
        end
    end
end)
