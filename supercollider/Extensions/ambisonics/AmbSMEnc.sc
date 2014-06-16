
AmbSMEnc {
	*ar { |in, azimuth=0, norm='N3D'|
		^ in * this.kr(azimuth,norm);
	}

	*kr { |azimuth|

	}
}

AmbSMEnc1 : AmbSMEnc{
	classvar <order = 1;

	*kr { |a, norm|
		// elevation, norm

		var w,x,y,z;

		var sqrt3 = sqrt(3);
		var sin_a = sin(a);
		var cos_a = cos(a);

		w = 1;
		//
		y = (2 * sqrt3 * sin_a) / pi;
		z = 0;
		x = (2 * sqrt3 * cos_a) / pi;

		^[w, y, z, x];
	}
}

AmbSMEnc2 : AmbSMEnc{
	classvar <order = 2;

	*kr { |a, norm|
		// elevation, norm

		var w,x,y,z,v,t,r,s,u;

		var sqrt3 = sqrt(3);
		var sin_a = sin(a);
		var cos_a = cos(a);
		//
		var sqrt15 = sqrt(15);

		w = 1;
		//
		y = (2 * sqrt3 * sin_a) / pi;
		z = 0;
		x = (2 * sqrt3 * cos_a) / pi;
		//
		v = (sqrt15 * cos_a * sin_a ) / 2;
		t = 0;
		r = sqrt(5) / 4;
		s = 0;
		u = (sqrt15 * cos(2*a)) / 4;

		^[w, y, z, x, v, t, r, s, u];
	}
}

AmbSMEnc3 : AmbSMEnc {
	classvar <order = 3;

	/* 	Class method: *kr
	Control rate method providing the Ambisonics coeffs
	Parameter
	azi: Azimuth angle of source
	elev: Elevation angle of source
	Return
	coeffs: Array[16] of Ambisonics coeffs
	*/

	*kr { |a, norm|
		// azimuth, norm

		var w,x,y,z,v,t,r,s,u,q,o,m,k,l,n,p;

		var sqrt3 = sqrt(3);
		var sin_a = sin(a);
		var cos_a = cos(a);
		//
		var sqrt15 = sqrt(15);
		//
		var sqrt70 = sqrt(70);
		var sqrt14_3 = sqrt(14/3);

		w = 1;
		//
		y = (2 * sqrt3 * sin_a) / pi;
		z = 0;
		x = (2 * sqrt3 * cos_a) / pi;
		//
		v = (sqrt15 * cos_a * sin_a ) / 2;
		t = 0;
		r = sqrt(5) / 4;
		s = 0;
		u = (sqrt15 * cos(2*a)) / 4;
		//
		q = (sqrt70 * sin(3*a)) / (3pi);
		o = 0;
		m = (sqrt14_3 * sin_a) / pi;
		k = 0;
		l = (sqrt14_3 * cos_a) / pi;
		n = 0;
		p = (sqrt70 * cos(3*a)) / (3pi);

		^[w, y, z, x, v, t, r, s, u, q, o, m, k, l, n, p];
	}
}