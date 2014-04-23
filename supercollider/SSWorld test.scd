(
~world= SSWorld(SSVector[500,500,100], SSVector.clear, 200, 0).showView; //dim,grav,maxvel,damp

o=SSObject(~world,SSVector.clear,SSVector[0, 0,0], 0, 1, 5,\point)/*.setMovement(\orbit)*/; //world,loc,vel,acc,mass,size,shape
)
o=SSObject(~world,SSVector[-200,-200,0],SSVector[0, 0,0], 0, 1, 5,\point)

o.loc_(SSVector[200,200,0])
o.class
o.world.center
o.movement.object.loc

o.setMovement(\orbit,1,\lev)
~world.viewDiff=false
~world.aziDiff=1.degree2rad
o.setMovement(\static)

~world.showView
~world.hideView
~world.pause
~world.resume
~world.objects.do(_.vel_(SSVector[10,10,10].rand);)


// OSC MONITOR
(
OSCdef("/object_loc", {|msg, time, addr, recvPort| msg.postln}, "/pos", nil); // def style
OSCdef("/new", {|msg, time, addr, recvPort| msg.postln}, "/new", nil); // def style
)