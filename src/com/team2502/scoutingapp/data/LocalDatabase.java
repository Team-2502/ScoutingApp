package com.team2502.scoutingapp.data;

import java.util.ArrayList;
import com.team2502.scoutingapp.Utilities;
import com.team2502.scoutingapp.data.Match.GameType;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class LocalDatabase extends SQLiteOpenHelper implements Database {
	
	private static final boolean deleteOnStart = false;
	
	// Matches Table
	private static final String MATCH_TABLE = "matches";
	private static final String MATCH_TIMESTAMP_COLUMN = "timestamp";
	private static final String MATCH_REGIONAL_COLUMN = "regional";
	private static final String MATCH_TEAM_NUMBER_COLUMN = "team_number";
	private static final String MATCH_MATCH_NUMBER_COLUMN = "match_number";
	private static final String MATCH_OVERALL_RATING_COLUMN = "overall_rating";
	private static final String MATCH_NOTES_COLUMN = "notes";
	private static final String MATCH_AUTO_MOVED_COLUMN = "auto_moved";
	private static final String MATCH_AUTO_SCORED_LOW_COLUMN = "auto_scored_low";
	private static final String MATCH_AUTO_SCORED_HIGH_COLUMN = "auto_scored_high";
	private static final String MATCH_AUTO_SCORED_HOT_COLUMN = "auto_scored_hot";
	private static final String MATCH_TELEOP_PICK_UP_COLUMN = "teleop_pick_up";
	private static final String MATCH_TELEOP_ASSIST_INITIALIZED_COLUMN = "teleop_assist_initialized";
	private static final String MATCH_TELEOP_ASSIST_ACQUIRED_COLUMN = "teleop_assist_acquired";
	private static final String MATCH_TELEOP_SECOND_ASSIST_INITIALIZED_COLUMN = "teleop_second_assist_initialized";
	private static final String MATCH_TELEOP_SECOND_ASSIST_ACQUIRED_COLUMN = "teleop_second_assist_acquired";
	private static final String MATCH_TELEOP_SCORED_LOW_COLUMN = "teleop_scored_low";
	private static final String MATCH_TELEOP_SCORED_HIGH_COLUMN = "teleop_scored_high";
	private static final String MATCH_TELEOP_THROW_TRUSS_COLUMN = "teleop_throw_truss";
	private static final String MATCH_TELEOP_CATCH_TRUSS_COLUMN = "teleop_catch_truss";
	private static final String MATCH_STRATEGY_GOAL_COLUMN = "strategy_goal";
	private static final String MATCH_STRATEGY_PASSER_COLUMN = "strategy_passer";
	private static final String MATCH_STRATEGY_CATCHER_COLUMN = "strategy_catcher";
	private static final String MATCH_STRATEGY_LAUNCHER_COLUMN = "strategy_launcher";
	private static final String MATCH_STRATEGY_DEFENSE_COLUMN = "strategy_defense";
	private static final String MATCH_STRATEGY_BROKEN_COLUMN = "strategy_broken";
	private static final String CREATE_MATCH_TABLE = "CREATE TABLE " + MATCH_TABLE + " (" + 
			MATCH_TIMESTAMP_COLUMN + " TIMESTAMP, " + 
			MATCH_REGIONAL_COLUMN + " TEXT, " + 
			MATCH_TEAM_NUMBER_COLUMN + " INT, " + 
			MATCH_MATCH_NUMBER_COLUMN + " TEXT, " + 
			MATCH_OVERALL_RATING_COLUMN + " REAL, " + 
			MATCH_NOTES_COLUMN + " TEXT, " + 
			MATCH_AUTO_MOVED_COLUMN + " INT, " + 
			MATCH_AUTO_SCORED_LOW_COLUMN + " INT, " + 
			MATCH_AUTO_SCORED_HIGH_COLUMN + " INT, " + 
			MATCH_AUTO_SCORED_HOT_COLUMN + " INT, " + 
			MATCH_TELEOP_PICK_UP_COLUMN + " INT, " + 
			MATCH_TELEOP_ASSIST_ACQUIRED_COLUMN + " INT, " + 
			MATCH_TELEOP_ASSIST_INITIALIZED_COLUMN + " INT, " + 
			MATCH_TELEOP_SECOND_ASSIST_INITIALIZED_COLUMN + " INT, " + 
			MATCH_TELEOP_SECOND_ASSIST_ACQUIRED_COLUMN + " INT, " + 
			MATCH_TELEOP_SCORED_LOW_COLUMN + " INT, " + 
			MATCH_TELEOP_SCORED_HIGH_COLUMN + " INT, " + 
			MATCH_TELEOP_THROW_TRUSS_COLUMN + " INT, " + 
			MATCH_TELEOP_CATCH_TRUSS_COLUMN + " INT, " + 
			MATCH_STRATEGY_GOAL_COLUMN + " INT, " + 
			MATCH_STRATEGY_PASSER_COLUMN + " INT, " + 
			MATCH_STRATEGY_CATCHER_COLUMN + " INT, " + 
			MATCH_STRATEGY_LAUNCHER_COLUMN + " INT, " + 
			MATCH_STRATEGY_DEFENSE_COLUMN + " INT, " + 
			MATCH_STRATEGY_BROKEN_COLUMN + " INT" + 
			")";
	private static final String COLUMN_LIST = 
				MATCH_TIMESTAMP_COLUMN + ", " + MATCH_REGIONAL_COLUMN + ", " + 
				MATCH_TEAM_NUMBER_COLUMN + ", " + MATCH_MATCH_NUMBER_COLUMN + ", " + 
				MATCH_OVERALL_RATING_COLUMN + ", " + MATCH_NOTES_COLUMN + ", " + 
				MATCH_AUTO_MOVED_COLUMN + ", " + MATCH_AUTO_SCORED_LOW_COLUMN + ", " + 
				MATCH_AUTO_SCORED_HIGH_COLUMN + ", " + MATCH_AUTO_SCORED_HOT_COLUMN + ", " + 
				MATCH_TELEOP_PICK_UP_COLUMN + ", " + MATCH_TELEOP_ASSIST_INITIALIZED_COLUMN + ", " + 
				MATCH_TELEOP_ASSIST_ACQUIRED_COLUMN + ", " + 
				MATCH_TELEOP_SECOND_ASSIST_INITIALIZED_COLUMN + ", " + 
				MATCH_TELEOP_SECOND_ASSIST_ACQUIRED_COLUMN + ", " + 
				MATCH_TELEOP_SCORED_LOW_COLUMN + ", " + MATCH_TELEOP_SCORED_HIGH_COLUMN + ", " + 
				MATCH_TELEOP_THROW_TRUSS_COLUMN + ", " + MATCH_TELEOP_CATCH_TRUSS_COLUMN + ", " + 
				MATCH_STRATEGY_GOAL_COLUMN + ", " + MATCH_STRATEGY_PASSER_COLUMN + ", " + 
				MATCH_STRATEGY_CATCHER_COLUMN + ", " + MATCH_STRATEGY_LAUNCHER_COLUMN + ", " + 
				MATCH_STRATEGY_DEFENSE_COLUMN + ", " + MATCH_STRATEGY_BROKEN_COLUMN;
	
	public LocalDatabase(Context context) {
		super(context, "matches.db", null, 1);
		if (deleteOnStart) {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + MATCH_TABLE);
			db.execSQL(CREATE_MATCH_TABLE);
			db.close();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MATCH_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MATCH_TABLE);
		onCreate(db);
	}
	
	public void open() {
		getWritableDatabase();
	}
	
	@Override
	public void addMatchData(Match match) {
		addMatchData(match, null);
	}
	
	@Override
	public void addMatchData(Match match, DatabaseCallback callback) {
		boolean success = addMatch(match);
		if (callback != null)
			callback.onMatchDataAdded(match, success);
	}
	
	public ArrayList <Match> getTeamData(Team team, String regional) {
		String query = "SELECT "+COLUMN_LIST+" FROM "+MATCH_TABLE+" WHERE "+MATCH_TEAM_NUMBER_COLUMN+" = ? AND "+MATCH_REGIONAL_COLUMN+"= ?";
		String [] args = new String[]{Integer.toString(team.getTeamNumber()), regional};
		return getAllMatches(query, args);
	}
	
	@Override
	public void requestTeamData(Team team, String regional, DatabaseCallback callback) {
		if (callback != null)
			callback.onMatchDataReceived(getTeamData(team, regional));
	}
	
	public ArrayList <Match> getRegionalData(String regional) {
		String query = "SELECT "+COLUMN_LIST+" FROM "+MATCH_TABLE+" WHERE "+MATCH_REGIONAL_COLUMN+"= ?";
		return getAllMatches(query, new String[]{regional});
	}
	
	@Override
	public void requestRegionalData(String regional, DatabaseCallback callback) {
		if (callback != null)
			callback.onMatchDataReceived(getRegionalData(regional));
	}
	
	public ArrayList <Match> getTeamData(Team team) {
		String query = "SELECT "+COLUMN_LIST+" FROM "+MATCH_TABLE+" WHERE "+MATCH_TEAM_NUMBER_COLUMN+" = ?";
		String [] args = new String[]{Integer.toString(team.getTeamNumber())};
		return getAllMatches(query, args);
	}
	
	@Override
	public void requestTeamData(Team team, DatabaseCallback callback) {
		if (callback != null)
			callback.onMatchDataReceived(getTeamData(team));
	}
	
	public ArrayList <Match> getRows(int start, int limit) {
		String query = "SELECT "+COLUMN_LIST+" FROM "+MATCH_TABLE+" LIMIT " + start + "," + limit;
		return getAllMatches(query, new String[]{});
	}
	
	@Override
	public void requestRows(int start, int limit, DatabaseCallback callback) {
		if (callback != null)
			callback.onMatchDataReceived(getRows(start, limit));
	}
	
	private boolean addMatch(Match match) {
		String query = "INSERT INTO " + MATCH_TABLE + "(" + COLUMN_LIST + ")" + 
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		SQLiteDatabase db = getWritableDatabase();
		SQLiteStatement ins = db.compileStatement(query);
		ins.clearBindings();
		ins.bindString(1, match.getEntryTimestamp()==null?"":match.getEntryTimestamp());
		ins.bindString(2, match.getRegional()==null?"":match.getRegional());
		ins.bindLong(3, match.getTeam().getTeamNumber());
		ins.bindString(4, match.getGameType().getShortName() + match.getMatchNumber());
		ins.bindDouble(5, match.getRating());
		ins.bindString(6, match.getNotes());
		ins.bindLong(7, match.isAutoMoved()?1:0);
		ins.bindLong(8, match.getAutoScoredLow());
		ins.bindLong(9, match.getAutoScoredHigh());
		ins.bindLong(10, match.isAutoScoredHot()?1:0);
		ins.bindLong(11, match.getOffGround());
		ins.bindLong(12, match.getAssistsStarted());
		ins.bindLong(13, match.getAssistsReceived());
		ins.bindLong(14, match.getSecAssistsStarted());
		ins.bindLong(15, match.getSecAssistsReceived());
		ins.bindLong(16, match.getScoredLow());
		ins.bindLong(17, match.getScoredHigh());
		ins.bindLong(18, match.getOverTruss());
		ins.bindLong(19, match.getFromTruss());
		ins.bindLong(20, match.isGoalie()?1:0);
		ins.bindLong(21, match.isPasser()?1:0);
		ins.bindLong(22, match.isCatcher()?1:0);
		ins.bindLong(23, match.isLauncher()?1:0);
		ins.bindLong(24, match.isDefense()?1:0);
		ins.bindLong(25, match.isBroken()?1:0);
		long row = ins.executeInsert();
		db.close();
		return row != -1;
	}
	
	private ArrayList <Match> getAllMatches(String query, String [] args) {
		ArrayList <Match> matches = new ArrayList<Match>();
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(query, args);
		
		if (cursor.moveToFirst()) {
			do {
				Match match = new Match();
				match.setEntryTimestamp(cursor.getString(0));
				match.setRegional(cursor.getString(1));
				match.setTeam(new Team(cursor.getInt(2)));
				String matchNum = cursor.getString(3);
				char matchType = Character.toLowerCase(matchNum.length()>0?matchNum.charAt(0):' ');
				if (matchType == 'p') match.setGameType(GameType.PRACTICE);
				else if (matchType == 'q') match.setGameType(GameType.QUALIFICATION);
				else if (matchType == 'e') match.setGameType(GameType.ELIMINATION);
				else match.setGameType(GameType.INVALID);
				match.setMatchNumber(Utilities.parseIntSafe(matchNum.substring(1)));
				match.setRating(cursor.getFloat(4));
				match.setNotes(cursor.getString(5));
				match.setAutoMoved(cursor.getInt(6)==1);
				match.setAutoScoredLow(cursor.getInt(7));
				match.setAutoScoredHigh(cursor.getInt(8));
				match.setAutoScoredHot(cursor.getInt(9)==1);
				match.setOffGround(cursor.getInt(10));
				match.setAssistsStarted(cursor.getInt(11));
				match.setAssistsReceived(cursor.getInt(12));
				match.setSecAssistsStarted(cursor.getInt(13));
				match.setSecAssistsReceived(cursor.getInt(14));
				match.setScoredLow(cursor.getInt(15));
				match.setScoredHigh(cursor.getInt(16));
				match.setOverTruss(cursor.getInt(17));
				match.setFromTruss(cursor.getInt(18));
				match.setGoalie(cursor.getInt(19)==1);
				match.setPasser(cursor.getInt(20)==1);
				match.setCatcher(cursor.getInt(21)==1);
				match.setLauncher(cursor.getInt(22)==1);
				match.setDefense(cursor.getInt(23)==1);
				match.setBroken(cursor.getInt(24)==1);
				matches.add(match);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return matches;
	}
	
}
