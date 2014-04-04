package com.team2502.scoutingapp;

import java.util.ArrayList;

import com.team2502.scoutingapp.data.DatabaseCallback;
import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.Team;
import com.team2502.scoutingapp.data.WebDatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TeamStatsActivity extends Activity implements DatabaseCallback, OnItemSelectedListener, OnEditorActionListener {
	
	private WebDatabase database;
	private Team team;
	private ArrayList <Match> matches;
	
	private Spinner regionalSpinner;
	private EditText teamSelector;
	
	private TextView regionalAutonomousAverage;
	private TextView regionalTeleoperatedAverage;
	private TextView worldwideAutonomousAverage;
	private TextView worldwideTeleoperatedAverage;
	
	private TextView regionalGoalie;
	private TextView regionalPasser;
	private TextView regionalCatcher;
	private TextView regionalLauncher;
	private TextView regionalDefense;
	private TextView regionalBroken;
	
	private TextView worldwideGoalie;
	private TextView worldwidePasser;
	private TextView worldwideCatcher;
	private TextView worldwideLauncher;
	private TextView worldwideDefense;
	private TextView worldwideBroken;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_statistics);
		int team = getIntent().getIntExtra("team", 0);
		
		database = new WebDatabase();
		this.team = new Team(team);
		database.requestTeamData(this.team, this);
		
		regionalSpinner = (Spinner)findViewById(R.id.regionalSelector);
		regionalSpinner.setOnItemSelectedListener(this);
		teamSelector = (EditText)findViewById(R.id.team_selection);
		teamSelector.setOnEditorActionListener(this);
		
		regionalAutonomousAverage = (TextView)findViewById(R.id.regional_autonomous_average);
		regionalTeleoperatedAverage = (TextView)findViewById(R.id.regional_teleoperated_average);
		worldwideAutonomousAverage = (TextView)findViewById(R.id.worldwide_autonomous_average);
		worldwideTeleoperatedAverage = (TextView)findViewById(R.id.worldwide_teleoperated_average);
		
		regionalGoalie = (TextView)findViewById(R.id.regional_strategy_goal);
		regionalPasser = (TextView)findViewById(R.id.regional_strategy_passer);
		regionalCatcher = (TextView)findViewById(R.id.regional_strategy_catcher);
		regionalLauncher = (TextView)findViewById(R.id.regional_strategy_launcher);
		regionalDefense = (TextView)findViewById(R.id.regional_strategy_defense);
		regionalBroken = (TextView)findViewById(R.id.regional_strategy_broken);

		worldwideGoalie = (TextView)findViewById(R.id.worldwide_strategy_goal);
		worldwidePasser = (TextView)findViewById(R.id.worldwide_strategy_passer);
		worldwideCatcher = (TextView)findViewById(R.id.worldwide_strategy_catcher);
		worldwideLauncher = (TextView)findViewById(R.id.worldwide_strategy_launcher);
		worldwideDefense = (TextView)findViewById(R.id.worldwide_strategy_defense);
		worldwideBroken = (TextView)findViewById(R.id.worldwide_strategy_broken);
		
		teamSelector.setText(Integer.toString(team));
	}
	
	@Override
	public void onMatchDataReceived(ArrayList<Match> matches) {
		this.matches = matches;
		setStrategies();
	}
	
	@Override
	public void onMatchDataAdded(Match match, boolean success) {
		
	}
	
	private void setStrategies() {
		ArrayList <String> regionals = new ArrayList<String>();
		double [] worldwideStrategies = new double[6];
		double autoAverage = 0;
		double teleopAverage = 0;
		int worldwideMatches = 0;
		for (Match m : matches) {
			if (!regionals.contains(m.getRegional()))
				regionals.add(m.getRegional());
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
		final double fAutoAverage = autoAverage / worldwideMatches;
		final double fTeleopAverage = teleopAverage / worldwideMatches;
		final ArrayList <String> fRegionals = regionals;
		final double [] fWorldwideStrategies = worldwideStrategies;
		runOnUiThread(new Runnable() {
			public void run() {
				ArrayAdapter<String> aa = new ArrayAdapter<String>(TeamStatsActivity.this, android.R.layout.simple_spinner_item, fRegionals);
				regionalSpinner.setAdapter(aa);
				worldwideAutonomousAverage.setText(String.format("%.1f pts", fAutoAverage));
				worldwideTeleoperatedAverage.setText(String.format("%.1f pts", fTeleopAverage));
				worldwideGoalie.setText(String.format("%.0f%%", fWorldwideStrategies[0]*100));
				worldwidePasser.setText(String.format("%.0f%%", fWorldwideStrategies[1]*100));
				worldwideCatcher.setText(String.format("%.0f%%", fWorldwideStrategies[2]*100));
				worldwideLauncher.setText(String.format("%.0f%%", fWorldwideStrategies[3]*100));
				worldwideDefense.setText(String.format("%.0f%%", fWorldwideStrategies[4]*100));
				worldwideBroken.setText(String.format("%.0f%%", fWorldwideStrategies[5]*100));
			}
		});
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		updateRegionalData((String)parent.getItemAtPosition(position));
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		regionalGoalie.setText("0%");
		regionalPasser.setText("0%");
		regionalCatcher.setText("0%");
		regionalLauncher.setText("0%");
		regionalDefense.setText("0%");
		regionalBroken.setText("0%");
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		int team = Utilities.parseIntSafe(teamSelector.getText().toString());
		if (team != 0 && team != this.team.getTeamNumber()) {
			this.team.setTeamNumber(team);
			database.requestTeamData(this.team, this);
		} else
			return false;
		return true;
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
		final double fAutoAverage = autoAverage / regionalMatches;
		final double fTeleopAverage = teleopAverage / regionalMatches;
		final double [] fRegionalStrategies = regionalStrategies;
		runOnUiThread(new Runnable() {
			public void run() {
				regionalAutonomousAverage.setText(String.format("%.1f pts", fAutoAverage));
				regionalTeleoperatedAverage.setText(String.format("%.1f pts", fTeleopAverage));
				regionalGoalie.setText(String.format("%.0f%%", fRegionalStrategies[0]*100));
				regionalPasser.setText(String.format("%.0f%%", fRegionalStrategies[1]*100));
				regionalCatcher.setText(String.format("%.0f%%", fRegionalStrategies[2]*100));
				regionalLauncher.setText(String.format("%.0f%%", fRegionalStrategies[3]*100));
				regionalDefense.setText(String.format("%.0f%%", fRegionalStrategies[4]*100));
				regionalBroken.setText(String.format("%.0f%%", fRegionalStrategies[5]*100));
			}
		});
	}

}
