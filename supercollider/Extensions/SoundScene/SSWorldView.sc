SSWorldView {

	var <world;
	var drawFunc;
	var <>textScale=10;
	var <>writeText=true;

	*new{ |ssWorld|
		^super.new.init(ssWorld);
	}

	init { |ssWorld|
		world=ssWorld;
		// drawFunc
	}


	draw { // return a function to be evaluated inside window.userView.drawFunc

		var z;
		var f=0.5; //focal point for perspective drawing

		^{

			/////////////////////////////////////////////////////////////////
			// draw static elements

			Pen.strokeColor=Color.black;
			Pen.alpha=1;


			// draw world bounds
			// Rect.aboutPoint(center,half x distance, half y distance)
			// remember that coordinates are changed respect to draw view

			z= (0.5).linlin(0, 1, f, 1); //0.5 because our floor is half-away from the "real" considered floor

			// draw sweet spot (on the floor)
			Pen.addArc(0@0,z*world.sweetSpotSize,0,2pi);
			Pen.stroke;
			//ceiling
			Pen.addRect(Rect.aboutPoint(0@0,world.dim.y/2,world.dim.x/2));
			//floor
			Pen.addRect(Rect.aboutPoint(0@0,z*world.dim.y/2,z*world.dim.x/2));
			//wall lines
			Pen.line(Point(world.dim.x/2,world.dim.y/2),Point(z*world.dim.x/2,z*world.dim.y/2));
			Pen.line(Point(world.dim.x/2.neg,world.dim.y/2),Point(z*world.dim.x/2.neg,z*world.dim.y/2));
			Pen.line(Point(world.dim.x/2,world.dim.y/2.neg),Point(z*world.dim.x/2,z*world.dim.y/2.neg));
			Pen.line(Point(world.dim.x/2.neg,world.dim.y/2.neg),Point(z*world.dim.x/2.neg,z*world.dim.y/2.neg));
			Pen.stroke;




			/////////////////////////////////////////////////////////////////
			// draw dynamic elements

			//TODO: not create a new instance of SSObject for each draw! make a visual object instead

			if (world.objects.size > 0 ) {
				world.objects.do { |obj|

					// mirror dimensions!!
					var x=(obj.loc.y).neg;
					var y=(obj.loc.x).neg;
					// 0 is the floor, and world.dim.z/2 is the ceiling
					var z=obj.loc.z.linlin(0,world.dim.z/2,world.dim.z/2,0); //invert min and max in the view

					var a,b;

					var newObj,newObj2;
					newObj=obj.copy.loc_(Cartesian(x,y,z)); //take care not to overwrite the actual object!!

					Pen.alpha=1;

					/////////////////////////////////////////////////////////////////
					// write names
					// we need extra (re)scaling because fond cannot be smaller than 1

					if (writeText==true) {

						newObj2=obj.copy.loc_(Cartesian(x*textScale,y*textScale,z*textScale)); //take care not to overwrite the actual object!!

						Pen.scale(1/textScale,1/textScale);
						Pen.stringAtPoint(newObj.name,Rect.aboutSSObject(newObj2,f:0.75).rightBottom,Font("Helvetica", 2.5),Color.red);
						Pen.scale(textScale,textScale);

					};


					/////////////////////////////////////////////////////////////////
					// draw objects and shadows

					switch (obj.shape)
					{\point} { //draw point
						Pen.strokeColor= Color.black;
						Pen.strokeOval(Rect.aboutSSObject(newObj));
						//draw shadow
						Pen.fillColor= Color.gray;
						Pen.alpha=0.5;
						//--place the shadow at the bottom
						Pen.fillOval(Rect.aboutSSObject(newObj.loc_(Cartesian(x,y,world.dim.z/2))));
					}
					// TODO: ADD OTHER SHAPES

					//default
					{
						Pen.fillColor= Color.black;
						Pen.fillOval(Rect.aboutSSObject(newObj));
						//draw shadow
						Pen.fillColor= Color.gray;
						Pen.alpha=0.5;
						//--place the shadow at the bottom
						Pen.fillOval(Rect.aboutSSObject(newObj.loc_(Cartesian(x,y,world.dim.z/2))));
					};

					/////////////////////////////////////////////////////////////////
					// draw line between the object and its shadow
					//TODO: change names!
					a=Rect.aboutSSObject(newObj.loc_(Cartesian(x,y,z))).center;
					b=Rect.aboutSSObject(newObj.loc_(Cartesian(x,y,world.dim.z/2))).center;
					Pen.line(a,b);
					Pen.alpha=0.1;
					Pen.stroke;
				}
			};
		};

	}
}