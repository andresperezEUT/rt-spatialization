// plays 4 sound files in output channels 16..20
// they are connected in jack to inputs 0..3 in order to simulate external audio

(


~internal=16; //hardcoded in jackd

// connect supercollider outputs to ambdec inputs (ACN convention)
/*"jack_connect SuperCollider:out_1 ambdec:in.0w".systemCmd;
"jack_connect SuperCollider:out_2 ambdec:in.1y".systemCmd;
"jack_connect SuperCollider:out_3 ambdec:in.1z".systemCmd;
"jack_connect SuperCollider:out_4 ambdec:in.1x".systemCmd;

"jack_connect SuperCollider:out_5 ambdec:in.2v".systemCmd;
"jack_connect SuperCollider:out_6 ambdec:in.2t".systemCmd;
"jack_connect SuperCollider:out_7 ambdec:in.2r".systemCmd;
"jack_connect SuperCollider:out_8 ambdec:in.2s".systemCmd;

"jack_connect SuperCollider:out_9 ambdec:in.2u".systemCmd;
"jack_connect SuperCollider:out_10 ambdec:in.3q".systemCmd;
"jack_connect SuperCollider:out_11 ambdec:in.3o".systemCmd;
"jack_connect SuperCollider:out_12 ambdec:in.3m".systemCmd;

"jack_connect SuperCollider:out_13 ambdec:in.3k".systemCmd;
"jack_connect SuperCollider:out_14 ambdec:in.3l".systemCmd;
"jack_connect SuperCollider:out_15 ambdec:in.3n".systemCmd;
"jack_connect SuperCollider:out_16 ambdec:in.3p".systemCmd;*/


// connect extra outputs to first sc inputs
"jack_connect SuperCollider:out_17 SuperCollider:in_1".systemCmd;
"jack_connect SuperCollider:out_18 SuperCollider:in_2".systemCmd;
"jack_connect SuperCollider:out_19 SuperCollider:in_3".systemCmd;
"jack_connect SuperCollider:out_20 SuperCollider:in_4".systemCmd;
"jack_connect SuperCollider:out_21 SuperCollider:in_5,".systemCmd;


// buffers play to the internal buses

b = [Buffer.read(s,"/home/pans/Documentos/SuperCollider/SCLiss/sounds/drums.wav"),
	Buffer.read(s,"/home/pans/Documentos/SuperCollider/SCLiss/sounds/hh.wav"),
	Buffer.read(s,"/home/pans/Documentos/SuperCollider/SCLiss/sounds/bass.wav"),
	Buffer.read(s,"/home/pans/Documentos/SuperCollider/SCLiss/sounds/piano.wav"),
	Buffer.read(s,"/home/pans/Documentos/SuperCollider/SCLiss/sounds/melody.wav")];

// b = Buffer.read(s,"/home/andres/Music/vagabundo_barbudo/dub_n1.wav");


SynthDef(\PlayBuf, {| amp = 1, out = 0, bufnum = 0 |
    Out.ar(out,
		amp * PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum),loop:1, doneAction:2)
		// PinkNoise.ar
    )
}).add;

SynthDef(\saw, {| out = 0, bufnum = 0 |
	Out.ar(out,
		Saw.ar(1000)
    )
}).add;

// ~s1.free
{
	//~s1=Synth(\saw,[\out,~internal,\bufnum,b]);
	~s1=Synth(\PlayBuf,[\out,~internal,\bufnum,b[0]]);
/*	~s2=Synth(\PlayBuf,[\out,~internal+1,\bufnum,b[1]]);*/
	~s3=Synth(\PlayBuf,[\out,~internal+1,\bufnum,b[1]]);
	~s4=Synth(\PlayBuf,[\out,~internal+2,\bufnum,b[2]]);
	~s5=Synth(\PlayBuf,[\out,~internal+3,\bufnum,b[3]]);
}.defer(1)

// TODO: make this asynchronous, more elegant than defer..

)




