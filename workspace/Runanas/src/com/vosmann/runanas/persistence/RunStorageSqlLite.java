package com.vosmann.runanas.persistence;

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
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	
	private static final String SQL_CREATE_RUN_METRICS =
	    "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
	    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
	    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
	    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
	    " )";	
	private static final String SQL_CREATE_RUN_POINT =
	    "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
	    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
	    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
	    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
	    " )";

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
//	@Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onUpgrade(db, oldVersion, newVersion);
//    }

	private static final String DICTIONARY_TABLE_NAME = "Run";
	private static final String DICTIONARY_TABLE_CREATE =
			"CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
					"SOME_KEY_WORD" + " TEXT, " +
					"SOME_KEY_DEFINITION" + " TEXT);";
	



}
