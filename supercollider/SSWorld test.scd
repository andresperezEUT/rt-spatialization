(
~world= SSWorld([20,10,10], [0,0,0], 200, 0).showView; //dim,grav,maxvel,damp

o=SSObject(~world,[0,0,0],[0,0,0], 0, 9,\point)/*.setMovement(\orbit)*/; //world,loc,vel,acc,mass,size,shape
o=SSObject(~world,[1,1,0],[0,0,0], 0, 1, 0.1,\point,channel:1)
)

~world.dim
~world.gravity
~world.objects.at(1).loc_(Spherical(1,0,0).asCartesian)
~world.objects.at(1).loc_(SVector)
pixelSize

o.vel
o.loc_(Cartesian(-4.1,0,0))
o.loc
~world.contains(o)
~world.contain(o)
a=Spherical(1,0,0)
a.asCartesian

o.locSph.x
o.loc

o.velSph
o.vel

o.vel_([0,1,0])
o.locSph_([1,pi/2,-pi/4])
o.size_(0.1)

o.loc_(Cartesian(1,0,0))
o.class
o.world.center
o.movement.object.loc


o.movement.next
o.movement.taccel=0.1;


~world.damping_(0)
o.setMovement(\rect)
~world.update

o.vel
o.vel_([0.1,0.1,0])
o.accel_([0.1,0.1,0])
o.setMovement(\orbit,2)

~world.stepFreq_(60)
o.setMovement(\orbit,2pi,\lev)
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

~world.window.refresh

0.5.linlin(0,1,10,100)
Font