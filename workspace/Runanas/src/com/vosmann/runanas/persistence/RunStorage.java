package com.vosmann.runanas.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vosmann.runanas.model.Run;
import com.vosmann.runanas.model.RunResult;
import com.vosmann.runanas.model.RunPoint;

public class RunStorage {
	private static final String TAG = "RunStorage";
	
//	/**
//	 * A projection that specifies only the run ID column from the RunPoint
//	 * table. These IDs are then use to retrieve only the last N runs.
//	 */
//	private static final String[] RUN_ID_PROJECTION = {
//			RunanasContract.RunResult.COLUMN_NAME_RUN_ID
//	};
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
	 * the query for RunResult is made.
	 */
	private static final String[] RUN_RESULT_PROJECTION = {
			RunanasContract.RunResult.COLUMN_NAME_RUN_ID,
			RunanasContract.RunResult.COLUMN_NAME_TIME,
			RunanasContract.RunResult.COLUMN_NAME_DISTANCE,
			RunanasContract.RunResult.COLUMN_NAME_DURATION,
			RunanasContract.RunResult.COLUMN_NAME_AVG_SPEED,
			RunanasContract.RunResult.COLUMN_NAME_MAX_SPEED,
			RunanasContract.RunResult.COLUMN_NAME_MIN_SPEED,
			RunanasContract.RunResult.COLUMN_NAME_MASS,
			RunanasContract.RunResult.COLUMN_NAME_ENERGY
	};
	
	private static SQLiteDatabase getWritableDb(Context context) {
		RunanasDbHelper runanasDbHelper = new RunanasDbHelper(context);
		SQLiteDatabase db = runanasDbHelper.getWritableDatabase();
		return db;
	}
	private static SQLiteDatabase getReadableDb(Context context) {
		RunanasDbHelper runanasDbHelper = new RunanasDbHelper(context);
		SQLiteDatabase db = runanasDbHelper.getReadableDatabase();
		return db;
	}
		
