// plays 4 sound files in output channels 16..20
// they are connected in jack to inputs 0..3 in order to simulate external audio

// todo!!!!!!!!! cambiar timbre, rate!!!!!!!!!!!!

(


~internal=16; //hardcoded in jackd

// connect supercollider outputs to ambdec inputs (ACN convention)
"jack_connect SuperCollider:out_1 ambdec:in.0w".systemCmd;
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
"jack_connect SuperCollider:out_16 ambdec:in.3p".systemCmd;


// connect extra outputs to first sc inputs
"jack_connect SuperCollider:out_17 SuperCollider:in_1".systemCmd;
"jack_connect SuperCollider:out_18 SuperCollider:in_2".systemCmd;
"jack_connect SuperCollider:out_19 SuperCollider:in_3".systemCmd;
"jack_connect SuperCollider:out_20 SuperCollider:in_4".systemCmd;
"jack_connect SuperCollider:out_21 SuperCollider:in_5".systemCmd;
"jack_connect SuperCollider:out_22 SuperCollider:in_6".systemCmd;
"jack_connect SuperCollider:out_23 SuperCollider:in_7".systemCmd;
"jack_connect SuperCollider:out_24 SuperCollider:in_8".systemCmd;



// buffers play to the internal buses

b = [Buffer.read(s,"/home/andres/Music/fly.wav"),
	Buffer.read(s,"/home/andres/Music/bee.wav")];

// b = Buffer.read(s,"/home/andres/Music/vagabundo_barbudo/dub_n1.wav");


SynthDef(\PlayBufFly, {| amp = 1, out = 0, bufnum = 0, numFrames = 0,  startPos = 0, rate = 1|
	var pos = startPos*numFrames;
    Out.ar(out,
		amp * PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), rate: rate, loop:1, startPos:pos, doneAction:2)
    )
}).add;



// ~s1.free
{
	//~s1=Synth(\saw,[\out,~internal,\bufnum,b]);
	Synth(\PlayBufFly,[\out,~internal,\bufnum,b[0],\startPos,1.0.rand,\numFrames,b[0].numFrames,\rate,0.5.rand+0.75]);
	Synth(\PlayBufFly,[\out,~internal+1,\bufnum,b[0],\startPos,1.0.rand,\numFrames,b[0].numFrames,\rate,0.5.rand+0.75]);
	Synth(\PlayBufFly,[\out,~internal+2,\bufnum,b[0],\startPos,1.0.rand,\numFrames,b[0].numFrames,\rate,0.5.rand+0.75]);
	Synth(\PlayBufFly,[\out,~internal+3,\bufnum,b[0],\startPos,1.0.rand,\numFrames,b[0].numFrames,\rate,0.5.rand+0.75]);

	Synth(\PlayBufFly,[\out,~internal+4,\bufnum,b[1],\startPos,1.0.rand,\numFrames,b[1].numFrames,\rate,0.5.rand+0.75]);
	Synth(\PlayBufFly,[\out,~internal+5,\bufnum,b[1],\startPos,1.0.rand,\numFrames,b[1].numFrames,\rate,0.5.rand+0.75]);
	Synth(\PlayBufFly,[\out,~internal+6,\bufnum,b[1],\startPos,1.0.rand,\numFrames,b[1].numFrames,\rate,0.5.rand+0.75]);
	Synth(\PlayBufFly,[\out,~internal+7,\bufnum,b[1],\startPos,1.0.rand,\numFrames,b[1].numFrames,\rate,0.5.rand+0.75]);

}.defer(1)

// TODO: make this asynchronous, more elegant than defer..

)




