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
// SSWindow.sc
// Based on RedUniverse quark (by redFrik)
//
// This class implements a Sound Scene Window, which is a special case of RedWindow
//
// Provides auto-translation of the coordinate system into:
//
//            +x
//             ^
//             |
//             |
//             |
//   +y <-------
//
// and places the world center point (0,0,0) into the middle of the window
//
// TODO:
// -> support arbitrary coordinate systems ordering
// -> adjust pixels to meters (through draw.scale?)
//
////////////////////////////////////////////////////////////////////////////


SSWindow : RedQWindow {

	var <>dim; //dimensions of the world which is represented, as a SSVector

	*new {|name= "redQWindow", bounds, resizable= false, border= true, server, scroll= false, dimVector|
		^super.new.initQWindow(name, bounds, resizable, border, scroll).initSSWindow(dimVector);
	}

	initSSWindow { |dimVector|
		dim = dimVector ? SSVector[100,100,100];
	}

	draw {|func| userView.drawFunc= {Pen.translate(dim@0/2,dim@1/2,dim@2/2);func.value();Pen.translate(dim@0,dim@1,dim@2)}}
}