// TODO: generalize oscReceiver function creation
// ---> maybe tratar especialmente orientation como una función aparte? class method?
// TODO: remove all fucking vars

OrientationController {

	var orientationListener, <orientationAction;
	var <orientationLog, <orientationLogger, orientationLogTime;
	var <azimuth, <elevation, <roll;
	var <dAzi;

	var accelerationListener, <accelerationAction;
	var <accelerationLog, <accelerationLogger, accelerationLogTime;
	var <acceleration;

	var gravityListener, <gravityAction;
	var <gravityLog, <gravityLogger, gravityLogTime;
	var <gravity;

	var <logging = false;
	var <logTime;
	var <isPlaying = false;

	var orientationTask, accelerationTask, gravityTask;

	var <replayTask;
	var oscListener;
	var action;
	var <logger;


	*new {
		^super.new.initOC;
	}

	initOC {


		acceleration = Array.newClear(3); //[x,y,z] components of linear acceleration
		gravity = Array.newClear(3); //[x,y,z] components of linear acceleration

		orientationLog = false;
		orientationLogger = List.new;

		replayTask = Dictionary.new;
		oscListener = Dictionary.new;
		action = Dictionary.new;

		logger = Dictionary.new;

		//////////////////////////////////////////////////////////////////////////////
		// define listeners and start to perform
		//////////////////////////////////////////////////////////////////////////////

		///////////// TYPE_ORIENTATION /////////////
		oscListener.add(\orientation -> OSCdef(\orientationListener,{ |msg, time, addr, recvPort|

			if (azimuth.isNil) { //first message received
				dAzi=msg[1]; //get this azimuth as our reference 0
			};
			azimuth = msg[1];
			elevation = msg[2];
			roll = msg[3];


			//transform into SSWorld coordinate system
			azimuth = (azimuth - dAzi).neg.degree2rad.wrap(0,2pi);
			elevation = elevation.neg.degree2rad;
			roll = roll.neg.degree2rad;

			if (isPlaying.not) {
				//call user-defined function
				action[\orientation].value([azimuth,elevation,roll],time);
				//logger
				if (logging) {
					logger[\orientation].add([time-logTime,[azimuth,elevation,roll]]);
					//orientationLogger.add([azimuth,elevation,roll,time-orientationLogTime]);
				};
			};
			},"/orientationController/orientation")
		);


		///////////// TYPE_LINEAR_ACCELERATION /////////////
		oscListener.add(\acceleration -> OSCdef(\accelerationListener,{ |msg, time, addr, recvPort|

			acceleration[0] = msg[1];
			acceleration[1] = msg[2];
			acceleration[2] = msg[3];

			if (isPlaying.not) {
				//call user-defined function
				action[\acceleration].value(msg[(1..3)],time);
				//logger
				if (logging) {
					logger[\acceleration].add([time-logTime,msg[(1..3)]]);
				};
			};

			},"/orientationController/acceleration");
		);

		///////////// TYPE GRAVITY /////////////
		oscListener.add(\gravity -> OSCdef(\gravityListener,{ |msg, time, addr, recvPort|

			gravity[0] = msg[1];
			gravity[1] = msg[2];
			gravity[2] = msg[3];

			if (isPlaying.not) {
				//call user-defined function
				action[\gravity].value(msg[(1..3)],time);
				//logger
				if (logging) {
					logger[\gravity].add([time-logTime,msg[(1..3)]]);
				};
			};

			},"/orientationController/gravity")
		);

	}


	enableOscListeners {
		oscListener.do(_.enable);
	}

	disableOscListeners {
		oscListener.do(_.disable);
	}

	setAction { |key, aFunc|
		action.add(key -> aFunc);
	}

	//////////////////////////////////////////////////////////////////////////////
	// logger
	//////////////////////////////////////////////////////////////////////////////


	log_ { |state|
		logging = state;
		if (state) {
			this.initializeBuffers;
		}
	}

	initializeBuffers {
		logger.add(\orientation -> List.new);
		logger.add(\acceleration -> List.new);
		logger.add(\gravity -> List.new);
		logTime = Main.elapsedTime;
	}


	//////////////////////////////////////////////////////////////////////////////
	// replay
	//////////////////////////////////////////////////////////////////////////////

	replay { |repeat=inf|

		if (isPlaying.not) {
			isPlaying = true;

			logger.keys.do { |key| // if never logger, will do nothing

				if (action[key].isNil.not) {

					var times= [0,logger[key].flop[0]].flat;
					var deltas = (1..times.size-1).collect{|i| times[i] - times[i-1] };


					var task = Task({
						repeat.do({
							deltas.do({ |delta,i|
								delta.wait;
								action[key].value(logger[key][i][1]);
							})
						})
					});

					replayTask.add(key -> task);
				}
			};

			replayTask.do(_.play);
		} {
			"already playing".postln;
		}
	}

	pause {
		isPlaying = false;
		replayTask.do(_.pause);
		logging = false; // in order to later recover the data
	}

	stop {
		isPlaying = false;
		replayTask.do(_.pause);
		// if logging was true, ie it was recording, initialize again buffers for new data acquisition
		if (logging) {
			this.log_(true); // to create new data
		}
	}

	resume {
		isPlaying = true;
		replayTask.do(_.resume);
	}


}
