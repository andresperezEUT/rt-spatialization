r=SpatialRender.new(s);

r.oscLogger.save
r.oscLogger.close

r.verbose_(true)
(
NetAddr.localAddr.sendMsg("/spatdifcmd/addEntity",\drums);
NetAddr.localAddr.sendMsg("/spatdif/source/drums/media/type",\jack);
NetAddr.localAddr.sendMsg("/spatdif/source/drums/media/channel",0);
)

NetAddr.localAddr.sendMsg("/spatdif/source/one/position",0,0,5,\aed);
NetAddr.localAddr.sendMsg("/spatdif/source/one/position",0,0,2,\aed);
/*(
NetAddr.localAddr.sendMsg("/spatdifcmd/addEntity",\hh);
NetAddr.localAddr.sendMsg("/spatdif/source/hh/media/type",\jack);
NetAddr.localAddr.sendMsg("/spatdif/source/hh/media/channel",1);
)*/
(
NetAddr.localAddr.sendMsg("/spatdifcmd/addEntity",\bass);
NetAddr.localAddr.sendMsg("/spatdif/source/bass/media/type",\jack);
NetAddr.localAddr.sendMsg("/spatdif/source/bass/media/channel",2);
)
(
NetAddr.localAddr.sendMsg("/spatdifcmd/addEntity",\piano);
NetAddr.localAddr.sendMsg("/spatdif/source/piano/media/type",\jack);
NetAddr.localAddr.sendMsg("/spatdif/source/piano/media/channel",3);
)
(
NetAddr.localAddr.sendMsg("/spatdifcmd/addEntity",\melody);
NetAddr.localAddr.sendMsg("/spatdif/source/melody/media/type",\jack);
NetAddr.localAddr.sendMsg("/spatdif/source/melody/media/channel",4);
)

// ~s2.set(\amp,0)



r.encoders