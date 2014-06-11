(
~world= SSWorld([10,10,10], [1,0,0], 200, 0).showView; //dim,grav,maxvel,damp

o=SSObject(~world,[0,2,2],[0,0,0], [0,0,0], 9,\point).setMovement(\static); //world,loc,vel,acc,mass,size,shape
// ~world.pause;*
// o=SSObject(~world,[1,1,0],[0,0,0], 0, 1, 0.1,\point,channel:1)
~r=4;
~azi=0;
~ele=0;
)

~world.getObject(\0).locSph_(rho:4)
// //////////////
// pitch = atan (x / sqrt(y^2 + z^2))
// roll = atan (y / sqrt(x^2 + z^2))
//////////////

o.locSph_([~r,~azi,~ele])

(
~ele=0;
~l1=List.new;
OSCdef(\pitch,{ |msg,time,addr,recvPort|
	// [msg,time,addr,recvPort].postln;
	~lastTime=~time;
	~time=time;
	~dT=~time - ~lastTime;
	~v=msg[1];

	~v=~v.scale(0,15369.258926815,-pi,pi);

	// ~v.postln;

	// ~v.postln;



/*	if (~v.abs < 0.1) {
		~v=0;
	};*/

	~dEle=~v*~dT;
	// ~dEle.postln;
	~l1.add(~dEle);
	~ele=~ele+~dEle;
	[~dEle,~ele].postln;
/*	~ele=~ele+(~v*~dT);
	~ele.postln;*/

},"/wiimote/mp/pitch");
)
Fil

OSCFunc.trace(false)
(
OSCdef(\touch,{ |msg,time,addr,recvPort|
	// [msg,time,addr,recvPort].postln;
	~azi=msg[1];
	~ele=msg[2];

	// azimuth in rad, "calibrated", positive in counter-clockwise
	~azi=(~azi-215.8518371582).neg.wrap(0,360).degree2rad;
	// elevation in rad, clipped between [0,pi/2]
	~ele=~ele.neg.clip(0,90).degree2rad;

	[~azi,~ele].postln;

	o.locSph_([~r,~azi,~ele])
},"/test");
)



(
o.vel_([0,0,1]);
o.addForce([1,0,0])
o.friction_(true)
o.stop
o.vel;
o.accel;
~world.gravity_([1,1,0])
~world.damping_(0.5)
~world.friction_(0.2)
~world.dim.x
)
Render

o.gravity_(true)
o.friction
o.gravity_(false)

o.movement.period
o.loc_([5,0,5]);
~world.update

o.movement.step_(0.1)


o.vel_([0,0,2])

o.loc_([1,0,0])

~world.resume
o.setMovement(\orbit,10,\dex)
o.setMovement(\random)
o.setMovement(\brown)
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