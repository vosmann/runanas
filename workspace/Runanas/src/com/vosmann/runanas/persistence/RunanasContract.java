package com.vosmann.runanas.persistence;

import android.provider.BaseColumns;

public abstract class RunanasContract {
	private RunanasContract() {
	}
		
	public static abstract class RunPoint implements BaseColumns {
		public static final String TABLE_NAME = "run_point";
		public static final String COLUMN_NAME_RUN_ID = "run_id";
		public static final String COLUMN_NAME_ACCURACY = "accuracy"; // float
		public static final String COLUMN_NAME_ALTITUDE = "altitude"; // double
		public static final String COLUMN_NAME_BEARING = "bearing"; // float
		public static final String COLUMN_NAME_LATITUDE = "latitude"; // double
		public static final String COLUMN_NAME_LONGITUDE = "longitude"; // double
		public static final String COLUMN_NAME_SPEED = "speed"; // float
		public static final String COLUMN_NAME_TIME = "time"; // long
	}
	
	public static abstract class RunResult implements BaseColumns {
		public static final String TABLE_NAME = "run_result ";
		public static final String COLUMN_NAME_RUN_ID = "run_id";
		public static final String COLUMN_NAME_TIME = "time"; // long
		public static final String COLUMN_NAME_DISTANCE = "distance"; // double
		public static final String COLUMN_NAME_DURATION = "duration"; // long
		public static final String COLUMN_NAME_AVG_SPEED = "avg_speed"; // double
		public static final String COLUMN_NAME_MAX_SPEED = "max_speed"; // double
		public static final String COLUMN_NAME_MIN_SPEED  = "min_speed"; // double
		public static final String COLUMN_NAME_MASS = "mass"; // double
		public static final String COLUMN_NAME_ENERGY = "energy"; // double
		public static final String COLUMN_NAME_ABRUPT_END = "abrupt_end"; // boolean
	}
}
