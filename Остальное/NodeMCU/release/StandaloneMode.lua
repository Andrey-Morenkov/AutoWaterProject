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
cfg = nil

do
    local ssid, password=wifi.ap.getconfig()
    local ip, nm, gw = wifi.ap.getip()
    local port = 25565
    -- Debug info
    print('AP Mode:')
    print(' SSID: ', ssid         )
    print(' PASS: ', password     )
    print(' IP:   ', ip           )
    print(' MASK: ', nm           )
    print(' PORT: ', port         )
    print(' GATE: ', gw,      '\n')
    ssid, password=nil, nil
    ip, nm, gw,port = nil, nil, nil, nil
    dofile('StandaloneBlink.lua')
    dofile('Server.lua')
end
