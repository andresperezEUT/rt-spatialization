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
// It provides also a zoom slider
//
// TODO:
// -> support arbitrary coordinate systems ordering
// -> adjust pixels to meters (through draw.scale?)
//
////////////////////////////////////////////////////////////////////////////


SSWindow : RedQWindow {

	var <>dim; //dimensions of the world which is represented, as a SSVector
	var widthSlider=20;
	var zoomSlider;
	var <>zoom = 55;
	var <>minZoom=10;
	var <>maxZoom=100;

	*new {|name= "redQWindow", bounds, resizable= false, border= true, server, scroll= false, dimVector|
		^super.new.initSSWindow(name, bounds, resizable, border, scroll,dimVector);
	}

	initSSWindow { |argName, argBounds, resize, border, scroll, dimVector|

		// this is copied from initRedQWindow...
		argBounds= argBounds ?? {Rect(128, 64, 300, 300)};
		if(scroll, {"RedQWindow: can't scroll".warn});
		view= QTopView(this, argName.asString, argBounds.moveTo(0, 0), resize, border);
		resizable= resize == true;
		QWindow.addWindow(this);
		view.connectFunction('destroyed()', {QWindow.removeWindow(this)}, false);

		this.background= Color.black;
		mouse= RedVector2D[0, 0];
		userView= UserView(this, Rect(widthSlider/2, 0, argBounds.width-(widthSlider/2), argBounds.height))
		.mouseDownAction_{|view, x, y| mouse= RedVector2D[x, y]}
		.mouseMoveAction_{|view, x, y| mouse= RedVector2D[x, y]};
		QWindow.initAction.value(this);
		//...until here

		zoomSlider=Slider(this, Rect(0, 0, widthSlider, argBounds.height)).value_(0.5);
		zoomSlider.action = {zoom=zoomSlider.value.linlin(0,1,minZoom,maxZoom)};


		dim = dimVector ? SSVector[100,100,100];

	}


	draw {|func| userView.drawFunc= {
		// a = Slider(w, Rect(20, 60, 150, 20));
		Pen.translate(this.bounds.width/2,this.bounds.height/2);
		Pen.scale(zoom,zoom);
		func.value();
		Pen.translate(0,0);
		Pen.scale(1/zoom,1/zoom);
		}
	}
}