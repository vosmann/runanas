package com.vosmann.runanas.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RunDataStore extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "Runanas";
	private static final int DATABASE_VERSION = 2;
	private static final String DICTIONARY_TABLE_NAME = "Run";
	private static final String DICTIONARY_TABLE_CREATE =
			"CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
					"SOME_KEY_WORD" + " TEXT, " +
					"SOME_KEY_DEFINITION" + " TEXT);";
	
	public RunDataStore(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(DICTIONARY_TABLE_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
	
	public void writeToFile(RunPoint runPoint) {
	}
	public void writeToFile(Run run) {
	}
	public void writeToDatabase(Run run) {
	}


	



}
