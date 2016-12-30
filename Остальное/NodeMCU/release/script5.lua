-- Server

sv = net.createServer(net.TCP, 300)
function receiver(sck, data)
  print(data)
  sck:close()
end

if sv then
  sv:listen(25565, function(conn)
    conn:on("receive", receiver)
    conn:send("hello world")
  end)
end
