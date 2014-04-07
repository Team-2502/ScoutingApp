package com.team2502.scoutingapp.data;

import java.util.ArrayList;
import java.util.Collections;

import com.team2502.scoutingapp.TeamRankingActivity;

import android.content.Context;
import android.util.Log;

public class LocalWebDatabase {
	
	private static final String LAST_ROW_DOWNLOADED_PREFERENCE = "last_row_downloaded";
	private ArrayList<Match> matches;
	private PreferencesDatabase preferenceData;
	private LocalDatabase localData;
	private WebDatabase webData;
	private int lastRowDownloaded;
	
	public LocalWebDatabase(Context context) {
		preferenceData = new PreferencesDatabase(context);
		localData = new LocalDatabase(context);
		webData = new WebDatabase();
		matches = new ArrayList<Match>();
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
		int row = 0;
		do {
			ArrayList <Match> downloaded = localData.getRows(row, 500);
			row += downloaded.size();
			downloadSize = downloaded.size();
			matches.addAll(downloaded);
		} while (downloadSize == 500);
	}
	
	private void updateWebDatabase() {
		int downloadSize = 0;
		int row = 0;
		do {
			ArrayList <Match> downloaded = webData.getRows(row, 500);
			row += downloaded.size();
			downloadSize = downloaded.size();
			matches.addAll(downloaded);
		} while (downloadSize == 500);
	}
	
	public ArrayList <Match> getAllMatches() {
		return matches;
	}
	
}
