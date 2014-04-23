package com.team2502.scoutingapp.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
				String parameters = "";
				parameters += "timestamp=" + encode(match.getEntryTimestamp());
				parameters += "&regional=" + encode(match.getRegional());
				parameters += "&team_number=" + encode(match.getTeam().getTeamNumber());
				parameters += "&match_number=" + encode(match.getGameType().getShortName() + match.getMatchNumber());
				parameters += "&overall_rating=" + encode(match.getRating());
				parameters += "&notes=" + encode(match.getNotes());
				parameters += "&auto_moved=" + encode(match.isAutoMoved()?"1":"0");
				parameters += "&auto_scored_low=" + encode(match.getAutoScoredLow());
				parameters += "&auto_scored_high=" + encode(match.getAutoScoredHigh());
				parameters += "&auto_scored_hot=" + encode(match.isAutoScoredHot()?"1":"0");
				parameters += "&teleop_pick_up=" + encode(match.getOffGround());
				parameters += "&teleop_assist_initialized=" + encode(match.getAssistsStarted());
				parameters += "&teleop_assist_acquired=" + encode(match.getAssistsReceived());
				parameters += "&teleop_second_assist_initialized=" + encode(match.getSecAssistsStarted());
				parameters += "&teleop_second_assist_acquired=" + encode(match.getSecAssistsReceived());
				parameters += "&teleop_scored_low=" + encode(match.getScoredLow());
				parameters += "&teleop_scored_high=" + encode(match.getScoredHigh());
				parameters += "&teleop_throw_truss=" + encode(match.getOffGround());
				parameters += "&teleop_catch_truss=" + encode(match.getFromTruss());
				parameters += "&strategy_goal=" + encode(match.isGoalie()?"1":"0");
				parameters += "&strategy_passer=" + encode(match.isPasser()?"1":"0");
				parameters += "&strategy_catcher=" + encode(match.isCatcher()?"1":"0");
				parameters += "&strategy_launcher=" + encode(match.isLauncher()?"1":"0");
				parameters += "&strategy_defense=" + encode(match.isDefense()?"1":"0");
				parameters += "&strategy_broken=" + encode(match.isBroken()?"1":"0");
				try {
					boolean success = getWebsiteData("add_item.php", parameters).toLowerCase(Locale.US).contains("success");
					if (callback != null)
						callback.onMatchDataAdded(match, success);
				} catch (ClientProtocolException e) {
					callback.onDatabaseException(e);
				} catch (IOException e) {
					callback.onDatabaseException(e);
				}
			}
		});
	}
	
	public ArrayList<Match> getTeamData(Team team, String regional) throws ClientProtocolException, IOException {
		return getMatches("get_items.php", "team="+encode(team.getTeamNumber())+"&regional="+encode(regional));
	}
	
	@Override
	public void requestTeamData(final Team team, final String regional, final DatabaseCallback callback) {
		if (callback == null)
			return;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					callback.onMatchDataReceived(getTeamData(team, regional));
				} catch (ClientProtocolException e) {
					callback.onDatabaseException(e);
				} catch (IOException e) {
					callback.onDatabaseException(e);
				}
			}
		});
	}
	
	public ArrayList<Match> getTeamData(Team team) throws ClientProtocolException, IOException {
		return getMatches("get_items.php", "team="+encode(team.getTeamNumber()));
	}
	
	@Override
	public void requestTeamData(final Team team, final DatabaseCallback callback) {
		if (callback == null)
			return;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					callback.onMatchDataReceived(getTeamData(team));
				} catch (ClientProtocolException e) {
					callback.onDatabaseException(e);
				} catch (IOException e) {
					callback.onDatabaseException(e);
				}
			}
		});
	}
	
	public ArrayList<Match> getRegionalData(String regional) throws ClientProtocolException, IOException {
		return getMatches("get_items.php", "regional="+encode(regional));
	}
	
	@Override
	public void requestRegionalData(final String regional, final DatabaseCallback callback) {
		if (callback == null)
			return;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					callback.onMatchDataReceived(getRegionalData(regional));
				} catch (ClientProtocolException e) {
					callback.onDatabaseException(e);
				} catch (IOException e) {
					callback.onDatabaseException(e);
				}
			}
		});
	}
	
	public ArrayList<Match> getRows(int start, int limit) throws ClientProtocolException, IOException {
		return getMatches("get_items.php", "start="+encode(start-1)+"&limit="+encode(limit));
	}
	
	public void requestRows(final int start, final int limit, final DatabaseCallback callback) {
		if (callback == null)
			return;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					callback.onMatchDataReceived(getRows(start, limit));
				} catch (ClientProtocolException e) {
					callback.onDatabaseException(e);
				} catch (IOException e) {
					callback.onDatabaseException(e);
				}
			}
		});
	}
	
	private ArrayList <Match> getMatches(String file, String parameters) throws ClientProtocolException, IOException {
		String data = getWebsiteData(file, parameters);
		ArrayList <Match> matches = new ArrayList<Match>();
		String [] entries = data.split("\n");
		for (int i = 1; i < entries.length; i++) {
			String [] columns = entries[i].split(",");
			if (columns.length < 25)
				continue;
			matches.add(parseMatch(columns));
		}
		return matches;
	}
	
	private String encode(Integer i) {
		return encode(i.toString());
	}
	
	private String encode(Float i) {
		return encode(i.toString());
	}
	
	private String encode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	private String getWebsiteData(String file, String parameters) throws ClientProtocolException, IOException {
		String url = HOST + file + "?" + parameters;
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
		match.setAutoScoredLow(Utilities.parseIntSafe(columns[startIndex+1]));
		match.setAutoScoredHigh(Utilities.parseIntSafe(columns[startIndex+2]));
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
