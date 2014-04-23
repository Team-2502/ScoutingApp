package com.team2502.scoutingapp;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import com.team2502.scoutingapp.data.LocalDatabase;
import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.WebDatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements TabListener {
	
	private AppSectionsPagerAdapter appSectionsPagerAdapter;
	private ViewPager viewPager;
	public ArrayList<Match> matches = new ArrayList<Match>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(appSectionsPagerAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		actionBar.addTab(actionBar.newTab().setText(R.string.autonomous).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.teleop).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.finalize).setTabListener(this));
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {

	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {

	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setNegativeButton(R.string.no, null).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.get_team_stats: {
				Intent i = new Intent(MainActivity.this, TeamStatsActivity.class);
				i.putExtra("team", 0);
				startActivity(i);
				return true;
			}
			case R.id.get_team_ranking: {
				Intent i = new Intent(MainActivity.this, TeamRankingActivity.class);
				startActivity(i);
				return true;
			}
			case R.id.get_match_prediction: {
				Intent i = new Intent(MainActivity.this, MatchPredictionActivity.class);
				startActivity(i);
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void submit() {
		AutonomousSectionFragment autoView = (AutonomousSectionFragment)appSectionsPagerAdapter.getItem(0);
		TeleopSectionFragment teleopView = (TeleopSectionFragment)appSectionsPagerAdapter.getItem(1);
		FinalizeSectionFragment finalizeView = (FinalizeSectionFragment)appSectionsPagerAdapter.getItem(2);
		
		Match match = new Match();
		try {
			match.setNotes(((EditText)findViewById(R.id.notesBox)).getEditableText().toString());
			match = autoView.inputMatchData(match);
			if (match == null)
				throw new InvalidParameterException();
			match = teleopView.inputMatchData(match);
			if (match == null)
				throw new InvalidParameterException();
			match = finalizeView.inputMatchData(match);
			if (match == null)
				throw new InvalidParameterException();
			matches.add(match);
			onMatchSubmitted(match);
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(), R.string.error_bad_fields, Toast.LENGTH_LONG).show();
		} catch (InvalidParameterException e) {
			Toast.makeText(getApplicationContext(), R.string.error_bad_fields, Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			Toast.makeText(getApplicationContext(), "Something went wrong... blame Jackson", Toast.LENGTH_LONG).show();
		}
	}
	
	public void reset() {
		viewPager.setCurrentItem(0);
		((AutonomousSectionFragment)appSectionsPagerAdapter.getItem(0)).reset();
		((TeleopSectionFragment)appSectionsPagerAdapter.getItem(1)).reset();
		((FinalizeSectionFragment)appSectionsPagerAdapter.getItem(2)).reset();
		((EditText)findViewById(R.id.notesBox)).setText("");
	}
	
	private void onMatchSubmitted(Match match) {
		match.setEntryTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(System.currentTimeMillis()));
		// Sends to databases
		new WebDatabase().addMatchData(match);
		new LocalDatabase(this).addMatchData(match);
		// Sends via whatever user chooses
		Intent send = new Intent(android.content.Intent.ACTION_SEND);
		send.setType("text/plain");
		send.putExtra(android.content.Intent.EXTRA_TEXT, match.toString());
		startActivity(Intent.createChooser(send, "Send Using"));
	}
	
}
