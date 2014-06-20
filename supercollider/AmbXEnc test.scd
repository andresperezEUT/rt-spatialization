SinOsc.ar(100).
(
~a=0;
~e=pi*2;
~da=pi;
~de=0.001;
[AmbEnc3.kr(~a,~e),AmbXEnc3.kr(~a,~da,~e,~de)].plot;
// sum(abs(AmbEnc3.kr(~a,~e)-AmbXEnc3.kr(~a,~d,~e,~d))).postln
)

AmbXEnc3.kr
a=min(1,2)
(
~a=0;
~e=0;
~da=pi;
~de=0.001;
[AmbREnc3.kr(~e),AmbXEnc3.kr(~a,~da,~e,~de)].plot;
// sum(abs(AmbEnc3.kr(~a,~e)-AmbXEnc3.kr(~a,~d,~e,~d))).postln
)


(
~a=0;
~e=0;
~da=0.001;
~de=0.001;
SynthDef(\r,{|e=0|
	// var sig = Saw.ar(200)*0.2;
	var sig = SoundIn.ar(0);
	//var sig = WhiteNoise.ar*0.1;
	var amb = AmbREnc3.ar(sig,e);
	Out.ar(0,amb)
}).add;

SynthDef(\x,{ |a=0,da=0.001,e=0,de=0.001|
	// var sig = Saw.ar(200)*0.2;
	var sig = SoundIn.ar(0);
	//var sig = WhiteNoise.ar*0.1;
	var amb = AmbXEnc3.ar(sig,a,da,e,de);
	// var amb = AmbXEnc3.ar(sig,0,0,0,0);
	Out.ar(0,amb);
}).add;

SynthDef(\n,{|amp=1,a=0,e=0|
	// var sig = Saw.ar(200)*0.2;
	var sig = SoundIn.ar(0);
	var amb = AmbEnc3.ar(sig,a,e).poll;
	Out.ar(0,amp*amb);
}).add;

SynthDef(\sm,{|amp=1,a=0|
	// var sig = Saw.ar(200)*0.2;
	var sig = SoundIn.ar(0);
	var amb = AmbSMEnc3.ar(sig,a).poll;
	Out.ar(0,amp*amb);
}).add;

SynthDef(\m,{|amp=1,a=0|
	// var sig = Saw.ar(200)*0.2;
	var sig = SoundIn.ar(0);
	var amb = AmbMEnc3.ar(sig,a).poll;
	Out.ar(0,amp*amb);
}).add;
)

////////////////////////


(
m=Synth(\m);
s=Synth(\sm,[\amp,0]);
n=Synth(\n,[\amp,0]);
n.set(\e,0);

~set={ |a|
	s.set(\a,a);
	m.set(\a,a);
	n.set(\a,a);
}
)


n.set(\e,pi/4)

// set
s.set(\a,pi/4)
s.set(\a,0)

~set.value(0)
~set.value(pi/2)


( // run normal
s.set(\amp,0);
m.set(\amp,0);
n.set(\amp,1);
)

( // run meridian
s.set(\amp,0);
m.set(\amp,1);
n.set(\amp,0);
)

( // run semi-meridian
s.set(\amp,1);
m.set(\amp,0);
n.set(\amp,0);
)
(
s.free;
n.free;
m.free;
)


//////////////////////////////
n=Synth(\n)
n.set(\a,0)
n.set(\e,0)
n.free

(
t=Task({
	inf.do({|i|
		~set.value((i*pi/20).neg);
		0.1.wait;
	})
}).play
)
t.stop

//////////////////////////////////////
r=Synth(\r)
r.set(\e,0)
r.set(\e,pi/2)
r.free
(
t=Task({
	inf.do({|i|
		r.set(\e,(i*pi/20).fold(0,pi/2).postln);
		0.6.wait;
	})
}).play
)
t.stop

//////////////////////////////////////
x=Synth(\x);
x.set(\a,0);
x.set(\da,2*pi)
x.set(\de,pi)
x.set(\e,0);
x.set(\de,0.001)
x.free
(
t=Task({
	inf.do({|i|
		x.set(\de,(i*pi/10+0.0001).fold(0,pi).postln);
		0.8.wait;
	})
}).play
)
t.stop


/////////////
e+de/2 <= pi/2, e-de/2 >= -pi/2

e=0
de=0.001
e+de/2 ~= 0
///////////////
e = limit_e (e, de)

if preserve_area :
        da = da / cos(e)
        if da > 2*pi :
                excess_area_factor = da / (2*pi)
                da = 2*pi
                de = de * excess_area_factor
                e = limit_e (e, de)



...

def limit_e (e, de)
        e = min(e, pi/2 - de/2)
        e = max(e, -pi/2 + de/2)
        return e
///////////////

(
n=Synth(\n);
n.set(\a,pi/4)
)
Mou
r=Synth(\r)

r.set(\e,pi/8)

PanAmbi

(
var a_2a = 1;
)


1.66666*3

5/3


2.pow(3)


cos(0)**2


sqrt(17.5)
sqrt(35/2)

sqrt(10.5)
sqrt(21/2)