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
	var <>regLoc; //initialize to start pos
	var <>name;
	var <>channel;

	*new {|world, loc, vel, accel, mass, size, shape, name, channel|
		//almost same method as in RedObject

		^super.newCopyArgs(
			world ?? {SSWorld.new},
			loc ?? {SSVector.clear},
			vel ?? {SSVector.clear},
			accel ?? {SSVector.clear},
			mass ? 1,
			size ? 1
		//change init function
		).initSSObject(shape, name, channel)
	}

	initSSObject { |myshape, myname, mychannel|

		// var numNames;
		regLoc=loc;

		shape = myshape ? \point;
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

		this.initRedObject;
	}


	locSph { //spherical coordinates
		^loc.asSpherical;
	}

	//set
	setMovement { |type=\static ... args|
		movement = switch (type)
		{\static} {Static.new(this)}
		{\orbit} {Orbit.new(this,args)};

	}


	update {

		vel=movement.next;

		super.update;
	}

}