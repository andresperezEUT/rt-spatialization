// //////////////////////
//
// // BORRAR ESTE Y UTILIZAR EL DE SPATIALRENDER!!!!
//
//
//
// ////////////////////////////////////////////////////////////////////////////
// //
// // Copyright ANDRÉS PÉREZ LÓPEZ, May 2014 [contact@andresperezlopez.com]
// //
// // This program is free software: you can redistribute it and/or modify
// // it under the terms of the GNU General Public License as published by
// // the Free Software Foundation, either version 3 of the License, or
// // (at your option) any later version.
// //
// // This program is distributed in the hope that it will be useful,
// // but WITHOUT ANY WARRANTY; withot even the implied warranty of
// // MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// // GNU General Public License for more details.
// //
// // You should have received a copy of the GNU General Public License
// // along with this program. If not, see <http://www.gnu.org/licenses/>
// //
// ////////////////////////////////////////////////////////////////////////////
// //
// // Render.sc
// //
// // This class implements a spatial render
// //
// // Render instance receives OSC messages from an external spatialization software
// // and applies spatial coefficients to the existing input channels
// //
// // Each different source/channel is tracked inside <>encoders dictionary
// //
// // Distance cues are supported by means of distance attenuation and air absorption simulation
// //
// // TODO:
// // -> support room/environmental parameters: send maxDistance for the delay!!! <-------
// //       create a room model inside the render
// // -> standardize OSC messages (spatdif?)
// //
// // ---> change channel reference to name reference!!!
// //
// ////////////////////////////////////////////////////////////////////////////
//
// Render {
//
// 	var <server;
// 	var <port;
// 	var <sourceAddress;
//
// 	var <>encoders;
// 	var shapeEncoders;
// 	var <spatialTechnique;
// 	var <synthName;
// 	var <vbapSpeakerConf,<vbapSpeakerBuffer,<vbapNumSpeakers;
//
// 	var <>verbose=true;
//
// 	*new{ |server, port=57120, spatialTechnique=\ambisonics, ambiOrder=3, vbapSpeakerConf|
// 		^super.new.init(server,port,spatialTechnique,ambiOrder,vbapSpeakerConf);
// 	}
//
// 	init { |myServer,myPort,myTechnique,ambiOrder,myVbapSpeakerConf|
//
// 		// // // // INIT FUNCTIONALITY // // // //
// 		server = myServer;
//
// 		//init OSC parameters
// 		port=myPort;
// 		sourceAddress=NetAddr("127.0.0.1", port); //change to allow any (external) ip
//
// 		//init Synths reference
// 		spatialTechnique=myTechnique;
//
// 		//get synths names
// 		switch(spatialTechnique)
// 		{\ambisonics} {
// 			synthName = switch(ambiOrder)
// 			{1} {\ambiEncoder1}
// 			{2} {\ambiEncoder2}
// 			{3} {\ambiEncoder3}
// 		}
// 		{\vbap} {synthName = \vbapEncoder};
//
// 		//init vbap synthdef
// 		if (myVbapSpeakerConf.isNil.not) {
//
// 			vbapSpeakerConf=myVbapSpeakerConf;
// 			vbapSpeakerBuffer=vbapSpeakerConf.loadToBuffer;
// 			vbapNumSpeakers=vbapSpeakerConf.numSpeakers;
//
// 		} {
// 			// if no vbap is used, just give some values for the synthdef not to crash
// 			vbapSpeakerBuffer=Buffer.new;
// 			vbapNumSpeakers=1;
//
// 		};
//
//
// 		encoders=Dictionary.new;
// 		shapeEncoders=Dictionary.new;
//
//
// 		// // // // RECEIVER FUNCTIONS // // // //
//
// 		OSCdef(\new, this.newSource, '/new', nil); /* nil allows for any port, ip app using it*/
// 		OSCdef(\pos, this.posSource, '/pos', nil); /*change nil for sourceAddress, just in case*/
// 		OSCdef(\typ, this.typSource, '/typ', nil); // source type (point, extended, ring...)
// 		OSCdef(\wid, this.widSource, '/wid', nil); // source width (for extended sources)
// 		OSCdef(\pre, this.preSource, '/pre', nil); // preserve area (for extended sources)
// 		OSCdef(\end, this.endSource, '/end', nil);
//
// 		// // // // INIT SYNTHDEFS // // // //
//
// 		this.initSynthDefs;
//
// 	}
//
//
// 	// // // // OSC FUNCTIONS // // // // // // // // // // // // // // // // // // // //
//
// 	newSource {
// 		^{ |msg, time, addr, recvPort|
// 			var name = msg[1];
// 			var channel = msg[2];
//
// 			var newGroup;
// 			var newBus;
//
// 			var newShapeEncodersDict;
//
// 			if (verbose) {
// 				"NEW SOURCE---".postln;
// 				("name: "++name.asString).postln;
// 				("channel: "++channel.asString).postln;
// 			};
//
// 			// general schema
// 			//
// 			//                 ................................newGroup................................
// 			//                |                                                                        |
// 			//                |                                                                        |
// 			//----(In.ar)---> [distance] ----(newBus)---> [ambiEncoder, ambiEncoderRing, ambiEncoderExt] -/-/-(Out.ar)-/-/->
// 			//                                       \--> [vbapEncoder] -/-/-(Out.ar)-/-/->
//
// 			// actions
// 			// 1. create group
// 			// 2. create mono audio bus
// 			// 3. create distance synth, add to head of group
// 			// 4, create encoding synth(s), add to tail group
//
// 			// 1. create group
// 			newGroup = Group.new;
// 			encoders.add(name -> newGroup);
//
// 			newShapeEncodersDict = Dictionary.new;
// 			shapeEncoders.add(name -> newShapeEncodersDict);
//
// 			// 2. create mono audio bus
// 			newBus = Bus.audio(server,1);
//
// 			// 3. create distance synth, add to head of group
// 			Synth(\distanceEncoder,[\busIn,channel,\r,1,\busOut,newBus],target:newGroup,addAction:\addToHead);
//
// 			// 4, create encoding synth, add to tail group
// 			switch(spatialTechnique)
// 			{\ambisonics} {
// 				// initialize with point source encoder, create paused other synths
// 				var point = Synth(synthName,[\busIn,newBus,\azi,azimuth,\ele,elevation],target:newGroup,addAction:\addToTail);
// 				var ring = Synth.newPaused(synthName++\Ring,[\busIn,newBus,\ele,elevation],target:newGroup,addAction:\addToTail);
// 				var ext = Synth.newPaused(synthName++\Ext,[\busIn,newBus,\azi,azimuth,\ele,elevation],target:newGroup,addAction:\addToTail);
// 				// add them to the shapeEncoders dictionary
// 				newShapeEncodersDict.add(\point -> point);
// 				newShapeEncodersDict.add(\ring -> ring);
// 				newShapeEncodersDict.add(\ext -> ext);
// 			}
// 			{\vbap} {
// 				Synth(synthName,[\busIn,newBus,\azi,azimuth,\ele,elevation,\numChannels,vbapNumSpeakers,\speakerBuffer,vbapSpeakerBuffer],target:newGroup,addAction:\addToTail)
// 			};
//
//
// 			//TODO: check if source already exists
//
// 			/*			encoders.add(channel -> Synth(\ambiEncoder,args:[\busIn,channel,\r,r,\azi,azimuth,\ele,elevation],addAction:'addToTail'))*/
// 		}
// 	}
//
// 	posSource {
// 		^{ |msg, time, addr, recvPort|
// 			var name=msg[1];
// 			var r=msg[2];
// 			var azimuth=msg[3];
// 			var elevation=msg[4];
//
// 			if (verbose) {
// 				"*********".postln;
// 				("name: "++channel).postln;
// 				("r: "++r).postln;
// 				("azimuth: "++azimuth).postln;
// 				("elevation: "++elevation).postln;
// 			};
//
// 			// TODO: vbap parameters should be in degrees!!!
//
// 			// send parameters to all nodes inside their group
// 			encoders.at(name).set(\r,r);
// 			encoders.at(name).set(\azi,azimuth);
// 			encoders.at(name).set(\ele,elevation);
// 		}
// 	}
//
// 	widSource {
// 		// change source width
// 		^{ |msg, time, addr, recvPort|
// 			var name=msg[1];
// 			var da=msg[2];
// 			var de=msg[3];
//
// 			if (verbose) {
// 				"*********".postln;
// 				("name: "++channel).postln;
// 				("da: "++da).postln;
// 				("de: "++de).postln;
// 			};
//
// 			// send parameters to all nodes inside their group
// 			encoders.at(name).set(\dAzi,da);
// 			encoders.at(name).set(\dEle,de);
// 		}
// 	}
//
// 	preSource {
// 		// change source width
// 		^{ |msg, time, addr, recvPort|
// 			var name = msg[1];
// 			var preserveArea = msg[2];
//
// 			if (verbose) {
// 				"*********".postln;
// 				("name: "++channel).postln;
// 				("preserve area: "++preserveArea).postln;
// 			};
//
// 			// send parameters to all nodes inside their group
// 			encoders.at(name).set(\preserveArea,preserveArea);
// 		}
// 	}
//
// 	// if source channel does not exist, will throw error, since nil.at() is not defined
// 	// is not the case for the other functions
// 	// TODO: implement channel/name checker
// 	typSource {
// 		// change source type
// 		^{ |msg, time, addr, recvPort|
// 			var name=msg[1];
// 			var type=msg[2];
//
// 			var ambiEncoders;
//
// 			if (verbose) {
// 				"*********".postln;
// 				("name: "++channel).postln;
// 				("type: "++type).postln;
// 			};
//
// 			// TODO: make this more elegant (ambi encoders subgroup, msg bundle, etc...)
//
// 			ambiEncoders=shapeEncoders.at(name); //this is a dictionary
// 			// activate corresponding synth
// 			switch(type)
// 			/*point*/{0} {ambiEncoders.at(\point).run(true); ambiEncoders.at(\ring).run(false); ambiEncoders.at(\ext).run(false)}
// 			/*ring*/ {1} {ambiEncoders.at(\point).run(false); ambiEncoders.at(\ring).run(true); ambiEncoders.at(\ext).run(false)}
// 			/*ext*/  {2} {ambiEncoders.at(\point).run(false); ambiEncoders.at(\ring).run(false); ambiEncoders.at(\ext).run(true)};
// 		}
// 	}
//
// 	endSource {
// 		^{ |msg, time, addr, recvPort|
// 			var name=msg[1];
//
// 			if (verbose) {
// 				"END SOURCE---".postln;
// 				("name: "++channel.asString).postln;
// 			};
//
// 			//TODO!!! avoid click
//
// 			// free all nodes inside the group
// 			encoders.at(name).free; //group.freeAll;
// 			//then remove the association
// 			encoders.removeAt(name);
// 			shapeEncoders.removeAt(name);
// 		}
// 	}
//
// 	// // // // SYNTH DEFINITIONS // // // // // // // // // // // // // // // // // // // //
//
// 	//TODO: ADD DELAY!
// 	//TODO: TRY OUT DIFFERENT INVERSE R LAWS
// 	//TODO: IMPLEMENT EARLY REFLECTIONS
//
// 	initSynthDefs {
//
// 		SynthDef(\distanceEncoder,{ |busIn=0,r=1,busOut|
// 			var sig, out;
// 			var filterfreq, filteramp;
//
// 			// get signal from AudioIn
// 			sig = SoundIn.ar(busIn,1);
//
// 			// apply distance absortion colour effect
// 			filterfreq = 15849 + (r * (−785.71 + (r * (18.919 - (0.1668 * r)))));
// 			filterfreq = filterfreq.clip(0,20000); // freq above 24kHz destroy your ears!!
// 			// filterfreq.poll;
// 			// inverse square-law attenuation
// 			// filteramp = 1 / (r**2);
// 			filteramp = 1 / r;
//
// 			filteramp=filteramp.clip(0,1); // don't allow <1 amp values
//
// 			// filteramp.poll;
//
// 			sig = LPF.ar(sig,filterfreq,filteramp); // 2nd order butterworth lpf
//
// 			// hardcore clip the signal to avoid shits
// 			sig = Clip.ar(sig,0,1);
//
// 			// out into intermediate bus
// 			Out.ar(busOut,sig)
// 		}).add;
//
//
// 		// ambisonics point source encoders
//
// 		SynthDef(\ambiEncoder1,{ |busIn,azi=0,ele=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbEnc1.ar(sig,azi,ele);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		SynthDef(\ambiEncoder2,{ |busIn,azi=0,ele=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbEnc2.ar(sig,azi,ele);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		SynthDef(\ambiEncoder3,{ |busIn,azi=0,ele=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbEnc3.ar(sig,azi,ele);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		// ambisonics ring source encoderes
//
// 		SynthDef(\ambiEncoder1Ring,{ |busIn,ele=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbREnc1.ar(sig,ele);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		SynthDef(\ambiEncoder2Ring,{ |busIn,ele=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbREnc2.ar(sig,ele);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		SynthDef(\ambiEncoder3Ring,{ |busIn,ele=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbREnc3.ar(sig,ele);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		// ambisonics extended source encoders
//
// 		SynthDef(\ambiEncoder1Ext,{ |busIn,azi=0,dAzi=0,ele=0,dEle=0,preserveArea=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbEnc1.ar(sig,azi,dAzi,ele,dEle,preserveArea);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		SynthDef(\ambiEncoder2Ext,{|busIn,azi=0,dAzi=0,ele=0,dEle=0,preserveArea=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbEnc2.ar(sig,azi,dAzi,ele,dEle,preserveArea);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		SynthDef(\ambiEncoder3Ext,{ |busIn,azi=0,dAzi=0,ele=0,dEle=0,preserveArea=0|
// 			var sig = In.ar(busIn);
// 			var enc = AmbEnc3.ar(sig,azi,dAzi,ele,dEle,preserveArea);
// 			Out.ar(0,enc);
// 		}).add;
//
// 		// vbap encoder
//
// 		SynthDef(\vbapEncoder,{ |busIn,azi=0,ele=0|
// 			var sig = In.ar(busIn);
// 			var enc = VBAP.ar(vbapNumSpeakers,sig,vbapSpeakerBuffer.bufnum,azi,ele);
// 			Out.ar(0,enc);
// 		}).add;
// 	}
//
//
//
// }