package com.team2502.scoutingapp;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.team2502.scoutingapp.data.LocalWebDatabase;
import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.Team;

import android.annotation.SuppressLint;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MatchPredictionActivity extends Activity implements TextWatcher, OnClickListener {
	
	private ArrayList <Match> matches;
	
	private EditText red1Team;
	private EditText red2Team;
	private EditText red3Team;
	private EditText blue1Team;
	private EditText blue2Team;
	private EditText blue3Team;
	
	private TextView red1Points;
	private TextView red2Points;
	private TextView red3Points;
	private TextView blue1Points;
	private TextView blue2Points;
	private TextView blue3Points;
	private TextView redTotalPoints;
	private TextView blueTotalPoints;
	
	private TextView redHeader;
	private TextView blueHeader;
	
	private Button reset;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_prediction);
		
		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		matches = new ArrayList<Match>();
		
		red1Team = (EditText)findViewById(R.id.predict_red_1_team);
		red2Team = (EditText)findViewById(R.id.predict_red_2_team);
		red3Team = (EditText)findViewById(R.id.predict_red_3_team);
		blue1Team = (EditText)findViewById(R.id.predict_blue_1_team);
		blue2Team = (EditText)findViewById(R.id.predict_blue_2_team);
		blue3Team = (EditText)findViewById(R.id.predict_blue_3_team);
		
		red1Team.addTextChangedListener(this);
		red2Team.addTextChangedListener(this);
		red3Team.addTextChangedListener(this);
		blue1Team.addTextChangedListener(this);
		blue2Team.addTextChangedListener(this);
		blue3Team.addTextChangedListener(this);
		
		red1Points = (TextView)findViewById(R.id.match_predict_red_1_points);
		red2Points = (TextView)findViewById(R.id.match_predict_red_2_points);
		red3Points = (TextView)findViewById(R.id.match_predict_red_3_points);
		blue1Points = (TextView)findViewById(R.id.match_predict_blue_1_points);
		blue2Points = (TextView)findViewById(R.id.match_predict_blue_2_points);
		blue3Points = (TextView)findViewById(R.id.match_predict_blue_3_points);
		redTotalPoints = (TextView)findViewById(R.id.match_predict_red_total_points);
		blueTotalPoints = (TextView)findViewById(R.id.match_predict_blue_total_points);
		
		redHeader = (TextView)findViewById(R.id.predict_red_header);
		blueHeader = (TextView)findViewById(R.id.predict_blue_header);
		redHeader.setBackgroundColor(Color.RED);
		blueHeader.setBackgroundColor(Color.BLUE);
		
		reset = (Button)findViewById(R.id.reset_teams);
		reset.setOnClickListener(this);
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
	
	@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
	@Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
	@Override public void afterTextChanged(Editable s) {
		updatePredictions();
	}
	
	@Override
	public void onClick(View v) {
		red1Team.setText("");
		red2Team.setText("");
		red3Team.setText("");
		blue1Team.setText("");
		blue2Team.setText("");
		blue3Team.setText("");
		red1Points.setText("");
		red2Points.setText("");
		red3Points.setText("");
		blue1Points.setText("");
		blue2Points.setText("");
		blue3Points.setText("");
		redTotalPoints.setText("");
		blueTotalPoints.setText("");
		redTotalPoints.setBackgroundColor(Color.TRANSPARENT);
		blueTotalPoints.setBackgroundColor(Color.TRANSPARENT);
	}
	
	private void updateData() {
		Executors.newSingleThreadExecutor().execute(new Runnable() { public void run() {
			LocalWebDatabase localWebData = new LocalWebDatabase(MatchPredictionActivity.this);
			localWebData.update();
			matches = localWebData.getAllMatches();
			localWebData.close();
			updatePredictions();
		}});
	}
	
	private void updatePredictions() {
		final Team red1 = new Team(Utilities.parseIntSafe(red1Team.getEditableText().toString()));
		final Team red2 = new Team(Utilities.parseIntSafe(red2Team.getEditableText().toString()));
		final Team red3 = new Team(Utilities.parseIntSafe(red3Team.getEditableText().toString()));
		final Team blue1 = new Team(Utilities.parseIntSafe(blue1Team.getEditableText().toString()));
		final Team blue2 = new Team(Utilities.parseIntSafe(blue2Team.getEditableText().toString()));
		final Team blue3 = new Team(Utilities.parseIntSafe(blue3Team.getEditableText().toString()));
		if (red1.getTeamNumber() == 0 || red2.getTeamNumber() == 0 || red3.getTeamNumber() == 0)
			return;
		if (blue1.getTeamNumber() == 0 || blue2.getTeamNumber() == 0 || blue3.getTeamNumber() == 0)
			return;
		Executors.newSingleThreadExecutor().execute(new Runnable() { public void run() {
			double red1Average = 0, red2Average = 0, red3Average = 0;
			double blue1Average = 0, blue2Average = 0, blue3Average = 0;
			int red1Matches = 0, red2Matches = 0, red3Matches = 0;
			int blue1Matches = 0, blue2Matches = 0, blue3Matches = 0;
			for (Match m : matches) {
				if (m.getTeam().equals(red1)) {
					red1Average += m.getAutonomousPoints() + m.getTeleoperatedPoints();
					red1Matches++;
				}
				if (m.getTeam().equals(red2)) {
					red2Average += m.getAutonomousPoints() + m.getTeleoperatedPoints();
					red2Matches++;
				}
				if (m.getTeam().equals(red3)) {
					red3Average += m.getAutonomousPoints() + m.getTeleoperatedPoints();
					red3Matches++;
				}
				if (m.getTeam().equals(blue1)) {
					blue1Average += m.getAutonomousPoints() + m.getTeleoperatedPoints();
					blue1Matches++;
				}
				if (m.getTeam().equals(blue2)) {
					blue2Average += m.getAutonomousPoints() + m.getTeleoperatedPoints();
					blue2Matches++;
				}
				if (m.getTeam().equals(blue3)) {
					blue3Average += m.getAutonomousPoints() + m.getTeleoperatedPoints();
					blue3Matches++;
				}
			}
			if (red1Matches > 0) red1Average /= red1Matches;
			if (red2Matches > 0) red2Average /= red2Matches;
			if (red3Matches > 0) red3Average /= red3Matches;
			if (blue1Matches > 0) blue1Average /= blue1Matches;
			if (blue2Matches > 0) blue2Average /= blue2Matches;
			if (blue3Matches > 0) blue3Average /= blue3Matches;
			updateTextViews(red1Average, red2Average, red3Average, blue1Average, blue2Average, blue3Average);
		}});
	}
	
	private void updateTextViews(final double red1, final double red2, final double red3, final double blue1, final double blue2, final double blue3) {
		runOnUiThread(new Runnable() { public void run() {
			double totalRed = red1+red2+red3;
			double totalBlue = blue1+blue2+blue3;
			red1Points.setText(String.format("%.0f", red1));
			red2Points.setText(String.format("%.0f", red2));
			red3Points.setText(String.format("%.0f", red3));
			blue1Points.setText(String.format("%.0f", blue1));
			blue2Points.setText(String.format("%.0f", blue2));
			blue3Points.setText(String.format("%.0f", blue3));
			redTotalPoints.setText(String.format("%.0f", totalRed));
			blueTotalPoints.setText(String.format("%.0f", totalBlue));
			redTotalPoints.setBackgroundColor(Color.TRANSPARENT);
			blueTotalPoints.setBackgroundColor(Color.TRANSPARENT);
			if (totalRed > totalBlue)
				redTotalPoints.setBackgroundColor(Color.GREEN);
			else if (totalRed == totalBlue) {
				redTotalPoints.setBackgroundColor(Color.YELLOW);
				blueTotalPoints.setBackgroundColor(Color.YELLOW);
			} else
				blueTotalPoints.setBackgroundColor(Color.GREEN);
		}});
	}
	
}
