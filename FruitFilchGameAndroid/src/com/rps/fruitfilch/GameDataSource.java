package com.rps.fruitfilch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class GameDataSource implements SQLHelperLibgdx {
	
	SQLHelperAndroid sqlHelperAndroid;
	SQLiteDatabase db;
	//ContentValues values = new ContentValues();
	
	public GameDataSource(Context context) {
		sqlHelperAndroid = new SQLHelperAndroid(context);
	}

	public void openDatabase() {
		try {
			db = sqlHelperAndroid.getWritableDatabase();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}		
	}
	
	public void closeDatabase() {
		db.close();
	}

	public void updateLevelScore(int levelID, int score, int stars) {
		String query1 = "SELECT * FROM world1 WHERE levelID=" + levelID;
		Cursor cursor = db.rawQuery(query1, null);
			if(cursor.moveToFirst()) {
				//Log.i(cursor.getString(1), cursor.getString(1));
				if(cursor.getString(1).compareTo("false") == 0) {
					//Log.i("I got here", "I got here");
					String query = "UPDATE world1 SET levelCleared='true' WHERE levelID=" + levelID;
					//db.rawQuery(query, null);
					db.execSQL(query);
				}
				if(score > cursor.getInt(2)) {
					String query = "UPDATE world1 SET score=" + score + " WHERE levelID=" + levelID;
					//db.rawQuery(query, null);
					db.execSQL(query);
				}
				if(stars > cursor.getInt(3)) {
					String query = "UPDATE world1 SET stars=" + stars +  " WHERE levelID=" + levelID;
					//db.rawQuery(query, null);
					db.execSQL(query);
			}
		}
	}
	
	public int getScore(int levelID) {
		String query1 = "SELECT * FROM world1 WHERE levelID=" + levelID;
		Cursor cursor = db.rawQuery(query1, null);
		if(cursor.moveToFirst()) {
			return cursor.getInt(2);
		}
		return -1;
	}
	
	public int getStars(int levelID) {
		String query1 = "SELECT * FROM world1 WHERE levelID=" + levelID;
		Cursor cursor = db.rawQuery(query1, null);
		if(cursor.moveToFirst()) {
			return cursor.getInt(3);
		}
		return -1;
	}
	
	public boolean isLevelCleared(int levelID) {
		String query1 = "SELECT * FROM world1 WHERE levelID=" + levelID;
		Cursor cursor = db.rawQuery(query1, null);
		cursor.moveToFirst();
		return Boolean.valueOf(cursor.getString(1));
	}

}
