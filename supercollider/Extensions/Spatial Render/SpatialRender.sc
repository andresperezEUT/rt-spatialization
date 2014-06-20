////////////////////////////////////////////////////////////////////////////
//
// Copyright ANDRÉS PÉREZ LÓPEZ, May 2014 [contact@andresperezlopez.com]
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
// Render.sc
//
// This class implements a spatial render
//
// Render instance receives OSC messages from an external spatialization software
// and applies spatial coefficients to the existing input channels
//
// Each different source/channel is tracked inside <>encoders dictionary
//
// Distance cues are supported by means of distance attenuation and air absorption simulation
//
// TODO:
// -> support room/environmental parameters: send maxDistance for the delay!!! <-------
// create a room model inside the render
//
// --> BORRAR OSCRECEIVERS EN LA FUNCIÓN CLOSE!!! QUE ESTÁ MAL IMPLEMENTADO
//
////////////////////////////////////////////////////////////////////////////

SpatialRender {

	var <server;
	var <port;
	var <sourceAddress;

	var <>encoders;
	var shapeEncoders;
	var <spatialTechnique;
	var <synthName;
	var <vbapSpeakerConf,<vbapSpeakerBuffer,<vbapNumSpeakers;

	var <>verbose=true;

	var <oscLogger, <logOSC = true;
	var <autosaveOSC = true, <>autosavePeriod=10;
	var autosaveTask;

	var receiverFunctions;

	*new{ |server, port=57120, spatialTechnique=\ambisonics, ambiOrder=3, vbapSpeakerConf|
		^super.new.init(server,port,spatialTechnique,ambiOrder,vbapSpeakerConf);
	}

	init { |myServer,myPort,myTechnique,ambiOrder,myVbapSpeakerConf|

		// // // // INIT FUNCTIONALITY // // // //
		server = myServer;

		//init OSC parameters
		port=myPort;
		sourceAddress=NetAddr("127.0.0.1", port); //change to allow any (external) ip

		//init Synths reference
		spatialTechnique=myTechnique;

		//get synths names
		switch(spatialTechnique)
		{\ambisonics} {
			synthName = switch(ambiOrder)
			{1} {\ambiEncoder1}
			{2} {\ambiEncoder2}
			{3} {\ambiEncoder3}
		}
		{\vbap} {synthName = \vbapEncoder};

		//init vbap synthdef
		if (myVbapSpeakerConf.isNil.not) {

			vbapSpeakerConf=myVbapSpeakerConf;
			vbapSpeakerBuffer=vbapSpeakerConf.loadToBuffer;
			vbapNumSpeakers=vbapSpeakerConf.numSpeakers;

		} {
			// if no vbap is used, just give some values for the synthdef not to crash
			vbapSpeakerBuffer=Buffer.new;
			vbapNumSpeakers=1;

		};


		encoders=Dictionary.new;
		shapeEncoders=Dictionary.new;

		receiverFunctions=Dictionary.new;

		// // // // RECEIVER FUNCTIONS // // // //

		OSCdef(\addEntity,this.newSource,"/spatdifcmd/addEntity",nil);
		OSCdef(\removeEntity,this.endSource,"/spatdifcmd/removeEntity",nil);


		// // // // INIT SYNTHDEFS // // // //

		this.initSynthDefs;

		// // // // INIT OSC LOGGER // // // //

		oscLogger = SpatDifLogger.new.log_(logOSC);

		// create a routine that auto-saves the spatdif logger in a regular way
		autosaveTask = Task({
			inf.do({
				oscLogger.save;
				autosavePeriod.wait;
			})
		});
		if (autosaveOSC) {autosaveTask.start};
	}

	autosaveOSC_ { |bool|
		if (bool) {
			autosaveTask.resume;
		} {
			autosaveTask.pause;
		};
		autosaveOSC = bool;
	}

	logOSC_ { |bool|
		logOSC = bool;
		oscLogger.log_(bool);
	}

	addReceiverFunction { |name|
		var entityString = "/spatdif/source/"++name;

		var newFunc = {	|msg, time, replyAddr, recvPort|
			switch(msg[0].asString)

			{entityString++"/media/type"} {this.mediaType(name,msg[1]) }
			{entityString++"/media/channel"} { this.mediaChannel(name,msg[1])}
			{entityString++"/position"} {this.setPosition(name,msg[1],msg[2],msg[3],msg[4])}
			{entityString++"/type"} {this.sourceType(name,msg[1])}
			{entityString++"/present"} {this.sourcePresent(name,msg[1])}
			{entityString++"/width"} {this.sourceWidth(name,msg[1],msg[2])}
			{entityString++"/preserveArea"} {this.preserveArea(name,msg[1])}
			{

			};

		};

		receiverFunctions.add(name -> newFunc)
		/* ^{ |msg, time, replyAddr, recvPort|
		switch(msg[0].asString)

		{entityString++"/media/type"} {this.mediaType(name,msg[1]) }
		{entityString++"/media/channel"} { this.mediaChannel(name,msg[1])}
		{entityString++"/position"} {this.setPosition(name,msg[1],msg[2],msg[3],msg[4])}
		{entityString++"/type"} {this.sourceType(name,msg[1])}
		{entityString++"/present"} {this.sourcePresent(name,msg[1])}
		{entityString++"/width"} {this.sourceWidth(name,msg[1],msg[2])}
		{entityString++"/preserveArea"} {this.preserveArea(name,msg[1])}
		{

		};
		}*/
	}


	// // // // OSC FUNCTIONS // // // // // // // // // // // // // // // // // // // //

	// TODO: no crear una instancia si el nombre ya existe!!!

	// TODO: guardar instancia de la funcion de addOSCRecvFunc para poder borrarla despues!!
	newSource {
		^{ |msg, time, addr, recvPort|
			var name = msg[1];

			//var entityString = "/spatdif/source/"++name;

			// create responders for this new source

			this.addReceiverFunction(name);
			thisProcess.addOSCRecvFunc(receiverFunctions.at(name));

			/* thisProcess.addOSCRecvFunc({ |msg, time, replyAddr, recvPort|
			switch(msg[0].asString)

			{entityString++"/media/type"} {this.mediaType(name,msg[1]) }
			{entityString++"/media/channel"} { this.mediaChannel(name,msg[1])}
			{entityString++"/position"} {this.setPosition(name,msg[1],msg[2],msg[3],msg[4])}
			{entityString++"/type"} {this.sourceType(name,msg[1])}
			{entityString++"/present"} {this.sourcePresent(name,msg[1])}
			{entityString++"/width"} {this.sourceWidth(name,msg[1],msg[2])}
			{entityString++"/preserveArea"} {this.preserveArea(name,msg[1])}
			{

			};
			})*/
		}
	}

	mediaType { |name, type|
		if (type == \jack) {
			var newGroup;
			var newBus;

			var newShapeEncodersDict;

			var channel = 0; //default

			if (verbose) {
				"NEW SOURCE---".postln;
				("name: "++name.asString).postln;
				// ("channel: "++channel.asString).postln;
			};

			// general schema
			//
			// ................................newGroup................................
			// |                                                                      |
			// |                                                                      |
			// ----(In.ar)---> [distance] ----(newBus)---> [ambiEncoder, ambiEncoderRing, ambiEncoderExt] -/-/-(Out.ar)-/-/->
			//                                        \--> [vbapEncoder] -/-/-(Out.ar)-/-/->

			// actions
			// 1. create group
			// 2. create mono audio bus
			// 3. create distance synth, add to head of group
			// 4, create encoding synth(s), add to tail group

			// 1. create group
			newGroup = Group.new;
			encoders.add(name -> newGroup);

			newShapeEncodersDict = Dictionary.new;
			shapeEncoders.add(name -> newShapeEncodersDict);

			// 2. create mono audio bus
			newBus = Bus.audio(server,1);

			// 3. create distance synth, add to head of group
			Synth(\distanceEncoder,[\externalIn,channel,\r,1,\busOut,newBus],target:newGroup,addAction:\addToHead);

			// 4, create encoding synth, add to tail group
			switch(spatialTechnique)
			{\ambisonics} {
				// initialize with point source encoder, create paused other synths
				var point = Synth(synthName,[\busIn,newBus],target:newGroup,addAction:\addToTail);
				var ring = Synth.newPaused(synthName++\Ring,[\busIn,newBus],target:newGroup,addAction:\addToTail);
				var ext = Synth.newPaused(synthName++\Ext,[\busIn,newBus],target:newGroup,addAction:\addToTail);
				var mer = Synth.newPaused(synthName++\Mer,[\busIn,newBus],target:newGroup,addAction:\addToTail);
				// add them to the shapeEncoders dictionary
				newShapeEncodersDict.add(\point -> point);
				newShapeEncodersDict.add(\ring -> ring);
				newShapeEncodersDict.add(\ext -> ext);
				newShapeEncodersDict.add(\mer -> mer);
			}
			{\vbap} {
				Synth(synthName,[\busIn,newBus,\numChannels,vbapNumSpeakers,\speakerBuffer,vbapSpeakerBuffer],target:newGroup,addAction:\addToTail)
			};


			//TODO: check if source already exists

			/* encoders.add(channel -> Synth(\ambiEncoder,args:[\busIn,channel,\r,r,\azi,azimuth,\ele,elevation],addAction:'addToTail'))*/
		}
	}

	mediaChannel { |name, channel|
		encoders.at(name).set(\externalIn,channel);

		if (verbose) {
			"*********".postln;
			("name: "++name).postln;
			("channel: "++channel.asString).postln;
		};
	}


	setPosition { |name,azimuth,elevation,r,convention|

		// todo: transformaciones según la convención!!
		// todo: para el ambisonics, pasar a radianes!!
		// TODO: vbap parameters should be in degrees!!!

		if (verbose) {
			"*********".postln;
			("name: "++name).postln;
			("r: "++r).postln;
			("azimuth: "++azimuth).postln;
			("elevation: "++elevation).postln;
		};

		if (spatialTechnique==\ambisonics) { // convert to rad
			azimuth = azimuth.degree2rad;
			elevation = elevation.degree2rad;
		};

		// send parameters to all nodes inside their group
		encoders.at(name).set(\r,r);
		encoders.at(name).set(\azi,azimuth);
		encoders.at(name).set(\ele,elevation);
	}


	sourceType { |name,type|

		var ambiEncoders;

		if (verbose) {
			"*********".postln;
			("name: "++name).postln;
			("type: "++type).postln;
		};

		// TODO: make this more elegant (ambi encoders subgroup, msg bundle, etc...)

		///////////////// CAMBIAR ESTO A SYMBOLS CON LOS NOMBRES!!!

		ambiEncoders=shapeEncoders.at(name); //this is a dictionary
		// activate corresponding synth
		switch(type)
		{\ring} {ambiEncoders.at(\point).run(false); ambiEncoders.at(\ring).run(true); ambiEncoders.at(\ext).run(false); ambiEncoders.at(\mer).run(false)}
		{\extended} {ambiEncoders.at(\point).run(false); ambiEncoders.at(\ring).run(false); ambiEncoders.at(\ext).run(true); ambiEncoders.at(\mer).run(false)}
		{\meridian} {ambiEncoders.at(\point).run(false); ambiEncoders.at(\ring).run(false); ambiEncoders.at(\ext).run(false); ambiEncoders.at(\mer).run(true)}
		/*point*/{ambiEncoders.at(\point).run(true); ambiEncoders.at(\ring).run(false); ambiEncoders.at(\ext).run(false); ambiEncoders.at(\mer).run(false)}

	}

	sourcePresent { |name,present|
		encoders.at(name).run(present);
	}


	sourceWidth { |name, da, de|

		if (verbose) {
			"*********".postln;
			("name: "++name).postln;
			("da: "++da).postln;
			("de: "++de).postln;
		};

		// send parameters to all nodes inside their group
		encoders.at(name).set(\dAzi,da.degree2rad);
		encoders.at(name).set(\dEle,de.degree2rad);
	}


	preserveArea { |name,preserveArea|

		if (verbose) {
			"*********".postln;
			("name: "++name).postln;
			("preserve area: "++preserveArea).postln;
		};

		// send parameters to all nodes inside their group
		encoders.at(name).set(\preserveArea,preserveArea);
	}

	// if source channel does not exist, will throw error, since nil.at() is not defined
	// is not the case for the other functions
	// TODO: implement channel/name checker


	endSource {
		^{ |msg, time, addr, recvPort|
			var name=msg[1];

			if (verbose) {
				"END SOURCE---".postln;
				("name: "++name.asString).postln;
			};

			//TODO!!! avoid click

			// free all nodes inside the group
			encoders.at(name).free; //group.freeAll;
			//then remove the association
			encoders.removeAt(name);
			shapeEncoders.removeAt(name);
		}
	}

	close {
		//remove OSC receivers ----> TODO!! ESTÁ MAL IMPLEMENTADO
		receiverFunctions.do{|f| thisProcess.removeOSCRecvFunc(f)};
		// stop autosave task
		autosaveTask.stop;
		// close file
		oscLogger.save;
		oscLogger.close;
	}
}