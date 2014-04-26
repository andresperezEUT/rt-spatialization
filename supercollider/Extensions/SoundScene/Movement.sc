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
		// ^Cartesian();
	}
}

RectMov : Movement {

	*new { |object|
		^super.new(object).initRect;
	}

	initRect {
		type=\rect;

	}

	next {
		var step=object.world.stepFreq;

		object.vel_(object.vel+object.accel); /*.object.world.limit(world.maxVel)*/
		object.loc_(object.loc+(object.vel/step));
		object.accel_([0,0,0]);
	}
}

// TODO: it is not correctly implemented!!!!

Orbit : Movement {
	// movimiento circular con respecto al centro
	// TODO: ADD Z DIMENSION
	// TODO: PUT THIS AS AN OPTION...



	var <>dir;
	var <>angularVel=0;
	var <>taccel=0;

	*new { |object, args|
		^super.new(object).initOrbit(args)
	}

	initOrbit { |args| // |velMag, dir|
		angularVel = args[0] ? 1;
		dir = args[1] ? \dex;
		type=\orbit;
	}

	next {
		var step=object.world.stepFreq;

		angularVel= angularVel+taccel*(1-object.world.damping); //no need to limit to maxVel as angular
		taccel= 0;

		//set new pos
		//normalize respect real seconds
		if (dir == \lev) {
			object.locSph_(object.locSph.addAzimuth(angularVel/step));
		} {
			object.locSph_(object.locSph.addAzimuth(angularVel.neg/step));
		};

	}
}