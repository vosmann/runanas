package com.vosmann.runanas.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RunanasDbHelper extends SQLiteOpenHelper {
	
	// If the database schema is changed, database version must be incremented.
    public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Runanas.db";
	
	// Helper constants.
//	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INTEGER"; // 1-8 bytes
	private static final String REAL_TYPE = " REAL";   // 8 bytes
	private static final String COMMA_SEP = ",";
	
	private static final String SQL_CREATE_RUN_POINT =
			"CREATE TABLE " + RunanasContract.RunPoint.TABLE_NAME + " ("
					+ RunanasContract.RunPoint._ID + " INTEGER PRIMARY KEY,"
					+ RunanasContract.RunPoint.COLUMN_NAME_RUN_ID
					+ INT_TYPE + COMMA_SEP
					+ RunanasContract.RunPoint.COLUMN_NAME_ACCURACY
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunPoint.COLUMN_NAME_ALTITUDE
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunPoint.COLUMN_NAME_BEARING
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunPoint.COLUMN_NAME_LATITUDE
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunPoint.COLUMN_NAME_LONGITUDE
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunPoint.COLUMN_NAME_SPEED
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunPoint.COLUMN_NAME_TIME
					+ INT_TYPE // + COMMA_SEP
					+ " )";
	private static final String SQL_CREATE_RUN_RESULT =
			"CREATE TABLE " + RunanasContract.RunResult.TABLE_NAME + " ("
					+ RunanasContract.RunResult._ID + " INTEGER PRIMARY KEY,"
					+ RunanasContract.RunResult.COLUMN_NAME_RUN_ID
					+ INT_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_TIME
					+ INT_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_DISTANCE
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_DURATION
					+ INT_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_AVG_SPEED
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_MAX_SPEED
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_MIN_SPEED
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_MASS
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunResult.COLUMN_NAME_ENERGY
					+ REAL_TYPE // + COMMA_SEP
					+ " )";	
	private static final String SQL_DROP_RUN_POINT =
	    "DROP TABLE IF EXISTS " + RunanasContract.RunPoint.TABLE_NAME;
	private static final String SQL_DROP_RUN_RESULT =
	    "DROP TABLE IF EXISTS " + RunanasContract.RunResult.TABLE_NAME;
	
	public RunanasDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    
	@Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RUN_POINT);
        db.execSQL(SQL_CREATE_RUN_RESULT);
    }
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Revise this upgrade policy.
        db.execSQL(SQL_DROP_RUN_POINT);
        db.execSQL(SQL_DROP_RUN_RESULT);
        onCreate(db);
    }
}
