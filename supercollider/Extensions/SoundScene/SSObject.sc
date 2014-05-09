////////////////////////////////////////////////////////////////////////////
//
// Copyright ANDRÉS PÉREZ LÓPEZ, April 2014 [contact@andresperezlopez.com]
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; withot even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>
//
////////////////////////////////////////////////////////////////////////////
//
// SSObject.sc
// Based on RedUniverse quark (by redFrik)
//
// This class implements a Sound Scene Object
//
// Each SSObject instance extends RedObject with:
// -> shape
// -> name id
// -> audio channel associated
// -> movement: dynamic behavior associated
//
////////////////////////////////////////////////////////////////////////////


SSObject : RedObject {

	//adds shape
	var <>shape;
	var <movement;
	////
	var <movementList;
	////
	var <>regLoc; //initialize to start pos
	var <>name;
	var <>channel;

	*new {|world, loc, vel, accel, mass, size, shape, name, channel|
		//almost same method as in RedObject

		^super.newCopyArgs(
			world ?? {SSWorld.new},
			loc !? {if (loc.isArray) {Cartesian.fromArray(loc)} {loc} } ?? {Cartesian(0,0,0)},
			vel !? {if (vel.isArray) {Cartesian.fromArray(vel)} {vel} } ?? {Cartesian(0,0,0)},
			accel !? {if (accel.isArray) {Cartesian.fromArray(accel)} {accel} } ?? {Cartesian(0,0,0)},
			mass ? 1,
			size ? 1
			//change init function
		).initSSObject(shape, name, channel)
	}


	initSSObject { |myshape, myname, mychannel|

		// var numNames;
		regLoc=loc;

		// if shape is a point give it a infinitesimal small value
		shape = myshape ? \point;
		if (shape == \point) {
			size=0.05;
		};

		//channel = mychannel ? 0;
		//name = myname ?? {(\track++channel).asSymbol};
		channel=mychannel;


		name=myname; //if they are nil, they will be assigned by default SSWorld numObjects

		//check if the name already exists in the world: change then if true
		if (world.getAllObjectNames.indicesOfEqual(myname).size>0) {
			name=nil;
			("Name "++myname++" already exists; default name applied").warn;
		};

		this.setMovement(\static); //static as default
		////
		movementList=List.new;
		// this.addMovement(\static);
		////

		this.initRedObject;
	}

	/////////////////////////////////////////////////////////////////////////////////
	// getter / setter methods

	// private:: auto-casting to Cartesian
	setValue { |value,type=\cartesian|

		if (value.isArray) {
			if (type==\cartesian) {
				value=Cartesian.fromArray(value)
			};
			if (type==\spherical) {
				value=Spherical.fromArray(value)
			};
		}

		^value.asCartesian;

	}

	///////////////// loc

	/*	loc_ { |newLoc|
	loc=this.setValue(newLoc);
	}*/

	loc_ { |newLoc,x=nil,y=nil,z=nil|
		newLoc ?? {
			newLoc = loc;
			x !? {newLoc.x = x};
			y !? {newLoc.y = y};
			z !? {newLoc.z = z};
		};
		loc=this.setValue(newLoc);
	}

	/*	locX_ { |newX|
	var newLoc=[newX,]
	}*/

	locSph {
		^loc.asSpherical;
	}

	/*	locSph_ {|newLocSph|
	loc=this.setValue(newLocSph,\spherical);
	}*/

	locSph_ { |newLocSph,rho=nil,azi=nil,ele=nil|
		newLocSph ?? {
			newLocSph = this.locSph;
			rho !? {newLocSph.rho = rho};
			azi !? {newLocSph.azimuth = azi};
			ele !? {newLocSph.elevation = ele};
		};
		loc=this.setValue(newLocSph,\spherical);
	}

	///////////////// vel

	vel_ { |newVel|
		vel=this.setValue(newVel);
	}

	addVel { |newVel|
		vel=vel+this.setValue(newVel);
	}

	velSph {
		^vel.asSpherical;
	}

	velSph_ {|newVelSph|
		vel=this.setValue(newVelSph,\spherical);
	}

	///////////////// accel

	accel_ { |newAccel|
		accel=this.setValue(newAccel);
	}

	accelSph {
		^accel.asSpherical;
	}

	accelSph_ {|newAccelSph|
		accel=this.setValue(newAccelSph,\spherical);
	}


	/////////////////////////////////////////////////////////////////////////////////
	//set
	setMovement { |type=\static ... args|
		movement = switch (type)
		{\static} {Static.new(this)}
		{\rect} {RectMov.new(this,args)}
		{\random} {RandomMov.new(this,args)}
		{\shm} {Shm.new(this,args)}
		{\orbit} {Orbit.new(this,args)};

	}

	/* addMovement { |type=\static ... args|
	var movement = switch (type)
	{\static} {Static.new(this)}
	{\rect} {RectMov.new(this,args)}
	{\orbit} {Orbit.new(this,args)};

	movementList.add(movement);
	}*/


	// update method is called from inside SSWorld update routine
	update {
		/* var newLoc=Cartesian.new;
		var newVel=Cartesian.new;
		var newAccel=Cartesian.new;

		movementList.do{ |mov|
		var nextState=mov.next;
		"next state".postln;
		nextState.postln;
		#newLoc,newVel,newAccel=[newLoc,newVel,newAccel]+nextState;
		};
		"loc".postln;
		accel.postln;
		newAccel.postln;
		this.loc_(loc+newLoc);
		this.vel_(vel+newVel);
		this.accel_(accel+newAccel);*/


		movement.next;
		// movementList.do(_.next);


		// vel=movement.next;
		// super.update;


		/* if (movement.type == \orbit) {


		} {
		vel= (vel+accel).limit(world.maxVel);
		loc= loc+vel;
		accel= 0;
		}*/
	}

}