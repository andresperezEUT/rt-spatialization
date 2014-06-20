+ SpatialRender {


	// // // // SYNTH DEFINITIONS // // // // // // // // // // // // // // // // // // // //

	//TODO: ADD DELAY!
	//TODO: TRY OUT DIFFERENT INVERSE R LAWS
	//TODO: IMPLEMENT EARLY REFLECTIONS

	initSynthDefs {

		SynthDef(\distanceEncoder,{ |externalIn=0,r=1,busOut|
			var sig, out;
			var filterfreq, filteramp;

			// get signal from AudioIn
			sig = SoundIn.ar(externalIn,1);

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

			// hardcore clip the signal to avoid shits
			sig = Clip.ar(sig,0,1);

			// out into intermediate bus
			Out.ar(busOut,sig)
		}).add;


		// ambisonics point source encoders

		SynthDef(\ambiEncoder1,{ |busIn,azi=0,ele=0|
			var sig = In.ar(busIn);
			var enc = AmbEnc1.ar(sig,azi,ele);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder2,{ |busIn,azi=0,ele=0|
			var sig = In.ar(busIn);
			var enc = AmbEnc2.ar(sig,azi,ele);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder3,{ |busIn,azi=0,ele=0|
			var sig = In.ar(busIn);
			var enc = AmbEnc3.ar(sig,azi,ele);
			Out.ar(0,enc);
		}).add;

		// ambisonics ring source encoderes

		SynthDef(\ambiEncoder1Ring,{ |busIn,ele=0|
			var sig = In.ar(busIn);
			var enc = AmbREnc1.ar(sig,ele);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder2Ring,{ |busIn,ele=0|
			var sig = In.ar(busIn);
			var enc = AmbREnc2.ar(sig,ele);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder3Ring,{ |busIn,ele=0|
			var sig = In.ar(busIn);
			var enc = AmbREnc3.ar(sig,ele);
			Out.ar(0,enc);
		}).add;

		// ambisonics extended source encoders

		SynthDef(\ambiEncoder1Ext,{ |busIn,azi=0,dAzi=0,ele=0,dEle=0,preserveArea=0|
			var sig = In.ar(busIn);
			var enc = AmbXEnc1.ar(sig,azi,dAzi,ele,dEle,preserveArea);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder2Ext,{|busIn,azi=0,dAzi=0,ele=0,dEle=0,preserveArea=0|
			var sig = In.ar(busIn);
			var enc = AmbXEnc2.ar(sig,azi,dAzi,ele,dEle,preserveArea);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder3Ext,{ |busIn,azi=0,dAzi=0,ele=0,dEle=0,preserveArea=0|
			var sig = In.ar(busIn);
			var enc = AmbXEnc3.ar(sig,azi,dAzi,ele,dEle,preserveArea);
			Out.ar(0,enc);
		}).add;

		// ambisonics semi-meridian source encoders

		SynthDef(\ambiEncoder1Mer,{ |busIn,azi=0|
			var sig = In.ar(busIn);
			var enc = AmbSMEnc1.ar(sig,azi);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder2Mer,{|busIn,azi=0|
			var sig = In.ar(busIn);
			var enc = AmbSMEnc2.ar(sig,azi);
			Out.ar(0,enc);
		}).add;

		SynthDef(\ambiEncoder3Mer,{ |busIn,azi=0|
			var sig = In.ar(busIn);
			var enc = AmbSMEnc3.ar(sig,azi);
			Out.ar(0,enc);
		}).add;

		// vbap encoder

		SynthDef(\vbapEncoder,{ |busIn,azi=0,ele=0|
			var sig = In.ar(busIn);
			var enc = VBAP.ar(vbapNumSpeakers,sig,vbapSpeakerBuffer.bufnum,azi,ele);
			Out.ar(0,enc);
		}).add;
	}
}