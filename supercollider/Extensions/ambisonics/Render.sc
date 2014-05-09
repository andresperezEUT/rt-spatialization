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
// Render.sc
//
// This class implements a spatial render
// For the moment, only Ambisonics rendering is supported
//
// Render instance receives OSC messages from an external spatialization software
// and applies spatial coefficients to the existing input channels
//
// Each different source/channel is tracked inside <>encoders dictionary
//
// Distance cues are supported by means of distance attenuation and air absorption simulation
//
// TODO:
// -> support several sound shapes
// -> support room/environmental parameters
// -> standardize OSC messages (spatdif?)
// -> support vbap panning
//
////////////////////////////////////////////////////////////////////////////



Render {

	var <port;
	var <sourceAddress;
	var <>encoders;

	var newSource;
	var endSource;
	var posSource;

	var <>verbose=true;

	*new{ |myPort|
		^super.new.init(myPort);
	}

	init { |myPort=57120|
		//init OSC functions
		port=myPort;
		sourceAddress=NetAddr("127.0.0.1", port); //change to allow any (external) ip

		//init Synths reference
		encoders=Dictionary.new;


		// // // // RECEIVER FUNCTIONS // // // //

		newSource = { |msg, time, addr, recvPort|
			var channel=msg[1];
			var r=msg[2];
			var azimuth=msg[3];
			var elevation=msg[4];

			if (verbose) {
				"NEW SOURCE---".postln;
				("channel: "++channel.asString).postln;
				("r: "++r.asString).postln;
				("azimuth: "++azimuth.asString).postln;
				("elevation: "++elevation.asString).postln;
			};


			//TODO: check if source already exists
			encoders.add(channel -> Synth(\ambiEncoder,args:[\busIn,channel,\r,r,\azi,azimuth,\ele,elevation],addAction:'addToTail'))
		};

		endSource = { |msg, time, addr, recvPort|
			var channel=msg[1];
			"END SOURCE---".postln;
			("channel: "++channel.asString).postln;
			//first free the synth
			//TODO!!! avoid click
			encoders.at(channel).free;
			//then remove the association
			encoders.removeAt(channel);
		};

		posSource = { |msg, time, addr, recvPort|
			var channel=msg[1];
			var r=msg[2];
			var azimuth=msg[3];
			var elevation=msg[4];

			if (verbose) {
				("channel: "++channel.asString).postln;
				("r: "++r.asString).postln;
				("azimuth: "++azimuth.asString).postln;
				("elevation: "++elevation.asString).postln;
			};
			// do the movement
			//TODO: only move if position change is bigger than JND...
			// !! already implemented in SSWorld

			encoders.at(channel).set(\r,r);
			encoders.at(channel).set(\azi,azimuth);
			encoders.at(channel).set(\ele,elevation);

		};


		OSCdef(\new, newSource, '/new', nil); /* nil allows for any port, ip app using it*/
		OSCdef(\pos, posSource, '/pos', nil); /*change nil for sourceAddress, just in case*/
		OSCdef(\end, endSource, '/end', nil);




		//init SynthDef

		//TODO: ADD DELAY!
		//TODO: TRY OUT DIFFERENT INVERSE R LAWS
		//TODO: IMPLEMENT EARLY REFLECTIONS

		SynthDef(\ambiEncoder,{ |busIn=0,r=1,azi=0,ele=0|
			var sig, enc, out;
			var filterfreq, filteramp;

			// get signal from AudioIn
			sig = SoundIn.ar(busIn,1);

			// apply distance absortion colour effect
			filterfreq = 15849 + (r * (−785.71 + (r * (18.919 - (0.1668 * r)))));
			filterfreq = filterfreq.clip(0,20000); // freq above 24kHz destroy your ears!!
			// filterfreq.poll;
			// inverse square-law attenuation
			// filteramp = 1 / (r**2);
			filteramp = 1 / r;

			filteramp=filteramp.clip(0,1); // don't allow <1 amp values

			// filteramp.poll;

			sig = LPF.ar(sig,filterfreq,filteramp); // 2nd order butterworth lpf

			// clip the signal to avoid shits
			sig = Clip.ar(sig,0,1);

			// ambisonics encoding
			enc = AmbEnc3.ar(sig,azi,ele);

			// output signal
			out = Out.ar(0,enc);
		}).add;



	}

}