	public static void storeRunPoint(RunPoint runPoint, Context context) {
		SQLiteDatabase db = getWritableDb(context);
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
	
	public static void storeRunResult(RunResult runResult , Context context) {
		SQLiteDatabase db = getWritableDb(context);
		// Preparing key (column name) - value pairs to store into the DB.
		ContentValues values = new ContentValues();
		values.put(RunanasContract.RunResult.COLUMN_NAME_RUN_ID,
				runResult.getRunId());
		values.put(RunanasContract.RunResult.COLUMN_NAME_TIME,
				runResult.getTime());
		values.put(RunanasContract.RunResult.COLUMN_NAME_DISTANCE,
				runResult.getDistance());
		values.put(RunanasContract.RunResult.COLUMN_NAME_DURATION,
				runResult.getDuration());
		values.put(RunanasContract.RunResult.COLUMN_NAME_AVG_SPEED,
				runResult.getAvgSpeed());
		values.put(RunanasContract.RunResult.COLUMN_NAME_MAX_SPEED,
				runResult.getMaxSpeed());
		values.put(RunanasContract.RunResult.COLUMN_NAME_MIN_SPEED,
				runResult.getMinSpeed());
		values.put(RunanasContract.RunResult.COLUMN_NAME_MASS,
				runResult.getMass());
		values.put(RunanasContract.RunResult.COLUMN_NAME_ENERGY,
				runResult.getEnergy());
		// Disregarding ID of newly added row. 
		// Using null because no nullable column is available.
		db.insert(RunanasContract.RunResult.TABLE_NAME, null, values);
		
	}
	
	public static List<Run> getAllRuns(Context context) {
		List<Run> runs = null;
		runs = getRuns(0, context);
		return runs;
	}
	
	// Maybe add a getRuns() that gets all runs after a certain date.
	/**
	 * Gets the Run objects from the SQLite database.
	 * 
	 * @param nrLastRuns
	 *            Number of last Runs to get. If zero or negative, all Runs are
	 *            retrieved.
	 * @param context
	 * @return The runs sorted in the order they were inserted into the
	 *         database.
	 */
	public static List<Run> getRuns(int nrLastRuns, Context context) {
		String nrLastRunsLimit = null;
		if (nrLastRuns > 0) {
			nrLastRunsLimit = Integer.toString(nrLastRuns);
		}
		List<Run> runs = new LinkedList<Run>();
		List<Long> runIds = new LinkedList<Long>();
		Map<Long, RunResult> runResults = new HashMap<Long, RunResult>();
		Map<Long, List<RunPoint>> runPoints =
				new HashMap<Long, List<RunPoint>>();
		StringBuilder runIdSet = new StringBuilder();
		
		String selection = null;
		String[] selectionArgs = null;
		final boolean isDistinct = true;
		
		SQLiteDatabase db = getReadableDb(context);
		
		// Get N last added RunResults.
		String runResultSortOrder = RunanasContract.RunResult._ID
				+ " DESC";
		Cursor runResultCursor = db.query(isDistinct,
				RunanasContract.RunResult.TABLE_NAME, // Table to query
				RUN_RESULT_PROJECTION, // RUN_ID_PROJECTION, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows (groupBy)
				null, // don't filter by row groups (having)
				runResultSortOrder, // (orderBy)
				nrLastRunsLimit // (limit)
				);
		runResultCursor.moveToFirst();
		while (!runResultCursor.isAfterLast()) {
			try {
				RunResult runResult = createRunResult(runResultCursor);
				runIds.add(runResult.getRunId());
				runResults.put(runResult.getRunId(), runResult);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "getRuns(): Tried to retrieve a column value using "
						+ "a non-existing column name.");
			}
			runResultCursor.moveToNext();
		}
		Collections.reverse(runIds); //  Sort from oldest to newest.

		// Get the RunPoints.
		selection = null; // where in (...)
		selectionArgs = null;
		// String runPointsSortOrder = RunanasContract.RunPoint.COLUMN_NAME_RUN_ID
		String runPointsSortOrder = RunanasContract.RunPoint.COLUMN_NAME_TIME
				+ " DESC";
		Cursor runPointsCursor = db.query(
				RunanasContract.RunPoint.TABLE_NAME, // Table to query
				RUN_POINT_PROJECTION, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				runPointsSortOrder);

//		// Get RunResult.
//		String runResultSortOrder = RunanasContract.RunResult.COLUMN_NAME_RUN_ID
//				+ " DESC";
//		Cursor runResultCursor = db.query(
//				RunanasContract.RunResult.TABLE_NAME, // The table to query
//				RUN_RESULT_PROJECTION, // The columns to return
//				selection, // The columns for the WHERE clause
//				selectionArgs, // The values for the WHERE clause
//				null, // don't group the rows
//				null, // don't filter by row groups
//		    runResultSortOrder); 
		
		return runs;
	}
	
	private static RunResult createRunResult(Cursor cursor) {
		RunResult runResult = null;
		// Ugly repetitive code in dire need of refactoring.
		long runId = cursor.getLong(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_RUN_ID));
		long time = cursor.getLong(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_TIME));
		double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_DISTANCE));
		long duration = cursor.getLong(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_DURATION));
		double avgSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_AVG_SPEED));
		double maxSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_MAX_SPEED));
		double minSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_MIN_SPEED));
		double mass = cursor.getDouble(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_MASS));
		double energy = cursor.getDouble(cursor.getColumnIndexOrThrow(
						RunanasContract.RunResult.COLUMN_NAME_ENERGY));
		runResult = new RunResult(runId, time, distance, duration, avgSpeed,
				maxSpeed, minSpeed, mass, energy);
		return runResult;
	}
	private static RunPoint createRunPoint() {
		RunPoint runPoint = null;
		return runPoint;
	}
}
