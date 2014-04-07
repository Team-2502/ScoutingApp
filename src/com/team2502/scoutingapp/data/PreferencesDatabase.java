package com.team2502.scoutingapp.data;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class PreferencesDatabase extends SQLiteOpenHelper {
	
	private static final String PREFERENCES_TABLE = "preferences";
	private static final String PREFERENCES_KEY_COLUMN = "key";
	private static final String PREFERENCES_VALUE_COLUMN = "value";
	private static final String CREATE_PREFERENCES_TABLE = "CREATE TABLE " + PREFERENCES_TABLE + " (" + 
			PREFERENCES_KEY_COLUMN + " TEXT, " + 
			PREFERENCES_VALUE_COLUMN + " TEXT" + 
			")";
	private Map <String, String> preferences;
	
	public PreferencesDatabase(Context context) {
		super(context, "preferences.db", null, 1);
		preferences = new HashMap<String, String>();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PREFERENCES_TABLE);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.i("PreferencesDatabase", "Loading preferences");
		Cursor cursor = db.rawQuery("SELECT * FROM " + PREFERENCES_TABLE, null);
		if (cursor.moveToFirst()) {
			do {
				Log.i("PreferencesDatabase", "Preference: KEY=" + cursor.getString(0) + "  VALUE=" + cursor.getString(1));
				preferences.put(cursor.getString(0), cursor.getString(1));
			} while (cursor.moveToNext());
		}
		cursor.close();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PREFERENCES_TABLE);
		onCreate(db);
	}
	
	public void open() {
		getWritableDatabase();
	}
	
	public String getPreference(String key) {
		return preferences.get(key);
	}
	
	@SuppressLint("NewApi")
	public boolean putPreference(String key, String value) {
		SQLiteDatabase db = getWritableDatabase();
		if (preferences.get(key) == null) {
			Log.d("PreferencesDatabase", "NULL Preference");
			SQLiteStatement stmt = db.compileStatement("INSERT INTO " + PREFERENCES_TABLE + " (key, value) VALUES (?, ?)");
			stmt.bindString(1, key);
			stmt.bindString(2, value);
			boolean ret = stmt.executeInsert() != -1;
			db.close();
			return ret;
		} else if (!preferences.get(key).equals(value)) {
			preferences.put(key, value);
			SQLiteStatement stmt = db.compileStatement("UPDATE " + PREFERENCES_TABLE + " SET value = ? WHERE key = ?");
			stmt.bindString(1, value);
			stmt.bindString(2, key);
			boolean ret = true;
			if (android.os.Build.VERSION.SDK_INT >= 11)
				ret = stmt.executeUpdateDelete() > 0;
			else
				stmt.execute();
			db.close();
			return ret;
		}
		return true;
	}
	
}
