package com.vosmann.runanas.data;

import com.vosmann.runanas.model.Run;
import com.vosmann.runanas.model.RunPoint;

public interface RunStorage {
	Run getRun(); 
	void storeRun(Run run); 
	
	RunPoint getRunPoint();
	void storeRunPoint(RunPoint runPoint);
}
