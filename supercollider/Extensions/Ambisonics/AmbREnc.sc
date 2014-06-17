
AmbREnc {
	*ar { |in, elev=0, norm='N3D'|
		^ in * this.kr(elev,norm);
	}

	*kr { |elev|

	}
}

AmbREnc1 : AmbREnc{
	classvar <order = 1;

	*kr { |e, norm|
		// elevation, norm

		var w,x,y,z;

		w = 1;
		//
		y = 0;
		z = sqrt(3) * sin(e);
		x = 0;

		^[w, y, z, x];
	}
}

AmbREnc2 : AmbREnc{
	classvar <order = 2;

	*kr { |e, norm|
		// elevation, norm

		var w,x,y,z,v,t,r,s,u;

		var sinE = sin(e);

		w = 1;
		//
		y = 0;
		z = sqrt(3) * sinE;
		x = 0;
		//
		v = 0;
		t = 0;
		r = (sqrt(5) * (-1 + (3 * sinE.pow(2)))) / 2;
		s = 0;
		u = 0;

		^[w, y, z, x, v, t, r, s, u];
	}
}

AmbREnc3 : AmbREnc {
	classvar <order = 3;

	/* 	Class method: *kr
	Control rate method providing the Ambisonics coeffs
	Parameter
	azi: Azimuth angle of source
	elev: Elevation angle of source
	Return
	coeffs: Array[16] of Ambisonics coeffs
	*/

	*kr { |e, norm|
		// elevation, norm

		var w,x,y,z,v,t,r,s,u,q,o,m,k,l,n,p;

		var sinE = sin(e);

		w = 1;
		//
		y = 0;
		z = sqrt(3) * sinE;
		x = 0;
		//
		v = 0;
		t = 0;
		r = (sqrt(5) * (-1 + (3 * sinE.pow(2)))) / 2;
		s = 0;
		u = 0;
		//
		q = 0;
		o = 0;
		m = 0;
		k = (sqrt(7) * (1 + (5 * cos(2*e))) * sinE).neg / 4;
		l = 0;
		n = 0;
		p = 0;

		^[w, y, z, x, v, t, r, s, u, q, o, m, k, l, n, p];
	}
}