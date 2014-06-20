AmbSMrEnc {
	*ar { |in, azimuth=0, norm='N3D'|
		^ in * this.kr(azimuth,norm);
	}

	*kr { |azimuth|

	}
}

AmbSMrEnc1 : AmbSMrEnc{
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
		z = (2 * sqrt3) / pi;
		x = (2 * sqrt3 * cos_a) / pi;

		^[w, y, z, x];
	}
}

AmbSMrEnc2 : AmbSMrEnc{
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
		z = (2 * sqrt3) / pi;
		x = (2 * sqrt3 * cos_a) / pi;
		//
		v = (sqrt15 * cos_a * sin_a ) / 2;
		t = (sqrt15 * sin_a) / pi;
		r = sqrt(5) / 4;
		s = (sqrt15 * cos_a) / pi;
		u = (sqrt15 * cos(2*a)) / 4;

		^[w, y, z, x, v, t, r, s, u];
	}
}

AmbSMrEnc3 : AmbSMrEnc {
	classvar <order = 3;

	/* Class method: *kr
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
		var cos_2a = cos(2*a);
		//
		var sqrt70 = sqrt(70);
		var sqrt14_3 = sqrt(14/3);
		var sqrt35_3 = sqrt(35/3);

		w = 1;
		//
		y = (2 * sqrt3 * sin_a) / pi;
		z = (2 * sqrt3) / pi;
		x = (2 * sqrt3 * cos_a) / pi;
		//
		v = (sqrt15 * cos_a * sin_a ) / 2;
		t = (sqrt15 * sin_a) / pi;
		r = sqrt(5) / 4;
		s = (sqrt15 * cos_a) / pi;
		u = (sqrt15 * cos_2a) / 4;
		//
		q = (sqrt70 * sin(3*a)) / (3pi);
		o = (sqrt35_3 * sin(2*a)) / pi;
		m = (sqrt14_3 * sin_a) / pi;
		k = sqrt(7) / (3pi);
		l = (sqrt14_3 * cos_a) / pi;
		n = (sqrt35_3 * cos_2a) / pi;
		p = (sqrt70 * cos(3*a)) / (3pi);

		^[w, y, z, x, v, t, r, s, u, q, o, m, k, l, n, p];
	}
}