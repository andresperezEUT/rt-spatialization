r=SpatialRender.new(s);

r.oscLogger.save
r.oscLogger.close

r.verbose_(true)
NetAddr.localAddr.sendMsg("/spatdifcmd/addEntity",\one)
NetAddr.localAddr.sendMsg("/spatdif/source/one/media/type",\jack)


NetAddr.localAddr.sendMsg("/spatdif/source/one/position",0,0,5,\aed)
NetAddr.localAddr.sendMsg("/spatdif/source/one/position",0,0,4,\aed)

NetAddr.localAddr.sendMsg("/spatdifcmd/addEntity",\two)
NetAddr.localAddr.sendMsg("/spatdif/source/two/media/type",\jack)





r.encoders
