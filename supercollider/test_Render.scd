(
// "/home/andres/Documents/ss/external audio simulation.scd".load;
r=Render.new(57120);
r.verbose=true;
)
r.verbose=false

m = NetAddr("127.0.0.1", 57120); // loopback

//start new channels
m.sendMsg("/new", 0);
m.sendMsg("/new", 2);

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


///////////////////////////////////////////////////////////////////
// WITH SOUND SCENE SIMULATOR

(
~world= SSWorld([20,20,10], [0,0,0], 200, 0).showView; //dim,grav,maxvel,damp

o=SSObject(~world,[2,2,0],[0,0,0], [0,0,0], 9,\point,channel:0);
)
~world.pause
~o2=SSObject(~world,[2,2,0],[0,0,0], [0,0,0], 9,\point,channel:2);
~o2.setMovement(\orbit)

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


~world.update
~world.resume
o.locSph_(rho:2.5)
o.locS
o.loc_(y:0)
o.loc_(z:2.5)

o.setMovement(\shm,[3,0,0],[5,0.5,1])

o.setMovement(\random)

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