package com.team2502.scoutingapp.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.team2502.scoutingapp.Utilities;
import com.team2502.scoutingapp.data.Match.GameType;

public class WebDatabase implements Database {
	
	private static final String HOST = "http://www.team2502.com/scouting_database_2014/";
	
	public WebDatabase() {
		
	}
	
	@Override
	public void addMatchData(Match match) {
		addMatchData(match, null);
	}
	
	@Override
	public void addMatchData(final Match match, final DatabaseCallback callback) {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				String url = HOST + "add_item.php?";
				url += "timestamp=" + match.getEntryTimestamp();
				url += "&regional=" + match.getRegional();
				url += "&team_number=" + match.getTeam().getTeamNumber();
				url += "&match_number=" + match.getGameType().getShortName() + match.getMatchNumber();
				url += "&overall_rating=" + match.getRating();
				url += "&notes=" + match.getNotes();
				url += "&auto_moved=" + (match.isAutoMoved()?"1":"0");
				url += "&auto_scored_low=" + (match.isAutoScoredLow()?"1":"0");
				url += "&auto_scored_high=" + (match.isAutoScoredHigh()?"1":"0");
				url += "&auto_scored_hot=" + (match.isAutoScoredHot()?"1":"0");
				url += "&teleop_pick_up=" + match.getOffGround();
				url += "&teleop_assist_initialized=" + match.getAssistsStarted();
				url += "&teleop_assist_acquired=" + match.getAssistsReceived();
				url += "&teleop_second_assist_initialized=" + match.getSecAssistsStarted();
				url += "&teleop_second_assist_acquired=" + match.getSecAssistsReceived();
				url += "&teleop_scored_low=" + match.getScoredLow();
				url += "&teleop_scored_high=" + match.getScoredHigh();
				url += "&teleop_throw_truss=" + match.getOffGround();
				url += "&teleop_catch_truss=" + match.getFromTruss();
				url += "&strategy_goal=" + (match.isGoalie()?"1":"0");
				url += "&strategy_passer=" + (match.isPasser()?"1":"0");
				url += "&strategy_catcher=" + (match.isCatcher()?"1":"0");
				url += "&strategy_launcher=" + (match.isLauncher()?"1":"0");
				url += "&strategy_defense=" + (match.isDefense()?"1":"0");
				url += "&strategy_broken=" + (match.isBroken()?"1":"0");
				boolean success = getWebsiteData(url).toLowerCase(Locale.US).contains("success");
				if (callback != null)
					callback.onMatchDataAdded(match, success);
			}
		});
	}
	
	@Override
	public void requestTeamData(Team team, String regional, final DatabaseCallback callback) {
		final String url = HOST+"get_items.php?team="+team.getTeamNumber()+"&regional="+regional;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				if (callback != null)
					callback.onMatchDataReceived(getMatches(url));
			}
		});
	}
	
	@Override
	public void requestTeamData(Team team, final DatabaseCallback callback) {
		final String url = HOST+"get_items.php?team="+team.getTeamNumber();
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				if (callback != null)
					callback.onMatchDataReceived(getMatches(url));
			}
		});
	}
	
	@Override
	public void requestRegionalData(String regional, final DatabaseCallback callback) {
		final String url = HOST+"get_items.php?regional="+regional;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				if (callback != null)
					callback.onMatchDataReceived(getMatches(url));
			}
		});
	}
	
	public void requestRows(DatabaseCallback callback) {
		requestRows(1, 100, callback);
	}
	
	public void requestRows(final int start, final int end, final DatabaseCallback callback) {
		final String url = HOST+"get_items.php?start="+(start-1)+"&end="+end;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				if (callback != null)
					callback.onMatchDataReceived(getMatches(url));
			}
		});
	}
	
	private Map <String, Match> getMatches(String url) {
		String data = getWebsiteData(url);
		String [] entries = data.split("\n");
		Map <String, Match> matches = new HashMap<String, Match>();
		for (int i = 1; i < entries.length; i++) {
			String [] columns = entries[i].split(",");
			if (columns.length < 25)
				continue;
			Match m = parseMatch(columns);
			String matchID = "T"+m.getTeam().getTeamNumber()+m.getGameType().getShortName()+m.getMatchNumber();
			matches.put(matchID, m);
		}
		return matches;
	}
	
	private String getWebsiteData(String url) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			return sb.toString();
		} catch (IOException e) {
			return null;
		}
	}
	
	private Match parseMatch(String [] columns) {
		Match match = new Match();
		int index = 0;
		index = parseMatchOverhead(match, columns, index);
		index = parseMatchAuto(match, columns, index);
		index = parseMatchTeleop(match, columns, index);
		index = parseMatchStrategy(match, columns, index);
		return match;
	}
	
	private int parseMatchOverhead(Match match, String [] columns, int startIndex) {
		match.setEntryTimestamp(columns[startIndex+0]);
		match.setRegional(columns[startIndex+1]);
		match.setTeam(new Team(Integer.parseInt(columns[startIndex+2])));
		char gt = Character.toLowerCase(columns[startIndex+3].length()>0?columns[3].charAt(0):' ');
		if (gt == 'p') match.setGameType(GameType.PRACTICE);
		else if (gt == 'q') match.setGameType(GameType.QUALIFICATION);
		else if (gt == 'e') match.setGameType(GameType.ELIMINATION);
		else match.setGameType(GameType.INVALID);
		match.setMatchNumber(Utilities.parseIntSafe(columns[startIndex+3].substring(1)));
		match.setRating(Utilities.parseFloatSafe(columns[startIndex+4]));
		match.setNotes(columns[startIndex+5]);
		return startIndex + 6;
	}
	
	private int parseMatchAuto(Match match, String [] columns, int startIndex) {
		match.setAutoMoved(columns[startIndex+0].equalsIgnoreCase("1"));
		match.setAutoScoredLow(columns[startIndex+1].equalsIgnoreCase("1"));
		match.setAutoScoredHigh(columns[startIndex+2].equalsIgnoreCase("1"));
		match.setAutoScoredHot(columns[startIndex+3].equalsIgnoreCase("1"));
		return startIndex + 4;
	}
	
	private int parseMatchTeleop(Match match, String [] columns, int startIndex) {
		match.setOffGround(Utilities.parseIntSafe(columns[startIndex+0]));
		match.setAssistsStarted(Utilities.parseIntSafe(columns[startIndex+1]));
		match.setAssistsReceived(Utilities.parseIntSafe(columns[startIndex+2]));
		match.setSecAssistsStarted(Utilities.parseIntSafe(columns[startIndex+3]));
		match.setSecAssistsReceived(Utilities.parseIntSafe(columns[startIndex+4]));
		match.setScoredLow(Utilities.parseIntSafe(columns[startIndex+5]));
		match.setScoredHigh(Utilities.parseIntSafe(columns[startIndex+6]));
		match.setOverTruss(Utilities.parseIntSafe(columns[startIndex+7]));
		match.setFromTruss(Utilities.parseIntSafe(columns[startIndex+8]));
		return startIndex + 9;
	}
	
	private int parseMatchStrategy(Match match, String [] columns, int startIndex) {
		match.setGoalie(columns[startIndex+0].equalsIgnoreCase("1"));
		match.setPasser(columns[startIndex+1].equalsIgnoreCase("1"));
		match.setCatcher(columns[startIndex+2].equalsIgnoreCase("1"));
		match.setLauncher(columns[startIndex+3].equalsIgnoreCase("1"));
		match.setDefense(columns[startIndex+4].equalsIgnoreCase("1"));
		match.setBroken(columns[startIndex+5].equalsIgnoreCase("1"));
		return startIndex + 6;
	}
	
}
