////////////////////////////////////////////////////////////////////////////
//
// Copyright ANDRÉS PÉREZ LÓPEZ, April 2014 [contact@andresperezlopez.com]
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; withot even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>
//
////////////////////////////////////////////////////////////////////////////
//
// plusSpherical_SS.sc
//
// Some convenience methods for the class Spherical from Joseph Anderson
//
////////////////////////////////////////////////////////////////////////////

+Spherical {

	// not to confuse phi and theta due to different nomenclatures!

	azimuth {
		^theta;
	}

	azimuth_ { |angle|
		theta=angle;
	}

	addAzimuth { |angle|
		this.azimuth_(this.azimuth+angle)
	}

	elevation {
		^phi;
	}

	elevation_ { |angle|
		phi=angle;
	}

	addElevation { |angle|
		this.elevation_(this.elevation+angle)
	}

}