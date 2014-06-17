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
// SSVector.sc
// Based on RedUniverse quark (by redFrik)
//
// This class implements a Sound Scene Vector, which is a special case of 3D RedVector
//
////////////////////////////////////////////////////////////////////////////

SSVector[float] : RedVector3D {

	*clear{
		^SSVector[0,0,0];
	}

	asCartesian {
		^Cartesian(this@0,this@1,this@2);
	}

	asSpherical {
		^this.asCartesian.asSpherical;
	}



}