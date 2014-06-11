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
Select
(
SynthDef(\r,{|e=0|
	var sig = SinOsc.ar;
	var amb = AmbREnc3.ar(sig,MouseY.kr(0,pi/2));
	Out.ar(0,amb)
}).add;
)

(
SynthDef(\x,{ |a=0,da=0.001,e=0,de=0.001,preserveArea=0|
	var sig = SinOsc.ar(440);

	var amb = AmbXEnc3.ar(sig,a,da,e,de,preserveArea);
	// var amb = AmbXEnc3.ar(sig,0,0,0,0);
	//Out.ar(0,amb);
}).add;
)
AmbXEnc3.delta_(0.000000000001)
pi/2/(cos(pi/2))/(2pi)*0.001
(
x=Synth(\x);
x.set(\de,0)
x.set(\da,pi);
x.set(\e,pi/2)
x.set(\preserveArea,0)
)
x.set(\e,pi/2)
/////////////
e+de/2 <= pi/2, e-de/2 >= -pi/2

(
~limitE= { |e,de|
	e = min(e,(pi/2)-(de/2));
	e = max(e,(-pi/2)+(de/2));

	e;
}
)
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
SynthDef(\n,{|a=0,e=0|
	var sig = SinOsc.ar(440);
	var amb = AmbEnc3.ar(sig,a,e);
	Out.ar(0,amb);
}).add;
)
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