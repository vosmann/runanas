package com.vosmann.runanas.persistence;

import java.util.List;

import com.vosmann.runanas.model.Run;
import com.vosmann.runanas.model.RunPoint;
import com.vosmann.runanas.persistence.RunanasContract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RunStorageSqlLite extends SQLiteOpenHelper implements RunStorage {
	// If the database schema is changed, database version must be incremented.
    public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Runanas.db";
	
	// Helper constants.
//	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INTEGER"; // 1-8 bytes
	private static final String REAL_TYPE = " REAL";   // 8 bytes
	private static final String COMMA_SEP = ",";
	
	private static final String SQL_CREATE_RUN_METRICS =
			"CREATE TABLE " + RunanasContract.RunMetrics.TABLE_NAME + " ("
					+ RunanasContract.RunMetrics._ID + " INTEGER PRIMARY KEY,"
					+ RunanasContract.RunMetrics.COLUMN_NAME_ID
					+ INT_TYPE + COMMA_SEP
					+ RunanasContract.RunMetrics.COLUMN_NAME_DISTANCE
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunMetrics.COLUMN_NAME_DURATION
					+ INT_TYPE + COMMA_SEP
					+ RunanasContract.RunMetrics.COLUMN_NAME_AVG_SPEED
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunMetrics.COLUMN_NAME_MAX_SPEED
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunMetrics.COLUMN_NAME_MIN_SPEED
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunMetrics.COLUMN_NAME_MASS
					+ REAL_TYPE + COMMA_SEP
					+ RunanasContract.RunMetrics.COLUMN_NAME_ENERGY_EXPENDITURE
					+ REAL_TYPE + COMMA_SEP
					+ " )";
	private static final String SQL_CREATE_RUN_POINT =
			"CREATE TABLE " + RunanasContract.RunPoint.TABLE_NAME + " ("
					+ RunanasContract.RunPoint._ID + " INTEGER PRIMARY KEY,"
					+ RunanasContract.RunPoint.COLUMN_NAME_ID
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
					+ INT_TYPE + COMMA_SEP
					+ " )";
	private static final String SQL_DROP_RUN_METRICS =
	    "DROP TABLE IF EXISTS " + RunanasContract.RunMetrics.TABLE_NAME;
	private static final String SQL_DROP_RUN_POINT =
	    "DROP TABLE IF EXISTS " + RunanasContract.RunPoint.TABLE_NAME;
	
	public RunStorageSqlLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    
	@Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RUN_METRICS);
        db.execSQL(SQL_CREATE_RUN_POINT);
    }
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Revise this upgrade policy.
        db.execSQL(SQL_DROP_RUN_METRICS);
        db.execSQL(SQL_DROP_RUN_POINT);
        onCreate(db);
    }

	public void storeRunPoint(RunPoint runPoint) {
		// TODO Auto-generated method stub
	}
	public List<Run> getRuns() {
		// TODO Auto-generated method stub
		return null;
	}
}
