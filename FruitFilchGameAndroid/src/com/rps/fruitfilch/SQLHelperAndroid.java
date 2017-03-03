package com.rps.fruitfilch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelperAndroid extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "fruitfilch.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE world1 (levelID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, levelCleared TEXT NOT NULL DEFAULT ('false'),score INTEGER DEFAULT (0), stars INTEGER DEFAULT (0));";
	
	
	
	SQLHelperAndroid(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		ContentValues values = new ContentValues();		
		for(int i = 1; i < 16; ++i) {
			values.put("levelID", i);
			values.put("levelCleared", "false");
			values.put("score", 0);
			values.put("stars", 0);
			db.insert("world1", null, values);
			values.clear();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
