// TODO: ensombrecer cuando una fuente está en present_false
// TODO: calcular la nyquist frequency en base al update frequency
// TODO: command for setting random position
// TODO: master velocity???
// TODO: master objects group?? sin tener que hacer un objects.do(...)
//TODO: hacer un comando de init_world, antes de eso se inicia en pause
//// una vez iniciado, se comienza a escribir el timeline en el logger
//// antes de eso, se escribe en la sección \meta\



////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
//                   TEMAZO
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

"/home/andres/Documents/ss/external audio simulation.scd".load;
(
r=SpatialRender.new(s);
r.verbose_(false);
~world= SSWorld([10,10,10], [0,0,0], 200, 0).showView; //dim,grav,maxvel,damp

~drums=SSObject(~world,[0,2,2],[0,0,0], [0,0,0], 9,\point,name:\drums,channel:0).setMovement(\orbit,2,\dex);
~bass=SSObject(~world,[0,2,0],[0,0,0], [0,0,0], 9,\point,name:\bass,channel:2);
~piano=SSObject(~world,[0,2,0],[0,0,0], [0,0,0], 9,\point,name:\piano,channel:3).setMovement(\orbit,2,\lev);
~melody=SSObject(~world,[0,2,0],[0,0,0], [0,0,0], 9,\point,name:\melody,channel:4);

)

~world.objects.do{|o|o.setMovement(\random)}
~world.pause

~melody

~melody.present_(false)
~piano.present_(false)

~drums.setMovement(\orbit,20,\dex)
~bass.setMovement(\orbit,50,\lev)
~drums.locSph_(ele:0);
~piano.setMovement(\orbit,50,\lev)

~melody.setMovement(\orbit,100,\lev)
~bass.locSph_(azi:0)
~bass.loc_(z:5)
~melody.loc_([-2,2,2])

~bass2=SSObject(~world,[0,2,0],[0,0,0], [0,0,0], 9,\point,name:\bass2,channel:2);

r.verbose_(false);
r.oscLogger.save
r.oscLogger.close
// ~world.pause;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
//                   test 2
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////


"/home/andres/Documents/ss/external audio simulation 2.scd".load;
(
r=SpatialRender.new(s);
r.verbose_(false);
~world= SSWorld([10,10,10], [0,0,0], 200, 0).showView; //dim,grav,maxvel,damp

~fly1=SSObject(~world,[0,2,2],[0,0,0], [0,0,0], 9,\point,name:\fly,channel:0).setMovement(\random);
~bee1=SSObject(~world,[0,2,0],[0,0,0], [0,0,0], 9,\point,name:\bee,channel:4).setMovement(\random);

~fly2=SSObject(~world,[0,2,2],[0,0,0], [0,0,0], 9,\point,name:\fly2,channel:1).setMovement(\random);
~fly3=SSObject(~world,[0,2,0],[0,0,0], [0,0,0], 9,\point,name:\fly3,channel:2).setMovement(\random);

~bee1.present_(false)
)

~fly1=SSObject(~world,[0,2,0],[0,0,0], [0,0,0], 9,\point,name:\fly1,channel:0).setMovement(\random);
~fly2=SSObject(~world,[0,-2,0],[0,0,0], [0,0,0], 9,\point,name:\fly2,channel:1).setMovement(\random);
~fly3=SSObject(~world,[2,0,0],[0,0,0], [0,0,0], 9,\point,name:\fly3,channel:2).setMovement(\random);
~fly4=SSObject(~world,[-2,0,0],[0,0,0], [0,0,0], 9,\point,name:\fly4,channel:3).setMovement(\random);

~fly4.setMovement(\static)

~fly1.setMovement(\random);


////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
//                   android test
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////




(
c=OrientationController.new;
c.setAction(\orientation,{|args,time|
	var azi = args[0];
	"azimuth: ".post;
	azi.postln;
	~bass.locSph_(azi:azi)
})
)

n = NetAddr.new( "localhost", NetAddr.langPort );
// send azimuth in degrees
n.sendMsg( "/orientationController/orientation", 180, 0, 0);




r.verbose_(true)

o.addVel([0,0,100])
o.vel

o.loc_([3,0,0]);
~world.update


o.vel_([0,0,2])

o.loc_([1,0,0])

~world.resume
o.setMovement(\random)
o.setMovement(\rect,Cartesian(0,0,2))
5.0.rand!3

o.setMovement(\static)

o.setMovement(\shm,[1,0,2],[2,1,1])
o.setMovement(\shm)
o.movement.amp=1;
o.movement.yAmp_(1)
o.movement.yT_(1)

[1].size

o.movement
o.vel_([1,0,1]);
o.accel_([10,10,10])

o.vel.class.postln

(0..1)
o.loc((0..1))

~world.showNames(true)
~world.dim
o.loc_([3,0,0])
o.loc_([1,1,5])
~world.dim.z

o.loc

1.neg

~world.gravity
~world.objects.at(1).loc_(Spherical(1,0,0).asCartesian)
~world.objects.at(1).loc_(SVector)
pixelSize

o.vel
o.loc_(Cartesian(5,0,9))
o.loc
~world.contains(o)
~world.contain(o)
a=Spherical(1,0,0)
a.asCartesian

o.locSph.x
o.loc

o.velSph
o.vel

o.locSph_([1,pi/2,-pi/4])
o.size_(0.1)

o.class
o.world.center
o.movement.object.loc


o.movement.next
o.movement.taccel=0.1;


~world.damping_(0)
~world.update

o.vel
o.vel_([0.1,0.1,0])
o.accel_([0.1,1,0])
o.setMovement(\orbit,0.2)

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