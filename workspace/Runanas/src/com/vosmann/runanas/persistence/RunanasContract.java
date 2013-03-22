package com.vosmann.runanas.persistence;

import android.provider.BaseColumns;

public abstract class RunanasContract {
	private RunanasContract() {
	}
		
	public static abstract class RunPoint implements BaseColumns {
		public static final String TABLE_NAME = "runpoint";
		public static final String COLUMN_NAME_RUN_ID = "runid";
		public static final String COLUMN_NAME_ACCURACY = "accuracy"; // float
		public static final String COLUMN_NAME_ALTITUDE = "altitude"; // double
		public static final String COLUMN_NAME_BEARING = "bearing"; // float
		public static final String COLUMN_NAME_LATITUDE = "latitude"; // double
		public static final String COLUMN_NAME_LONGITUDE = "longitude"; // double
		public static final String COLUMN_NAME_SPEED = "speed"; // float
		public static final String COLUMN_NAME_TIME = "time"; // long
	}
	
	public static abstract class RunResult implements BaseColumns {
		public static final String TABLE_NAME = "runresult ";
		public static final String COLUMN_NAME_RUN_ID = "runid";
		public static final String COLUMN_NAME_TIME = "time"; // long
		public static final String COLUMN_NAME_DISTANCE = "distance"; // double
		public static final String COLUMN_NAME_DURATION = "duration"; // long
		public static final String COLUMN_NAME_AVG_SPEED = "avgspeed"; // double
		public static final String COLUMN_NAME_MAX_SPEED = "maxspeed"; // double
		public static final String COLUMN_NAME_MIN_SPEED  = "minspeed"; // double
		public static final String COLUMN_NAME_MASS = "mass"; // double
		public static final String COLUMN_NAME_ENERGY = "energy"; // double
	}
}
