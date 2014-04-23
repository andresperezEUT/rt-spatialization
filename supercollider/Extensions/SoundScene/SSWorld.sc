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
// SSWindow.sc
// Based on RedUniverse quark (by redFrik)
//
// This class implements a Sound Scene World, which is a special case of RedWorld type 1 (without walls)
//
// Extended features:
// -> auto drawing function with nice cenital perspective and object names and shapes display
// -> dictionary for storing objects by name
// -> internal timing functions for the simulation
// -> OSC messages with adjustable JND variations for sending to the spatial render
//
//
//
// TODO:
// -> object representations according to their shapes
// -> adjust time steps to seconds
////////////////////////////////////////////////////////////////////////////

SSWorld : RedWorld1 { //default with walls

	var time, <>stepFreq;
	var <window;

	var <>rDiff, <>aziDiff, <>eleDiff;

	var <>viewDiff;

	var <>address;

	var <numObjects=0;

	var <objectsID;

	*new {|dim, gravity, maxVel, damping, timeStep=60|

		^super.newCopyArgs(
			dim ?? {SSVector[300, 300, 300]},
			gravity ?? {SSVector[0, 0.98, 0]},
			maxVel ? 10,
			damping ? 0.25
		).initSSWorld(timeStep);
	}

	initSSWorld{ |mystepFreq|
		stepFreq=mystepFreq;
		//defaults
		rDiff=1;
		aziDiff=1.degree2rad;
		eleDiff=5.degree2rad;
		viewDiff=false;

		address=NetAddr.localAddr;


		//view
/*		window= SSWindow("SS-WORLD", Rect(0,0,dim[0],dim[1]),dimVector:dim);*/
		window= RedQWindow("SS-WORLD", Rect(0,0,dim[0],dim[1])).background_(Color.white);
		window.draw{



			Pen.translate(dim@0/2,dim@1/2,0);
			Pen.strokeColor=Color.black;
			Pen.alpha=1;
/*			Pen.addArc((dim[0]/2)@(dim[1]/2),100,0,2pi);*/
			// TODO: make the circle in 3d perspective!
			// TODO: add circle independently of if there is any object (see this.update)
			Pen.addArc(0@0,100,0,2pi);

			/////////////////
			// Pen.rotate(-pi/2,0,0);
			/////////////////

			//TODO: not create a new instance of SSObject for each draw! make a visual object instead

			if (this.objects.size > 0 ) {
				this.objects.do { |obj|

					// mirroring!
					var x=(obj.loc@1).neg;
					var y=(obj.loc@0).neg;
					var z=obj.loc@2;

					var a,b;

					var newObj;
					newObj=obj.copy.loc_(SSVector[x,y,z]); //take care not to overwrite the actual object!!

					Pen.alpha=1;
					//write object name
					Pen.stringAtPoint(newObj.name,Rect.aboutSSObject(newObj,f:0.75).rightBottom,Font("Helvetica", 12),Color.red);

/*					Rect.aboutSSObject(newObj).rightBottom.postln;
					Rect.aboutSSObject(newObj).leftBottom.postln;
					Rect.aboutSSObject(newObj).rightTop.postln;
					Rect.aboutSSObject(newObj).leftTop.postln;*/

					// Pen.stringLeftJustIn(newObj.name,Rect.aboutSSObject(newObj),Font("Helvetica", 20),Color.red);
					//draw object
					Pen.strokeColor= Color.black;
					Pen.strokeRect(Rect.aboutSSObject(newObj));
					//draw shadow
					Pen.fillColor= Color.gray;
					Pen.alpha=0.5;
					//--place the shadow at the bottom
					Pen.fillRect(Rect.aboutSSObject(newObj.loc_(SSVector[x,y,dim@2])));
					//draw line between the object and its shadow
					//TODO: change names!
					a=Rect.aboutSSObject(newObj.loc_(SSVector[x,y,z])).center;
					b=Rect.aboutSSObject(newObj.loc_(SSVector[x,y,dim@2])).center;
					Pen.line(a,b);
					Pen.alpha=0.1;
					Pen.stroke;

				};
			};

			//undo transformations
			// Pen.rotate(pi/2,0,0);
			Pen.translate(dim@0,dim@1,0);
		};
		//
		// task managing objects update and time passing
		time=Task({
			inf.do{
				this.update;
				stepFreq.reciprocal.wait;
			}

		}).start;

		// from initRedWorld
		// objects=[];
		objects=Dictionary.new;

		objectsID=Dictionary.new;


		// objects= Dictionary.new; // inRedWorld
		RedUniverse.add(this);						//add world to universe
		this.prInitSurroundings;

	}

	add { |obj|

		// super.add(obj); // save object in objects array and set object's world to this
		objects.add(numObjects -> obj);
		obj.world= this; //maybe this is redundant???
		//////
		obj.channel = obj.channel ? numObjects;
		obj.name = obj.name ?? {(\track++numObjects).asSymbol};
		// obj.name=(\track++numObjects).asSymbol;

		//internal dictionaries;
		objectsID.add(numObjects -> obj.name);

		numObjects=numObjects+1;


		//SEND NEW_OBJECT AND POSITION MESSAGE
		//TODO: DICCIONARIO CON LOS OBJETOS PARA MANEJARLOS POR NOMBRE!!!
		address.sendMsg("/new",obj.channel);
		address.sendMsg("/pos",obj.channel,obj.loc[0],obj.loc[1],obj.loc[2]);
	}

	center {
		// ^dim/2;
		^SSVector.clear;
	}

	pause {
		time.pause;
	}

	resume {
		time.resume;
	}

	update {
		if (this.objects.size > 0 ) {
			this.objects.do { |obj,i|
				// TODO: addForce??
				var lastPos,newPos,aziDif,eleDif;
				var lastO,rLastO,aziLastO,eleLastO;
				var newO,rNewO,aziNewO,eleNewO;

				var updateReg;


				// TODO: IMPLEMENT THIS COMPACT!!
				// lastPos=obj.loc;
				lastPos=obj.regLoc;
				lastO=lastPos-this.center; //position centerend around wold center
				lastO=Cartesian(lastO[0],lastO[1],lastO[2]);
				rLastO=lastO.rho; //automatic casting to cartesian, polar, spherical!!
				aziLastO=lastO.theta;
				eleLastO=lastO.phi;

				///////////
				//update
				obj.update;
				this.contain(obj); //wrap it into world limits
				///////////

				// TODO: IMPLEMENT THIS COMPACT!!
				newPos=obj.loc;
				newO=newPos-this.center; //position centerend around wold center
				newO=Cartesian(newO[0],newO[1],newO[2]);
				rNewO=newO.rho; //automatic casting to cartesian, polar, spherical!!
				aziNewO=newO.theta;
				eleNewO=newO.phi;

				// compare variation!!
/*				["last",aziLastO,eleLastO].postln;
				["new",aziNewO,eleNewO].postln;
				["rDif",abs(rNewO-rLastO)].postln;
				["aziDif",abs(aziNewO-aziLastO)].postln;
				["eleDif",abs(eleNewO-eleLastO)].postln;*/


				// TODO; MERGE THESE IFS INTO ONE!!
				updateReg=false;
				if (abs(rNewO-rLastO) > rDiff) {
					//notify
					// "R DIFF".postln;
					//update
					updateReg=true;
				};
				if (abs(aziNewO-aziLastO) > aziDiff) {
					//notify
					// "azi diff".postln;
					//update
					updateReg=true;
				};
				if (abs(eleNewO-eleLastO) > eleDiff) {
					//notify
					// "ele diff".postln;
					//update
					updateReg=true;
				};



				//update reg
				if (updateReg) {

					obj.regLoc_(newPos);

					/////////////////////////////////////////
					//FORMAT: object id, x,y,z
					// address.sendMsg("/object_loc",i,obj.loc[0],obj.loc[1],obj.loc[2]);
					/////////////////////////////////////////
					//FORMAT: object id, phi,theta (CAUTION: DEFINED INVERSELY IN Spherical.sc)

					address.sendMsg("/pos",i,/*obj.locSph.rho,*/obj.locSph.theta,obj.locSph.phi);

					/////////////////////////////////////////
					// see what is reported
					if (viewDiff) {
						this.updateView;
					}
				};

				// see in a continuous way
				if (not(viewDiff)) {
					this.updateView;
				}

			}
		}
	}

	updateView {
		{window.refresh}.defer;
	}

	///////////////// view
	// view: only Qt
	showView {
		window.visible_(true);
	}

	hideView {
		window.visible_(false)
	}

	////////////////////////////////////////
	////
	//// accessing objects

	// get internal ID for a given object name
	getObjectID { |objName|
		^objectsID.findKeyForValue(objName);
	}

	// get object instance for a given object name
	getObject { |objName|
		^objects.at(this.getObjectID(objName));
	}

	// get an array of all existing objects
	getAllObjects {
		^objects.values.asArray;
	}

	// get an array of all names of existing objects
	getAllObjectNames {
		^objectsID.values.asArray;
	}





}