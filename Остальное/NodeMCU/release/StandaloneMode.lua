-- If cant connect to wifi or hasn't wifi
wifi.setmode(wifi.SOFTAP)

cfg = {}
    cfg.ssid = AP_SSID
    cfg.pwd= AP_PASS
    
wifi.ap.config(cfg)
cfg = nil

cfg =
{
    ip = "192.168.1.111",
    netmask ="255.255.255.0",
    gateway="192.168.1.111"    
}
    
wifi.ap.setip(cfg)

do
    local ssid, password=wifi.ap.getconfig()
    local ip, nm, gw = wifi.ap.getip()
    -- Debug info
    print('AP Mode:')
    print(' SSID: ', ssid         )
    print(' PASS: ', password     )
    print(' IP:   ', ip           )
    print(' MASK: ', nm           )
    print(' GATE: ', gw,      '\n')
    ssid, password=nil, nil
    ip, nm, gw = nil, nil, nil
    dofile('StandaloneBlink.lua')
end
