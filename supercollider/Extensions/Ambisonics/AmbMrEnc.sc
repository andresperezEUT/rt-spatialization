AmbMrEnc {
	*ar { |in, azimuth=0, norm='N3D'|
		^ in * this.kr(azimuth,norm);
	}

	*kr { |azimuth|

	}
}

AmbMrEnc1 : AmbMrEnc{
	classvar <order = 1;

	*kr { |a, norm|
		// elevation, norm

		var w,x,y,z;

		w = 1;
		//
		y = 0;
		z = (2 * sqrt(3)) / pi;
		x = 0;

		^[w, y, z, x];
	}
}

AmbMrEnc2 : AmbMrEnc{
	classvar <order = 2;

	*kr { |a, norm|
		// elevation, norm

		var w,x,y,z,v,t,r,s,u;

		var sqrt15=sqrt(15);

		w = 1;
		//
		y = 0;
		z = (2 * sqrt(3)) / pi;
		x = 0;
		//
		v = (sqrt15 * cos(a) * sin(a)) / 2;
		t = 0;
		r = sqrt(5) / 4;
		s = 0;
		u = (sqrt15 * cos(2*a)) / 4;

		^[w, y, z, x, v, t, r, s, u];
	}
}

AmbMrEnc3 : AmbMrEnc {
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

		var sqrt15 = sqrt(15);
		var cos_2a = cos(2*a);
		//
		var sqrt35_3 = sqrt(35/3);


		w = 1;
		//
		y = 0;
		z = (2 * sqrt(3)) / pi;
		x = 0;
		//
		v = (sqrt15 * cos(a) * sin(a)) / 2;
		t = 0;
		r = sqrt(5) / 4;
		s = 0;
		u = (sqrt15 * cos_2a) / 4;
		//
		q = 0;
		o = (sqrt35_3 * sin(2*a) ) / pi;
		m = 0;
		k = sqrt(7) / (3pi);
		l = 0;
		n = (sqrt35_3 * cos_2a) / pi;
		p = 0;

		^[w, y, z, x, v, t, r, s, u, q, o, m, k, l, n, p];
	}
}