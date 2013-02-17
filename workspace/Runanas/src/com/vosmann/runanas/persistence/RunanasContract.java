package com.vosmann.runanas.persistence;

import android.provider.BaseColumns;

public abstract class RunanasContract {
	private RunanasContract() {
	}
		
	public static abstract class RunMetrics implements BaseColumns {
		public static final String TABLE_NAME = "runmetrics ";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_DISTANCE = "distance";
		public static final String COLUMN_NAME_DURATION = "duration";
		public static final String COLUMN_NAME_AVG_SPEED = "avgspeed";
		public static final String COLUMN_NAME_MAX_SPEED = "maxspeed";
		public static final String COLUMN_NAME_MIN_SPEED  = "minspeed";
		public static final String COLUMN_NAME_MASS = "mass";
		public static final String COLUMN_NAME_ENERGY_EXPENDITURE =
				"energyexpenditure";
	}
	
	public static abstract class RunPoint implements BaseColumns {
		public static final String TABLE_NAME = "runpoint";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_ACCURACY = "accuracy"; // float
		public static final String COLUMN_NAME_ALTITUDE = "altitude"; // double
		public static final String COLUMN_NAME_BEARING = "bearing"; // float
		public static final String COLUMN_NAME_LATITUDE = "latitude"; // double
		public static final String COLUMN_NAME_LONGITUDE = "longitude"; // double
		public static final String COLUMN_NAME_SPEED = "speed"; // float
		public static final String COLUMN_NAME_TIME = "time"; // long
	}
}
