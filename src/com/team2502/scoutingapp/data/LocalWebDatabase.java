package com.team2502.scoutingapp.data;

import java.util.ArrayList;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class LocalWebDatabase {
	
	private static final String LAST_ROW_DOWNLOADED_PREFERENCE = "last_row_downloaded";
	private ArrayList<Match> matches;
	private PreferencesDatabase preferenceData;
	private LocalDatabase localData;
	private WebDatabase webData;
	private Context context;
	private int lastRowDownloaded;
	
	public LocalWebDatabase(Context context) {
		preferenceData = new PreferencesDatabase(context);
		localData = new LocalDatabase(context);
		webData = new WebDatabase();
		matches = new ArrayList<Match>();
		this.context = context;
		this.lastRowDownloaded = 1;
		preferenceData.open();
		localData.open();
	}
	
	public void update() {
		if (preferenceData.getPreference(LAST_ROW_DOWNLOADED_PREFERENCE) != null)
			lastRowDownloaded = Integer.parseInt(preferenceData.getPreference(LAST_ROW_DOWNLOADED_PREFERENCE));
		preferenceData.putPreference(LAST_ROW_DOWNLOADED_PREFERENCE, Integer.toString(lastRowDownloaded));
		updateData();
	}
	
	public void close() {
		preferenceData.close();
		localData.close();
	}
	
	private void updateData() {
		matches.clear();
		updateLocalDatabase();
		updateWebDatabase();
	}
	
	private void updateLocalDatabase() {
		int downloadSize = 0;
		int row = 1;
		do {
			ArrayList <Match> downloaded = localData.getRows(row, 500);
			row += downloaded.size();
			downloadSize = downloaded.size();
			matches.addAll(downloaded);
		} while (downloadSize == 500);
	}
	
	private void updateWebDatabase() {
		int downloadSize = 0;
		do {
			try {
				ArrayList <Match> downloaded = webData.getRows(lastRowDownloaded, 500);
				lastRowDownloaded += downloaded.size();
				downloadSize = downloaded.size();
				matches.addAll(downloaded);
			} catch (Exception e) {
				Looper.prepare();
				Looper.loop();
				Toast.makeText(context, "Error connecting to database. No Wifi/Internet?", Toast.LENGTH_LONG).show();
				Looper.myLooper().quit();
			}
		} while (downloadSize == 500);
	}
	
	public ArrayList <Match> getAllMatches() {
		return matches;
	}
	
}
