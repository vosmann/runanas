package com.vosmann.runanas.persistence;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vosmann.runanas.model.Run;
import com.vosmann.runanas.model.RunMetrics;
import com.vosmann.runanas.model.RunPoint;

public class RunStorage {
	
	/**
	 * A projection that specifies only the run ID column from the RunPoint
	 * table. These IDs are then use to retrieve only the last N runs.
	 */
	private static final String[] RUN_ID_PROJECTION = {
			RunanasContract.RunPoint.COLUMN_NAME_RUN_ID
	};
	/**
	 * A projection that specifies which columns from the DB will be used after
	 * the query for RunPoints is made.
	 */
	private static final String[] RUN_POINT_PROJECTION = {
			RunanasContract.RunPoint.COLUMN_NAME_RUN_ID,
			RunanasContract.RunPoint.COLUMN_NAME_ACCURACY,
			RunanasContract.RunPoint.COLUMN_NAME_ALTITUDE,
			RunanasContract.RunPoint.COLUMN_NAME_BEARING,
			RunanasContract.RunPoint.COLUMN_NAME_LATITUDE,
			RunanasContract.RunPoint.COLUMN_NAME_LONGITUDE,
			RunanasContract.RunPoint.COLUMN_NAME_SPEED,
			RunanasContract.RunPoint.COLUMN_NAME_TIME
	};
	/**
	 * A projection that specifies which columns from the DB will be used after
	 * the query for RunMetrics is made.
	 */
	private static final String[] RUN_METRICS_PROJECTION = {
			RunanasContract.RunMetrics.COLUMN_NAME_RUN_ID,
			RunanasContract.RunMetrics.COLUMN_NAME_DISTANCE,
			RunanasContract.RunMetrics.COLUMN_NAME_DURATION,
			RunanasContract.RunMetrics.COLUMN_NAME_AVG_SPEED,
			RunanasContract.RunMetrics.COLUMN_NAME_MAX_SPEED,
			RunanasContract.RunMetrics.COLUMN_NAME_MIN_SPEED,
			RunanasContract.RunMetrics.COLUMN_NAME_MASS,
			RunanasContract.RunMetrics.COLUMN_NAME_ENERGY_EXPENDITURE
	};
	
	private static SQLiteDatabase getDb(Context context) {
		RunanasDbHelper runanasDbHelper = new RunanasDbHelper(context);
		SQLiteDatabase db = runanasDbHelper.getWritableDatabase();
		return db;
	}
		
	public static void storeRunPoint(RunPoint runPoint, Context context) {
		SQLiteDatabase db = getDb(context);
		// Preparing key (column name) - value pairs to store into the DB.
		ContentValues values = new ContentValues();
		values.put(RunanasContract.RunPoint.COLUMN_NAME_RUN_ID,
				runPoint.getRunId());
		values.put(RunanasContract.RunPoint.COLUMN_NAME_ACCURACY,
				runPoint.getLocation().getAccuracy());
		values.put(RunanasContract.RunPoint.COLUMN_NAME_ALTITUDE,
				runPoint.getLocation().getAltitude());
		values.put(RunanasContract.RunPoint.COLUMN_NAME_BEARING,
				runPoint.getLocation().getBearing());
		values.put(RunanasContract.RunPoint.COLUMN_NAME_LATITUDE,
				runPoint.getLocation().getLatitude());
		values.put(RunanasContract.RunPoint.COLUMN_NAME_LONGITUDE,
				runPoint.getLocation().getLongitude());
		values.put(RunanasContract.RunPoint.COLUMN_NAME_SPEED,
				runPoint.getLocation().getSpeed());
		values.put(RunanasContract.RunPoint.COLUMN_NAME_TIME,
				runPoint.getLocation().getTime());
		// Disregarding ID of newly added row. 
		// Using null because no nullable column is available.
		db.insert(RunanasContract.RunPoint.TABLE_NAME, null, values);
	}
	
	public static void storeRunMetrics(RunMetrics runMetrics , Context context) {
		SQLiteDatabase db = getDb(context);
		// Preparing key (column name) - value pairs to store into the DB.
		ContentValues values = new ContentValues();
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_RUN_ID,
				runMetrics.getRunId());
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_DISTANCE,
				runMetrics.getDistance());
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_DURATION,
				runMetrics.getDuration());
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_AVG_SPEED,
				runMetrics.getAvgSpeed());
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_MAX_SPEED,
				runMetrics.getMaxSpeed());
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_MIN_SPEED,
				runMetrics.getMinSpeed());
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_MASS,
				runMetrics.getMass());
		values.put(RunanasContract.RunMetrics.COLUMN_NAME_ENERGY_EXPENDITURE,
				runMetrics.getEnergyExpenditure());
		// Disregarding ID of newly added row. 
		// Using null because no nullable column is available.
		db.insert(RunanasContract.RunMetrics.TABLE_NAME, null, values);
		
	}
	
	public static List<Run> getRuns(int nrLastRuns, Context context) {
		List<Run> runs = new LinkedList<Run>();
		SQLiteDatabase db = getDb(context);
		
		// Improvement: get only entries from, e.g., the last three months.
		String selection = null;
		String[] selectionArgs = null;
		final boolean isDistinct = true;
		
		// Get list of run IDs ordered by descending time from RunPoints.
		String runIdSortOrder = RunanasContract.RunPoint.COLUMN_NAME_TIME
				+ " DESC";
		Cursor runIdsCursor = db.query(isDistinct,
				RunanasContract.RunPoint.TABLE_NAME, // The table to query
				RUN_ID_PROJECTION, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				runIdSortOrder);

		// Get the RunPoints.
		String runPointsSortOrder = RunanasContract.RunPoint.COLUMN_NAME_RUN_ID
				+ " DESC";
		Cursor runPointsCursor = db.query(RunanasContract.RunPoint.TABLE_NAME, // The
																				// table
																				// to
																				// query
				RUN_POINT_PROJECTION, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				runPointsSortOrder);

		// Get RunMetrics.
		String runMetricsSortOrder = RunanasContract.RunMetrics.COLUMN_NAME_RUN_ID
				+ " DESC";
		Cursor runMetricsCursor = db.query(
				RunanasContract.RunMetrics.TABLE_NAME, // The table to query
				RUN_METRICS_PROJECTION, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
		    runMetricsSortOrder); 
		
		return runs;
	}
}
