package com.vosmann.runanas.persistence;

import java.util.List;

import com.vosmann.runanas.model.Run;
import com.vosmann.runanas.model.RunPoint;

public interface RunStorage {
	void storeRunPoint(RunPoint runPoint);
	List<Run> getRuns(); 
}
