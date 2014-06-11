(
// "/home/andres/Documents/ss/external audio simulation.scd".load;
a = VBAPSpeakerArray.new(2, [0, 45, 90, 135, 180, -135, -90, -45]); // 8 channel ring
r=Render.new(s,57120,vbapSpeakerConf:a);
r=Render.new(s,57120)
r.verbose=true;
)
r.verbose=false

nil.at(1)


m = NetAddr("127.0.0.1", 57120); // loopback

//start new channels
m.sendMsg("/new", 0);
m.sendMsg("/new", 2);

m.sendMsg("/wid",5,1,1)
m.sendMsg("/typ",1,2)

//remove channels
m.sendMsg("/end", 2);

//change sound position
// (/pos, sourceID, r, azimuth, elevation)

OSCFunc.trace
m.sendMsg("/pos", 0, 0, 0, 0);
m.sendMsg("/pos", 0, 0, pi, 0);
m.sendMsg("/pos", 0, 0, pi/2, pi/4);

m.sendMsg("/pos", 3, 1, pi/2, -pi/4);
m.sendMsg("/pos", 3, 1, pi, pi/2);
m.sendMsg("/pos", 3, 1, 3*pi/2, pi/4);


OSCFunc.trace(true)
OSCFunc.trace(false)


r.encoders.at(0).scope
r.encoders.at(0).set(\r,1)
r.encoders.at(0).set(\ele,pi/2)

1 / (0.1**2);

r.encoders
r.spatialTechnique
r.synthName
r.encoders.at(0).dumpTree

///////////////////////////////////////////////////////////////////
// WITH SOUND SCENE SIMULATOR

(
~world= SSWorld([10,10,10], [0,0,0], 200, 0).showView; //dim,grav,maxvel,damp

o=SSObject(~world,[2,2,0],[0,0,0], [0,0,0], 9,\point,channel:0);
o.locSph_(rho:5);
o.shape_(\ext);
o.locSph_(ele:0);
o.dAzimuth_(pi/4);
)
o.dElevation_()
o.shape_(\ring);
o.shape_(\point)

(
o.locSph_(ele:pi/4);
o.dElevation_(0.5)
o.locSph.elevation
)
(
o.locSph_(ele:0);
o.dElevation_(0)
)
o.locSph.elevation
o.dAzimuth
o.locSph.azimuth+(o.dAzimuth/2)
o.preserveArea_(false)
o.loc_([5,5,10])
o.locSph_(rho:4)
o.loc_(z:2)
~world.getAllObjectNames
~world.maxDistance
~world.dim_([5,5,5])
~world.dim
o.channel
o.remove
o
~world.pause
~world.remove(o)
~o2=SSObject(~world,[2,2,0],[0,0,0], [0,0,0], 9,\point,channel:2);
~o2.o.setMovement(\orbit)

~o3=SSObject(~world,[2,2,0],[0,0,0], [0,0,0], 9,\point,channel:2);
~o3.setMovement(\random)

~o4=SSObject(~world,[2,2,0],[0,0,0], [0,0,0], 9,\point,channel:2);
~o4.loc_([3,0,0])
~o4.setMovement(\shm,[3,3,0],[5,0.5,1])

~o5=SSObject(~world,[2,2,0],[0,0,0], [0,0,0], 9,\point,channel:2);

(
o.setMovement(\random);
~o2.setMovement(\random);
~o3.setMovement(\random);
~o4.setMovement(\random);
~o5.setMovement(\random);
)

~world.worldView.f_(0.75)
~world.update
~world.resume
o.locSph_(rho:2.5)
o.loc_([2.5,0,2.5])
o.loc_(z:5)
o.locSph_(rho:2)
o.locSph_(azi:0)

o.setMovement(\shm,[3,0,0],[5,0.5,1])

o.setMovement(\static)

o.setMovement(\orbit,2,\lev)

o.setMovement(\orbit,5,\dex)
o.setMovement(\static)
o.setMovement(\rect,[1,0,0])
~o2.setMovement(\static)
o.setMovement(\rect,Cartesian(0,0,2))
5.0.rand!3


o.setMovement(\static)
o.loc_([3,0,0])
o.setMovement(\shm,[0,0,2.5],[3,0.5,1])
o.setMovement(\shm)


o=SSObject(~world,SSVector[50,0,0],SSVector.clear,0,1,5,\point);

o.loc