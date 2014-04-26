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
// Movement.sc
//
// This class implements some discrete dynamic behaviors for the Sound Scene simulation
//
// Each instance is associated with the SSObject instance that has this concrete behavior
//
// TODO:
// -> support several dynamic behaviors
// -> scale vectors to behave in standard units (angular velocity, newtons, etc...)
//
////////////////////////////////////////////////////////////////////////////

Movement {

	var <>object; // a SS object
	var <type;

	*new { |object|
		^super.new.initMovement(object)
	}

	initMovement{ |myobject|
		object=myobject;
	}

	next {
		// to be implemented by subclasses
	}

}

Static : Movement {

	*new { |object|
		^super.new(object).initStatic;
	}

	initStatic {
		type=\static;
	}

	next {
		^SSVector.clear;
	}
}

// TODO: it is not correctly implemented!!!!

Orbit : Movement {
	// movimiento circular con respecto al centro
	// TODO: ADD Z DIMENSION
	// TODO: ADD VELOCITY MAGNITUDE
	// TODO: PUT THIS AS AN OPTION...

	var <>velMag;
	var <>dir;

	var angularVel,taccel;

	*new { |object, args|
		^super.new(object).initOrbit(args)
	}

	initOrbit { |args| // |velMag, dir|
		velMag = args[0] ? 1;
		angularVel = args[0] ? 1;
		dir = args[1] ? \dex;
		type=\orbits;
	}

	next {
/*		var theta;
		angularVel= angularVel+taccel*(1-world.damping); //no need to limit to maxVel as angular
		theta=object.locSph.theta+angularVel;
		object.locSph;
		object.locSph_(object.locSph);
		theta= theta+angularVel;
		taccel= 0;*/


/*		//cartesian version
		var pos, center;
		var place, vectorR, vectorV, vel;
		var vectorN;

		pos=object.loc;
		center=object.world.center;

		place=SSVector[pos[0],pos[1],center[2]];

		vectorR=place-center;

		vectorV = switch (dir)
		{\dex} {SSVector[vectorR[1],vectorR[0].neg,0]}  //dextrorotation: clockwise [y,-x]
		{\lev} {SSVector[vectorR[1].neg,vectorR[0],0]}; //levorotation:   counter-clockwise [-y,x]

		///////////////
		// "******".postln;
		vel=vectorV.normalize/*.postln*/;

		vel=vel*velMag;

		^vel;*/



	}
}