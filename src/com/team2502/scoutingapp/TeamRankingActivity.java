package com.team2502.scoutingapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.Executors;

import com.team2502.scoutingapp.data.LocalDatabase;
import com.team2502.scoutingapp.data.LocalWebDatabase;
import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.PreferencesDatabase;
import com.team2502.scoutingapp.data.Team;
import com.team2502.scoutingapp.data.WebDatabase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TeamRankingActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private ArrayList <TeamScore> teams = new ArrayList<TeamScore>();
	private ArrayList <Match> matches = new ArrayList<Match>();
	
	private ArrayList<String> regionals;
	private ArrayAdapter<String> regionalAdapter;
	private ProgressBar teamLoading;
	private Spinner regionalSpinner;
	private TableLayout rankingTable;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_rankings);
		
		regionals = new ArrayList<String>();
		regionalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regionals);
		teamLoading = (ProgressBar)findViewById(R.id.team_loading_progress);
		teamLoading.setVisibility(View.VISIBLE);
		regionalSpinner = (Spinner)findViewById(R.id.regionalSelector);
		regionalSpinner.setOnItemSelectedListener(this);
		regionalSpinner.setAdapter(regionalAdapter);
		rankingTable = (TableLayout)findViewById(R.id.team_ranking_table);
		
		regionals.add("");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateData();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		matches.clear();
		teams.clear();
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() != R.id.team_ranking_table_row)
			return;
		String teamStr = ((TextView)v.findViewById(R.id.ranking_team_number)).getText().toString();
		int team = Utilities.parseIntSafe(teamStr);
		if (team != 0) {
			Intent i = new Intent(this, TeamStatsActivity.class);
			i.putExtra("team", team);
			startActivity(i);
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		reloadAll((String)parent.getItemAtPosition(position));
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		reloadAll("");
	}
	
	private void reloadAll(String regional) {
		teamLoading.setVisibility(View.VISIBLE);
		synchronized (teams) {
    		teams.clear();
		}
		for (Match m : matches) {
    		matchAdded(m, regional);
    	}
		synchronized (teams) {
			Collections.sort(teams, new ScoredComparison());
		}
		updateTable();
	}
	
	private void updateData() {
		Executors.newSingleThreadExecutor().execute(new Runnable() { public void run() {
			runOnUiThread(new Runnable() { public void run() {
				teamLoading.setVisibility(View.VISIBLE);
			}});
			LocalWebDatabase localWebData = new LocalWebDatabase(TeamRankingActivity.this);
			localWebData.update();
			matches = localWebData.getAllMatches();
			synchronized (teams) {
				teams.clear();
				for (Match m : matches) {
					matchAdded(m, "");
				}
				Collections.sort(teams, new ScoredComparison());
			}
			localWebData.close();
			updateTable();
		}});
	}
	
	private void matchAdded(Match m, String regionalFilter) {
		if (!regionals.contains(m.getRegional())) {
			regionals.add(m.getRegional());
			runOnUiThread(new Runnable() { public void run() {
				regionalAdapter.notifyDataSetChanged();
			}});
		}
		if (regionalFilter.length() == 0 || regionalFilter.equalsIgnoreCase(m.getRegional())) {
    		TeamScore ts = new TeamScore(m.getTeam());
    		int index = Collections.binarySearch(teams, ts);
    		if (index < 0) {
    			index = teams.size();
    			ts.addMatch(m);
    			teams.add(ts);
    			Collections.sort(teams);
    		} else {
    			teams.get(index).addMatch(m);
    		}
		}
	}
	
	private void updateTable() {
		// Safety mechanism
		if (Looper.getMainLooper().getThread() != Thread.currentThread())
			runOnUiThread(new Runnable() { public void run() { updateTable(); }});
		else {
			rankingTable.removeAllViews();
			addTableHeader();
			for (int rank = 1; rank <= teams.size(); rank++) {
				addTableRow(rank, teams.get(rank-1));
			}
			teamLoading.setVisibility(View.INVISIBLE);
		}
	}
	
	private void addTableHeader() {
		addTableRow("Rank", "Team", "Score", "Avg Points", "Matches");
	}
	
	private void addTableRow(int rank, TeamScore team) {
		String rankStr = Integer.toString(rank);
		String teamStr = Integer.toString(team.getTeamNumber());
		String scoreStr = String.format(Locale.US, "%.2f", team.getScore());
		String avgPointsStr = String.format(Locale.US, "%.1f pts", team.getAveragePoints());
		String matchesStr = Integer.toString(team.getMatchCount());
		addTableRow(rankStr, teamStr, scoreStr, avgPointsStr, matchesStr);
	}
	
	private void addTableRow(String rank, String team, String score, String points, String matches) {
		TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.team_ranking_table_row, null);
		
		((TextView)row.findViewById(R.id.ranking_id)).setText(rank);
		((TextView)row.findViewById(R.id.ranking_team_number)).setText(team);
		((TextView)row.findViewById(R.id.ranking_total_score)).setText(score);
		((TextView)row.findViewById(R.id.ranking_average_points)).setText(points);
		((TextView)row.findViewById(R.id.ranking_recorded_matches)).setText(matches);
		row.setOnClickListener(this);
		// Draw separator
		TextView tv = new TextView(this);
		tv.setBackgroundColor(Color.parseColor("#80808080"));
		tv.setHeight(2);
		rankingTable.addView(row);
		rankingTable.addView(tv);
		
		// If you use context menu it should be registered for each table row
		registerForContextMenu(row);
	}
	
	private class ScoredComparison implements Comparator<TeamScore> {
		@Override
		public int compare(TeamScore lhs, TeamScore rhs) {
			int compareScore = Double.valueOf(rhs.getScore()).compareTo(lhs.getScore());
			if (compareScore == 0) {
				if (lhs.getAveragePoints() == rhs.getAveragePoints())
					return Integer.valueOf(lhs.getTeamNumber()).compareTo(rhs.getTeamNumber());
				else
					return Double.valueOf(rhs.getAveragePoints()).compareTo(lhs.getAveragePoints());
			}
			return compareScore;
		}
	}
	
	private class TeamScore implements Comparable <TeamScore> {
		private final Team team;
		private int sumPoints;
		private int matchCount;
		private int scoreLow;
		private int scoreHigh;
		private int truss;
		private int assist;
		
		public TeamScore(Team team) {
			this.team = team;
			sumPoints = 0;
			matchCount = 0;
			scoreLow = 0;
			scoreHigh = 0;
			truss = 0;
			assist = 0;
		}
		
		public int getTeamNumber() {
			return team.getTeamNumber();
		}
		
		public double getScore() {
			double score = 0;
			score += 4   * (scoreHigh / (double)matchCount);
			score += 3.5 * (assist / (double)matchCount);
			score += 1.5 * (truss / (double)matchCount);
			score += 1   * (scoreLow / (double)matchCount);
			return score;
		}
		
		public double getAveragePoints() {
			return sumPoints / (double)matchCount;
		}
		
		public int getMatchCount() {
			return matchCount;
		}
		
		public void addMatch(Match m) {
			sumPoints += m.getAutonomousPoints() + m.getTeleoperatedPoints();
			scoreLow += m.getScoredLow();
			scoreHigh += m.getScoredHigh();
			truss += m.getOverTruss();
			assist += m.getAssistsReceived() + m.getAssistsStarted();
			assist += m.getSecAssistsReceived() + m.getSecAssistsStarted();
			matchCount++;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof TeamScore)
				return ((TeamScore)o).getTeamNumber() == team.getTeamNumber();
			if (o instanceof Integer)
				return ((Integer)o).intValue() == team.getTeamNumber();
			if (o instanceof Team)
				return ((Team)o).getTeamNumber() == team.getTeamNumber();
			return false;
		}

		@Override
		public int compareTo(TeamScore another) {
			int myTeam = getTeamNumber();
			int anotherTeam = another.getTeamNumber();
			if (myTeam == anotherTeam)
				return 0;
			return myTeam > anotherTeam ? -1 : 1;
		}
	}
	
}
