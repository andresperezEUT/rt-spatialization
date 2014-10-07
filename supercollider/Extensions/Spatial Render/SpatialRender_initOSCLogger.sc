+ SpatialRender {

	initOSCLogger { |myOscLoggerPath|

		// place here the desired spatdif meta info

		var fileName;
		var extensions;
		var author,host,date,session,location,annotation;

		fileName = "TimeFileLog" ++ "_" ++ Date.localtime.stamp ++ ".txt";

		extensions = ["jack","distance-cues"];

		// author =
		host = "SCLiss:SpatialRender";
		date = Date.getDate.format("%Y-%m-%d");
		// session =
		// location =
		// annotation =




		// TODO: osx compatibility
		oscLoggerPath = myOscLoggerPath ? "~/.local/share/SuperCollider/SpatialRender".standardizePath;

		("mkdir "++ oscLoggerPath).systemCmd; //just in case

		oscLogger = SpatDifLogger.new(fileName,oscLoggerPath,extensions,author,host,date,session,location,annotation);
		oscLogger.log_(logOSC);

		// create a routine that auto-saves the spatdif logger in a regular way
		autosaveTask = Task({
			inf.do({
				oscLogger.save;
				autosavePeriod.wait;
			})
		});
		if (autosaveOSC) {autosaveTask.start};
	}


}
