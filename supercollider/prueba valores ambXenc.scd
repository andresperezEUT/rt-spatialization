// con este código se comprueba que la diferencia para fuente puntual entre AmbEnc y AmbXEnc es mínima
(
~limitE= { |e,de|
	e = min(e,(pi/2)-(de/2));
	e = max(e,(-pi/2)+(de/2));

	e;
}
)
(
11.do({ |e|
	var dAzi = 0.001;
	var dEle = 0.001;
	var ele = e/10*pi/2;
	var eleX = ~limitE.(ele,dEle);
	10.do({ |a|
		var azi = a/10*2*pi;
		var amb = AmbEnc3.kr(azi,ele);
		var ambX = AmbXEnc3.kr(azi,dAzi,eleX,dEle);
		[azi,ele].postln;
		(sum(abs(amb-ambX))).postln;
	})
});
"*********"
)

////////////////////////////////////////////////////
(
var d = 0.00001;
11.do({ |argE|
	var a = 0;
	var e = 0;
	var de = argE/10*pi + d;
	11.do({ |argA|
		var da = argA/10*2*pi + d;
		//[da,de].postln;
		AmbXEnc3.kr(a,da,e,de).postln;
	})
});
"*********"
)

AmbXEnc3.kr(0,0.001,0,0.0001)

)
(
~ele=pi/2;
~dEle=0.01;
~xele=~limitE.(~ele,~dEle);

// with limiting
~w=(AmbEnc3.kr(0,~ele)[3] -	AmbXEnc3.kr(0,0.01,~xele,~dEle)[3]);
abs(~w).postln;
""

/*
In the worst case (ele=pi/2), dEle set to 0.01 gives a difference of 0.011 with normal case
*/
)
A
///////////////////////////////////

/*e = limit_e (e, de)

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
return e*/


