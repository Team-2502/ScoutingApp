package com.team2502.scoutingapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import com.team2502.scoutingapp.data.DatabaseCallback;
import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.Team;
import com.team2502.scoutingapp.data.WebDatabase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TeamStatsActivity extends Activity implements DatabaseCallback, OnItemSelectedListener, TextWatcher {
	
	private WebDatabase database;
	private Team team;
	private ArrayList <Match> matches;
	private ArrayList <String> regionals;
	
	private ArrayAdapter<String> regionalAdapter;
	private ProgressBar teamLoading;
	private Spinner regionalSpinner;
	private EditText teamSelector;
	private TableLayout previousMatchesTable;
	
	private TextView regionalAutonomousAverage;
	private TextView regionalTeleoperatedAverage;
	private TextView worldwideAutonomousAverage;
	private TextView worldwideTeleoperatedAverage;
	
	private TextView [] regionalStrategiesTextView;
	private TextView [] worldwideStrategiesTextView;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_statistics);
		int team = getIntent().getIntExtra("team", 0);
		
		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		database = new WebDatabase();
		this.team = new Team(team);
		database.requestTeamData(this.team, this);
		
		regionals = new ArrayList<String>();
		regionalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regionals);
		teamLoading = (ProgressBar)findViewById(R.id.team_loading_progress);
		teamLoading.setVisibility(View.VISIBLE);
		regionalSpinner = (Spinner)findViewById(R.id.regionalSelector);
		regionalSpinner.setOnItemSelectedListener(this);
		regionalSpinner.setAdapter(regionalAdapter);
		teamSelector = (EditText)findViewById(R.id.team_selection);
		teamSelector.addTextChangedListener(this);
		previousMatchesTable = (TableLayout)findViewById(R.id.previous_matches_table);
		
		regionalAutonomousAverage = (TextView)findViewById(R.id.regional_autonomous_average);
		regionalTeleoperatedAverage = (TextView)findViewById(R.id.regional_teleoperated_average);
		worldwideAutonomousAverage = (TextView)findViewById(R.id.worldwide_autonomous_average);
		worldwideTeleoperatedAverage = (TextView)findViewById(R.id.worldwide_teleoperated_average);
		
		regionalStrategiesTextView = new TextView[6];
		regionalStrategiesTextView[0] = (TextView)findViewById(R.id.regional_strategy_goal);
		regionalStrategiesTextView[1] = (TextView)findViewById(R.id.regional_strategy_passer);
		regionalStrategiesTextView[2] = (TextView)findViewById(R.id.regional_strategy_catcher);
		regionalStrategiesTextView[3] = (TextView)findViewById(R.id.regional_strategy_launcher);
		regionalStrategiesTextView[4] = (TextView)findViewById(R.id.regional_strategy_defense);
		regionalStrategiesTextView[5] = (TextView)findViewById(R.id.regional_strategy_broken);
		
		worldwideStrategiesTextView = new TextView[6];
		worldwideStrategiesTextView[0] = (TextView)findViewById(R.id.worldwide_strategy_goal);
		worldwideStrategiesTextView[1] = (TextView)findViewById(R.id.worldwide_strategy_passer);
		worldwideStrategiesTextView[2] = (TextView)findViewById(R.id.worldwide_strategy_catcher);
		worldwideStrategiesTextView[3] = (TextView)findViewById(R.id.worldwide_strategy_launcher);
		worldwideStrategiesTextView[4] = (TextView)findViewById(R.id.worldwide_strategy_defense);
		worldwideStrategiesTextView[5] = (TextView)findViewById(R.id.worldwide_strategy_broken);
		
		teamSelector.setText(Integer.toString(team));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onMatchDataReceived(ArrayList<Match> matches) {
		this.matches = matches;
		runOnUiThread(new Runnable() {
			public void run() {
				teamLoading.setVisibility(View.INVISIBLE);
				if (TeamStatsActivity.this.matches.size() == 0)
					resetAll();
				else
					setStrategies();
			}
		});
	}
	
	@Override
	public void onMatchDataAdded(Match match, boolean success) {
		
	}
	
	@Override
	public void onDatabaseException(Exception exception) {
		
	}
	
	private void setStrategies() {
		Collections.sort(matches, new RegionalComparison());
		double [] worldwideStrategies = new double[6];
		double autoAverage = 0;
		double teleopAverage = 0;
		int worldwideMatches = 0;
		regionals.clear();
		previousMatchesTable.removeAllViews();
		addTableHeader();
		for (Match m : matches) {
			if (!regionals.contains(m.getRegional()))
				regionals.add(m.getRegional());
			addTableRow(m);
			worldwideMatches++;
			autoAverage += m.getAutonomousPoints();
			teleopAverage += m.getTeleoperatedPoints();
			worldwideStrategies[0] += m.isGoalie()?1:0;
			worldwideStrategies[1] += m.isPasser()?1:0;
			worldwideStrategies[2] += m.isCatcher()?1:0;
			worldwideStrategies[3] += m.isLauncher()?1:0;
			worldwideStrategies[4] += m.isDefense()?1:0;
			worldwideStrategies[5] += m.isBroken()?1:0;
		}
		for (int i = 0; i < 6; i++) {
			worldwideStrategies[i] /= worldwideMatches;
		}
		autoAverage /= worldwideMatches;
		teleopAverage /= worldwideMatches;
		regionalAdapter.notifyDataSetChanged();
		worldwideAutonomousAverage.setText(String.format("%.1f pts", autoAverage));
		worldwideTeleoperatedAverage.setText(String.format("%.1f pts", teleopAverage));
		for (int i = 0; i < 6; i++)
			worldwideStrategiesTextView[i].setText(String.format("%.0f%%", worldwideStrategies[i]*100));
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		updateRegionalData((String)parent.getItemAtPosition(position));
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		for (int i = 0; i < 6; i++)
			regionalStrategiesTextView[i].setText("0%");
		regionalAutonomousAverage.setText("0 pts");
		regionalTeleoperatedAverage.setText("0 pts");
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		int team = Utilities.parseIntSafe(teamSelector.getText().toString());
		if (team != 0 && team != TeamStatsActivity.this.team.getTeamNumber()) {
			TeamStatsActivity.this.team.setTeamNumber(team);
			teamLoading.setVisibility(View.VISIBLE);
			database.requestTeamData(TeamStatsActivity.this.team, TeamStatsActivity.this);
		}
	}
	
	private void updateRegionalData(String regional) {
		if (regional == null)
			return;
		double [] regionalStrategies = new double[6];
		double autoAverage = 0;
		double teleopAverage = 0;
		int regionalMatches = 0;
		for (Match m : matches) {
			if (regional == null) regional = m.getRegional();
			if (m.getRegional().equals(regional)) {
				regionalMatches++;
				autoAverage += m.getAutonomousPoints();
				teleopAverage += m.getTeleoperatedPoints();
				regionalStrategies[0] += m.isGoalie()?1:0;
				regionalStrategies[1] += m.isPasser()?1:0;
				regionalStrategies[2] += m.isCatcher()?1:0;
				regionalStrategies[3] += m.isLauncher()?1:0;
				regionalStrategies[4] += m.isDefense()?1:0;
				regionalStrategies[5] += m.isBroken()?1:0;
			}
		}
		for (int i = 0; i < 6; i++) {
			if (regionalMatches > 0)
				regionalStrategies[i] /= regionalMatches;
		}
		autoAverage /= regionalMatches;
		teleopAverage /= regionalMatches;
		regionalAutonomousAverage.setText(String.format("%.1f pts", autoAverage));
		regionalTeleoperatedAverage.setText(String.format("%.1f pts", teleopAverage));
		for (int i = 0; i < 6; i++)
			regionalStrategiesTextView[i].setText(String.format("%.0f%%", regionalStrategies[i]*100));
	}
	
	private void resetAll() {
		regionals.clear();
		regionalAdapter.notifyDataSetChanged();
		for (int i = 0; i < 6; i++) {
			regionalStrategiesTextView[i].setText("0%");
			worldwideStrategiesTextView[i].setText("0%");
		}
		regionalAutonomousAverage.setText("0 pts");
		regionalTeleoperatedAverage.setText("0 pts");
		worldwideAutonomousAverage.setText("0 pts");
		worldwideTeleoperatedAverage.setText("0 pts");
	}
	
	private void addTableHeader() {
		addTableRow("Match", "Regional", "Auto", "Teleop", "Total", "Notes");
	}
	
	private void addTableRow(Match m) {
		String matchNumber = String.format(Locale.US, "%s%d", m.getGameType().getShortName(), m.getMatchNumber());
		String autonomous = String.format(Locale.US, "%d", (int)m.getAutonomousPoints());
		String teleoperated = String.format(Locale.US, "%d", (int)m.getTeleoperatedPoints());
		String total = String.format(Locale.US, "%d", (int)(m.getAutonomousPoints() + m.getTeleoperatedPoints()));
		addTableRow(matchNumber, m.getRegional().trim(), autonomous, teleoperated, total, m.getNotes().trim());
	}
	
	private void addTableRow(String matchNumber, String regional, String autonomous, String teleoperated, String total, String notes) {
		TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.previous_matches_table_row, null);
		
		((TextView)row.findViewById(R.id.previous_match_number)).setText(matchNumber);
		((TextView)row.findViewById(R.id.previous_match_regional)).setText(regional);
		((TextView)row.findViewById(R.id.previous_match_autonomous)).setText(autonomous);
		((TextView)row.findViewById(R.id.previous_match_teleoperated)).setText(teleoperated);
		((TextView)row.findViewById(R.id.previous_match_total)).setText(total);
		((TextView)row.findViewById(R.id.previous_match_notes)).setText(notes);
		// Draw separator
		TextView tv = new TextView(this);
		tv.setBackgroundColor(Color.parseColor("#80808080"));
		tv.setHeight(2);
		previousMatchesTable.addView(row);
		previousMatchesTable.addView(tv);
		
		// If you use context menu it should be registered for each table row
		registerForContextMenu(row);
	}
	
	private class RegionalComparison implements Comparator<Match> {
		@Override
		public int compare(Match lhs, Match rhs) {
			return lhs.getRegional().compareTo(rhs.getRegional());
		}
	}
	
}
