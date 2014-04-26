(
~world= SSWorld(SSVector[20,20,10], SSVector.clear, 200, 0).showView; //dim,grav,maxvel,damp

o=SSObject(~world,SSVector.clear,SSVector[0, 0,0], 0, 9,\point)/*.setMovement(\orbit)*/; //world,loc,vel,acc,mass,size,shape
o=SSObject(~world,SSVector[2,2,0],SSVector[0, 0,0], 0, 1, 0.1,\point,channel:1)
)

~world.objects.at(1).loc_(Spherical(1,0,0).asCartesian)
~world.objects.at(1).loc_(SVector)
pixelSize

o.size_(0.1)

o.loc_(SSVector[200,-200,0])
o.class
o.world.center
o.movement.object.loc

o.setMovement(\orbit,0.1,\lev)
~world.viewDiff=false
~world.aziDiff=1.degree2rad
o.setMovement(\static)

~world.showView
~world.hideView
~world.pause
~world.resume
~world.objects.do(_.vel_(SSVector[10,10,10].rand);)
~world.objects.at(1).loc_(SSVector[100,200,0])

// OSC MONITOR
(
OSCdef("/object_loc", {|msg, time, addr, recvPort| msg.postln}, "/pos", nil); // def style
OSCdef("/new", {|msg, time, addr, recvPort| msg.postln}, "/new", nil); // def style
)


r=Render.new;

~world.alwaysOnTop(true)

~world.window.view.resize_(1)

0.5.linlin(0,1,10,100)
Font