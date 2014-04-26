SSWorldView {

	var <world;
	var drawFunc;
	var <>textScale=10;

	*new{ |ssWorld|
		^super.new.init(ssWorld);
	}

	init { |ssWorld|
		world=ssWorld;
		// drawFunc
	}


	draw { // return a function to be evaluated inside window.userView.drawFunc

		^{

			/////////////////////////////////////////////////////////////////
			// draw static elements

			Pen.strokeColor=Color.black;
			Pen.alpha=1;

			// draw sweet spot
			Pen.addArc(0@0,world.sweetSpotSize,0,2pi);
			Pen.stroke;

			// draw world bounds
			// Rect.aboutPoint(center,half x distance, half y distance)
			Pen.addRect(Rect.aboutPoint(0@0,world.dim[0]/4,world.dim[1]/4));
			Pen.stroke;




			/////////////////////////////////////////////////////////////////
			// draw dynamic elements

			//TODO: not create a new instance of SSObject for each draw! make a visual object instead

			if (world.objects.size > 0 ) {
				world.objects.do { |obj|

					// mirror dimensions!!
					var x=(obj.loc@1).neg;
					var y=(obj.loc@0).neg;
					var z=obj.loc@2;

					var a,b;

					var newObj,newObj2;
					newObj=obj.copy.loc_(SSVector[x,y,z]); //take care not to overwrite the actual object!!

					Pen.alpha=1;


					/////////////////////////////////////////////////////////////////
					// write names
					// we need extra (re)scaling because fond cannot be smaller than 1

					newObj2=obj.copy.loc_(SSVector[x*textScale,y*textScale,z*textScale]); //take care not to overwrite the actual object!!

					Pen.scale(1/textScale,1/textScale);
					Pen.stringAtPoint(newObj.name,Rect.aboutSSObject(newObj2,f:0.75).rightBottom,Font("Helvetica", 2.5),Color.red);
					Pen.scale(textScale,textScale);



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
						Pen.fillOval(Rect.aboutSSObject(newObj.loc_(SSVector[x,y,world.dim@2])));
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
						Pen.fillOval(Rect.aboutSSObject(newObj.loc_(SSVector[x,y,world.dim@2])));
					};

					/////////////////////////////////////////////////////////////////
					// draw line between the object and its shadow
					//TODO: change names!
					a=Rect.aboutSSObject(newObj.loc_(SSVector[x,y,z])).center;
					b=Rect.aboutSSObject(newObj.loc_(SSVector[x,y,world.dim@2])).center;
					Pen.line(a,b);
					Pen.alpha=0.1;
					Pen.stroke;
				}
			};
		};

	}
}