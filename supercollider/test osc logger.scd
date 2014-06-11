interpret

CtLoop
["a","s","d"].collect{}
Score
Date.localtime.postln

"/a/s/d/f".split($/)[10]
n = "name" ++ "_"++Date.localtime.stamp++".txt";
f=File(n,"w");
f.write("asdfasdf\n")
f.close


q=["a","asdfasdf", "asdf"].collect{|a|a.asCompileString}

q
OSC message recorder and player
(c) 2013 - Marije Baalman
GPL3 license

// this class needs the FileLog quark to function


// ------ recording

// to record, create a OSCFileLog
~timelog = SpatDifLogger.new.log_(false);
~timelog.log_(true);

// send some osc data to test:
n = NetAddr.new( "localhost", NetAddr.langPort );
3.do{
	n.sendMsg( "/spatdif/a/s/f", 0, 20.rand, 19.rand, "hello", 3, 4, 2.003);
}
(
b=OSCBundle.new;
b.add(["/spatdif/a/s/f", 1]);
b.add(["/spatdif/a/s/f", 2]);
b.add(["/spatdif/a/s/f", 3]);
)

// close the file again:
~timelog.close;


//------- playback:

// set up our target net address (here we just send to SC again
n = NetAddr.new( "localhost", NetAddr.langPort );

// we trace it to see if we get it, just to show that it works:
OSCFunc.trace( true );

// create a player
~oscplayer = OSCFileLogPlayer.new( "/home/nescivi/SuperCollider/test_130812_121049", n ); // arguments are the file/folder we previously recorded, and the target netaddress

// and play it
~oscplayer.play;

~oscplayer.pause;

~oscplayer.resume;

~oscplayer.stop;

~oscplayer.reset;

// play back faster:
~myClock = TempoClock.new( 10 );
~oscplayer.play( ~myClock );


// close the file again:
~oscplayer.close;