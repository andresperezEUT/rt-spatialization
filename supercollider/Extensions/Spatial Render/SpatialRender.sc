////////////////////////////////////////////////////////////////////////////
//
// Copyright ANDRÉS PÉREZ LÓPEZ, September 2014 [contact@andresperezlopez.com]
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
// SpatialRender.sc
//
// This class implements a spatial render
//
// Render instance receives OSC messages from an external spatialization software
// and applies spatial coefficients to the existing input channels
//
// Each different source/channel is tracked inside encoders dictionary
//
// Distance cues are supported by means of distance attenuation and air absorption simulation
//
// ATK Binaural SynthDefs are treated specially, since head models should be loaded into buffers before instanciating Synths...
//
// TODO:
// -> support room/environmental parameters: send maxDistance for the delay!!! <-------
// create a room model inside the render
//
//   -> TODO: binaural options: change model, subjectID "dynamically"
//
// --> BORRAR OSCRECEIVERS EN LA FUNCIÓN CLOSE!!! QUE ESTÁ MAL IMPLEMENTADO
//
////////////////////////////////////////////////////////////////////////////

SpatialRender {

	var <server;
	var <port;
	var <sourceAddress;

	// Dictionary with references to the Synth groups (one for each track)
	var <encoders;
	// Dictionary with references to the individual synths: [\distanceEncoder, \pannerEncoder]
	var <encodersReferences;
	//Dictionary with references to the internal audio buses between synths
	var <internalBuses;


	var <spatialTechnique;
	var <synthName;
	var <vbapSpeakerConf,<vbapSpeakerBuffer,<vbapNumSpeakers;

	var <binauralDecoder;
	var <binauralSubjectID;

	// Dictionary storing ambisonics parameters for each source: [order,shape]
	var <ambisonicsParameters;
	var <defaultAmbisonicsOrder;
	var <defaultAmbisonicsShape;

	var <>verbose=true;

	var <oscLogger, <logOSC = true;
	var <autosaveOSC = true, <>autosavePeriod=10;
	var autosaveTask;

	var receiverFunctions;

	*new{ |server, port=57120, spatialTechnique=\ambisonics, ambiOrder=3, vbapSpeakerConf, hrtfHead = \listen, hrtfID = 1002 |
		^super.new.init(server,port,spatialTechnique,ambiOrder,vbapSpeakerConf,hrtfHead,hrtfID);
	}

	init { |myServer,myPort,myTechnique,ambiOrder,myVbapSpeakerConf,hrtfHead,hrtfID|

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

		{\vbap} {
			synthName = \vbapEncoder
		}

		{\binaural} {
			synthName = \binauralEncoder
		}
		;

		// init ambisonics defaults
		defaultAmbisonicsOrder = 3;
		defaultAmbisonicsShape = \point;

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
		encodersReferences = Dictionary.new;
		internalBuses = Dictionary.new;

		ambisonicsParameters = Dictionary.new;

		receiverFunctions=Dictionary.new;

		// // // // RECEIVER FUNCTIONS // // // //

		OSCdef(\addEntity,this.newSource,"/spatdifcmd/addEntity",nil);
		OSCdef(\removeEntity,this.endSource,"/spatdifcmd/removeEntity",nil);


		// // // // INIT SYNTHDEFS // // // //

		// binaural init
		this.setBinauralDecoder(hrtfHead,hrtfID);

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
	}


	// // // // OSC FUNCTIONS // // // // // // // // // // // // // // // // // // // //

	// TODO: no crear una instancia si el nombre ya existe!!!

	// TODO: guardar instancia de la funcion de addOSCRecvFunc para poder borrarla despues!!
	newSource {
		^{ |msg, time, addr, recvPort|
			var name = msg[1];

			// create responders for this new source

			this.addReceiverFunction(name);
			thisProcess.addOSCRecvFunc(receiverFunctions.at(name));

		}
	}


	// all synths functionality is provided here; the message is sended just after newSource
	mediaType { |name, type|
		if (type == \jack) {
			var newGroup;
			var newBus;

			var distanceSynth;
			var pannerSynth;

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

			// 2. create mono audio bus, add to the reference dictionary
			newBus = Bus.audio(server,1);
			internalBuses.add(name -> newBus);

			// 3. create distance synth, add to head of group
			distanceSynth = Synth(\distanceEncoder,[\externalIn,channel,\r,1,\busOut,newBus],target:newGroup,addAction:\addToHead);

			// *. initialize default ambisonics parameters
			// TODO: move this to a more elegant place??
			ambisonicsParameters.add(name -> [defaultAmbisonicsOrder,defaultAmbisonicsShape]);

			// 4, create encoding synth, add to tail group
			switch(spatialTechnique)
			{\ambisonics} {
				var synthName = this.getAmbisonicsSynthName(name);
				pannerSynth = Synth(synthName,[\busIn,newBus],target:newGroup,addAction:\addToTail);
			}

			{\vbap} {
				pannerSynth = Synth(synthName,[\busIn,newBus,\numChannels,vbapNumSpeakers,\speakerBuffer,vbapSpeakerBuffer],target:newGroup,addAction:\addToTail)
			}

			{\binaural} {
				pannerSynth = Synth(synthName,[\busIn,newBus,\subjectID,binauralSubjectID],target:newGroup,addAction:\addToTail);
			}
			;

			encodersReferences.add(name -> [distanceSynth, pannerSynth]);


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

		if (spatialTechnique==\binaural) { // convert to rad
			azimuth = azimuth.degree2rad.neg;
			elevation = elevation.degree2rad;
		};

		// send parameters to all nodes inside their group
		encoders.at(name).set(\r,r);
		encoders.at(name).set(\azi,azimuth);
		encoders.at(name).set(\ele,elevation);
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

	// TODO: add spatDIF compatibility
	setSpatializationTechnique { |newSpatialTechnique|
		spatialTechnique = newSpatialTechnique;

	}


	getAmbisonicsSynthName { |source|
		var order = ambisonicsParameters.at(source).at(0);
		var shape = ambisonicsParameters.at(source).at(1);

		var name = \ambiEncoder;
		var extName;

		extName = switch (shape)
		{\ring} {\Ring}
		{\extended} {\Ext}
		{\meridian} {\Mer}
		{\point} {""};

		^(name ++ order ++ extName).asSymbol;
	}


	// TODO: maybe check if parameters are not changed?

	setAmbisonicsParameters { |source, order = 3, shape = \point|

		// only apply if current technique is ambisonics
		if (spatialTechnique == \ambisonics) {

			// apply to all sources
			if (source.isNil) {
				encodersReferences.keys.do{ |key|
					var pannerSynth;
					var synthName;

					// set new ambisonics parameters
					ambisonicsParameters.at(key).put(0,order);
					ambisonicsParameters.at(key).put(0,shape);
					synthName = this.getAmbisonicsSynthName(key);

					// free current synth
					pannerSynth = encodersReferences[key][1];
					pannerSynth.free;

					// create new synth
					pannerSynth = Synth(synthName,[\busIn,internalBuses.at(key)],target:encoders.at(key),addAction:\addToTail);

					// save reference in the same place
					encodersReferences.at(key).put(1,pannerSynth);
				}
			} {
			// apply to a specific source
				var pannerSynth;
				var synthName;

				// set new ambisonics parameters
				ambisonicsParameters.at(source).put(0,order);
				ambisonicsParameters.at(source).put(1,shape);
				synthName = this.getAmbisonicsSynthName(source);

				// free current synth
				pannerSynth = encodersReferences[source][1];
				pannerSynth.free;

				// create new synth
				pannerSynth = Synth(synthName,[\busIn,internalBuses.at(source)],target:encoders.at(source),addAction:\addToTail);

				// save reference in the same place
				encodersReferences.at(source).put(1,pannerSynth);

			}
		} {
			"Ambisonics is not the current spatialization technique".warn;
		}
	}

	getBinauralParameters {


	}


	// TODO: add spatDIF compatibility
	setBinauralDecoder { |decoderType, subjectID|

		// only apply if current spatial technique is binaural
		if (spatialTechnique == \binaural) {

			// load new data
			binauralSubjectID = subjectID;
			binauralDecoder  = switch(decoderType)
			{\spherical} {FoaDecoderKernel.newSpherical(binauralSubjectID,server)}
			{\listen} {FoaDecoderKernel.newListen(binauralSubjectID,server)}
			{\cipic} {FoaDecoderKernel.newCIPIC(binauralSubjectID,server)}
			;

			// update synthdef
			this.initBinauralSynthDef;

			// reload synths
			encodersReferences.keys.do{ |key|
				var pannerSynth = encodersReferences[key][1];

				// free current synth
				pannerSynth.free;

				// create new synth
				pannerSynth = Synth(\binauralEncoder,[\busIn,internalBuses.at(key),\subjectID,binauralSubjectID],target:encoders.at(key),addAction:\addToTail);

				// save reference in the same place
				encodersReferences.at(key).put(1,pannerSynth);

			}
		} {
			"Binaural is not the current spatialization technique".warn;
		}

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
