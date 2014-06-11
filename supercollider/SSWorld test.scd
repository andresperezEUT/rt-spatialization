(
~world= SSWorld([10,10,10], [1,0,0], 200, 0).showView; //dim,grav,maxvel,damp

o=SSObject(~world,[0,2,2],[0,0,0], [0,0,0], 9,\point).setMovement(\static); //world,loc,vel,acc,mass,size,shape
// ~world.pause;*
// o=SSObject(~world,[1,1,0],[0,0,0], 0, 1, 0.1,\point,channel:1)
)
(
o.locSph_(azi:0);
o.locSph_(ele:1*pi/4);
o.locSph.postln;
)

(
c=OrientationController.new.setAction(\orientation,{|a|
	//a.postln;
	/*	var azi=a.degree2rad.fold(0,2pi);
	var ele=e.neg.degree2rad;*/
	o.locSph_(azi:a[0],ele:a[1]);
	// [a,e].postln;
	// o.locSph.postln;
	// "-------".postln;
})
)

c.logging

c.log_(true);
c.log_(false);

c.logger.do(_.postln)

c.replayTask.do{|t|t.play}
c.replay
c.pause
c.resume
c.stop


t=c.orientationLogger.asArray.flop[3]

c.replay(\orientation,1)
c.pause
c.resume(\orientation)




Main.elapsedTime;
a=List.new
a.add(1)
(
c=OrientationController.new.setAction(\gravity,{|g,time|
	g.postln;
	Main.elapsedTime.postln;
	"********".postln;
})
)

(
~lastX=0;
~lastY=0;
~lastZ=0;
c=OrientationController.new;
c.setAction(\acceleration,{|acc,time|
	~diffX=acc[0]-~lastX;
	~diffY=acc[1]-~lastY;
	~diffZ=acc[2]-~lastZ;

	~diffZ.postln;
	if (abs(~diffZ)>10) {"******".postln};


	~lastX=acc[0];
	~lastY=acc[1];
	~lastZ=acc[2];
})
)


(

~accList=List.new(200);
~accList2=List.new(200);
~acc=0;
~lastAcc=0;
~th=30;
~interval=0.2; //seconds
~timeDiff=0;
~lastUpdate=0;

~lastX=0;
~lastY=0;
~lastZ=0;
c=OrientationController.new;
c.setAction(\acceleration,{|acc,time|
	var x=acc[0];
	var y=acc[1];
	var z=acc[2];
	var timeDiff=time-~lastUpdate;


	var force = abs(x+y+z-~lastX-~lastY-~lastZ);
	if (force>~th and:{timeDiff>~interval}) {
		[force,acc].postln;"***".postln;
		~lastUpdate=time;
	};
	~accList.add([acc,force].flat);

	~lastX=x;
	~lastY=y;
	~lastZ=z;
	// time.postln;
	//acc.postln;
/*	~timeDiff=time-~lastUpdate;
	~lastAcc=acc;*/
/*	~acc=sqrt((acc[0]*acc[0])+(acc[1]*acc[1])+(acc[2]*acc[2]));
	~accList.add([acc,~acc].flat);*/

/*	~lastAcc[2].postln;
	~force=(acc[0]+acc[1]+acc[2]-~lastAcc[0]-~lastAcc[1]).postln;
	~force=~force-~lastAcc[2];
	~force.postln;*/
	// ~force.postln;
	// ~force=~fors.postln;

/*	// ~acc.postln;
	if (~acc>~th) {
		if (~acc>~lastAcc and:{~timeDiff>~interval}) {
			acc.postln;
			"***".postln;
			~lastUpdate=time;
		};
	};*/

})
)
~accList.asArray.flop.plot;
[1,2,3].plot

[[1,2,3],4].flat
~accList.first
~accList.last
(
~accList.asArray.flop[1].plot;
~accList.asArray.flop[2].plot;
)
o.locSph_(azi:5)
// (
// o.vel_([0,0,1]);
// o.addForce([1,0,0])
// o.friction_(true)
// o.stop
// o.vel;
// o.accel;
// ~world.gravity_([1,1,0])
// ~world.damping_(0.5)
// ~world.friction_(0.2)
// ~world.dim.x_(5)
// )
// Render
//
// o.gravity_(true)
// o.friction
// o.gravity_(false)
//
// o.movement.period
// o.loc_([5,0,5]);
// ~world.update
//
// o.movement.step_(0.1)
//
//
// o.vel_([0,0,2])
//
// o.loc_([1,0,0])
//
// ~world.resume
// o.setMovement(\orbit,10,\dex)
// o.setMovement(\random)
// o.setMovement(\brown)
// o.setMovement(\rect,Cartesian(0,0,2))
// 5.0.rand!3
//
// o.setMovement(\static)
//
// o.setMovement(\shm,[1,0,2],[2,1,1])
// o.setMovement(\shm)
// o.movement.amp=1;
// o.movement.yAmp_(1)
// o.movement.yT_(1)
//
// [1].size
//
// o.movement
// o.vel_([1,0,1]);
// o.accel_([10,10,10])
//
// o.vel.class.postln
//
// (0..1)
// o.loc((0..1))
//
// ~world.showNames(true)
// ~world.dim
// o.loc_([3,0,0])
// o.loc_([1,1,5])
// ~world.dim.z
//
// o.loc
//
// 1.neg
//
// ~world.gravity
// ~world.objects.at(1).loc_(Spherical(1,0,0).asCartesian)
// ~world.objects.at(1).loc_(SVector)
// pixelSize
//
// o.vel
// o.loc_(Cartesian(5,0,9))
// o.loc
// ~world.contains(o)
// ~world.contain(o)
// a=Spherical(1,0,0)
// a.asCartesian
//
// o.locSph.x
// o.loc
//
// o.velSph
// o.vel
//
// o.locSph_([1,pi/2,-pi/4])
// o.size_(0.1)
//
// o.class
// o.world.center
// o.movement.object.loc
//
//
// o.movement.next
// o.movement.taccel=0.1;
//
//
// ~world.damping_(0)
// ~world.update
//
// o.vel
// o.vel_([0.1,0.1,0])
// o.accel_([0.1,1,0])
// o.setMovement(\orbit,0.2)
//
// ~world.stepFreq_(60)
// o.setMovement(\orbit,2pi,\lev)
// ~world.viewDiff=false
// ~world.aziDiff=1.degree2rad
// o.setMovement(\static)
//
// ~world.showView
// ~world.hideView
// ~world.pause
// ~world.resume
// ~world.objects.do(_.vel_(SSVector[10,10,10].rand);)
// ~world.objects.at(1).loc_(SSVector[100,200,0])
//
// // OSC MONITOR
// (
// OSCdef("/object_loc", {|msg, time, addr, recvPort| msg.postln}, "/pos", nil); // def style
// OSCdef("/new", {|msg, time, addr, recvPort| msg.postln}, "/new", nil); // def style
// )
//
//
// r=Render.new;
//
// ~world.alwaysOnTop(true)
//
// ~world.window.refresh
//
// 0.5.linlin(0,1,10,100)
// Font