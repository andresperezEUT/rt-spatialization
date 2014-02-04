// // // // // // // // // // // // // // // // // // // // // // //
//
// generates a line in the AmbEnc parameters value
// plays a DC for getting the static coefficients
// saves line and output in a textfile, for further processing
//
// // // // // // // // // // // // // // // // // // // // // // //

// here plays the synth
(
{
	~kr=Server.local.options.blockSize;
	~len_kr=floor(44100/~kr);
	~seconds=1;
	~lineBuffer = Buffer.alloc(s, ~seconds*~len_kr, 1);
	~ambiBuffer = Buffer.alloc(s, ~seconds*~len_kr, 16); // ambisonics order 3

	// sound synthesis

	// // // // // // // // // // // // // // // // // // // // // // //
	~az=0;
	// ~az=Line.kr(0,2*pi,~seconds,doneAction:2);
	// ~el=0;
	~el=Line.kr((-pi),pi,~seconds,doneAction:2);
	// // // // // // // // // // // // // // // // // // // // // // //

	~sig=AmbEnc3.ar(DC.ar(1),~az,~el);
	// save data
	RecordBuf.kr(~el,~lineBuffer,loop:0);
	RecordBuf.kr(~sig,~ambiBuffer,loop:0);

	Out.ar(0,~sig);
}.play
)

// here saves values from scsynth into a file


(
// get current path
var currentDir=PathName(thisProcess.nowExecutingPath).pathOnly;
// create a new file

// // // // // // // // // // // // // // // // // // // // // // //
// f = File(currentDir+/+"ambi_azi.txt", "w");
f = File(currentDir+/+"ambi_ele.txt", "w");
// // // // // // // // // // // // // // // // // // // // // // //

// load in sclang as a floatArray --> very unoptimized way to do it, since it writes into /tmp to load again to further write into disk!!!!
~lineBuffer.loadToFloatArray( action:{|array| ~a=array });
~ambiBuffer.loadToFloatArray( action:{|array| ~b=array });

~n=~ambiBuffer.numChannels;
~lineBuffer.numFrames.do { |i|
	//write mono values
	f.write(~a[i].asString);
	f.write(",");
	//write multichannel values
	~n.do { |n|
		f.write(~b[i*~n+n].asString);
		if (n<(~n-1)) {
			// not write in last column
			f.write(",")
		}
	};
	f.write("\n");
};
// don't forget
f.close
)