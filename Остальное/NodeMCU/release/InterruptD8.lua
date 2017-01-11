do
    local blink_counter = 0
    local state = 0

    tmr.alarm(0, 80, 1, function()
        if state == 0 then
            state = 1
            gpio.write(8, gpio.LOW)
        else
            state = 0
            blink_counter = blink_counter + 1
            gpio.write(8, gpio.HIGH)
            if blink_counter > 0 then
                tmr.stop(0)
            end
        end
    end)
end
