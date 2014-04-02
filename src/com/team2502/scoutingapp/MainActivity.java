package com.team2502.scoutingapp;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Map;

import com.team2502.scoutingapp.data.LocalDatabase;
import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.Team;
import com.team2502.scoutingapp.data.WebDatabase;
import com.team2502.scoutingapp.data.Match.GameType;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

	public void submit() {
		//		View autoView = ((AutonomousSectionFragment)appSectionsPagerAdapter.getItem(0)).getInflatedView();
		//		View teleopView = ((TeleopSectionFragment)appSectionsPagerAdapter.getItem(1)).getInflatedView();
		//		View finalizeView = ((FinalizeSectionFragment)appSectionsPagerAdapter.getItem(2)).getInflatedView();

		Match match = new Match();
		Team team = new Team();
		try {
			((AutonomousSectionFragment)appSectionsPagerAdapter.getItem(0)).inputMatchData(match);
			//			team.setTeamNumber(Integer.parseInt(((EditText)autoView.findViewById(R.id.teamNumber)).getEditableText().toString()));
			//			match.setTeam(team);
			//			match.setMatchNumber(Integer.parseInt(((EditText)autoView.findViewById(R.id.matchNumber)).getEditableText().toString()));
			//
			//			match.setAutoMoved(((Checkable)autoView.findViewById(R.id.movedSwitch)).isChecked());
			//			match.setAutoScoredLow(((Checkable)autoView.findViewById(R.id.scoredLowSwitch)).isChecked());
			//			match.setAutoScoredHigh(((Checkable)autoView.findViewById(R.id.scoredHighSwitch)).isChecked());
			//			match.setAutoScoredHot(((Checkable)autoView.findViewById(R.id.scoredHotSwitch)).isChecked());

			((TeleopSectionFragment)appSectionsPagerAdapter.getItem(1)).inputMatchData(match);
			//			match.setOffGround(Integer.parseInt(((EditText)teleopView.findViewById(R.id.groundCount)).getEditableText().toString()));
			//			match.setAssistsStarted(Integer.parseInt(((EditText)teleopView.findViewById(R.id.startedCount)).getEditableText().toString()));
			//			match.setAssistsReceived(Integer.parseInt(((EditText)teleopView.findViewById(R.id.receivedCount)).getEditableText().toString()));
			//			match.setSecAssistsStarted(Integer.parseInt(((EditText)teleopView.findViewById(R.id.secStartedCount)).getEditableText().toString()));
			//			match.setSecAssistsReceived(Integer.parseInt(((EditText)teleopView.findViewById(R.id.secReceivedCount)).getEditableText().toString()));
			//			match.setScoredLow(Integer.parseInt(((EditText)teleopView.findViewById(R.id.scoredLowCount)).getEditableText().toString()));
			//			match.setScoredHigh(Integer.parseInt(((EditText)teleopView.findViewById(R.id.scoredHighCount)).getEditableText().toString()));
			//			match.setOverTruss(Integer.parseInt(((EditText)teleopView.findViewById(R.id.overTrussCount)).getEditableText().toString()));
			//			match.setFromTruss(Integer.parseInt(((EditText)teleopView.findViewById(R.id.fromTrussCount)).getEditableText().toString()));

			((FinalizeSectionFragment)appSectionsPagerAdapter.getItem(2)).inputMatchData(match);
			//			match.setGoalie(((Checkable)finalizeView.findViewById(R.id.goalieCheck)).isChecked());
			//			match.setPasser(((Checkable)finalizeView.findViewById(R.id.passerCheck)).isChecked());
			//			match.setCatcher(((Checkable)finalizeView.findViewById(R.id.catcherCheck)).isChecked());
			//			match.setLauncher(((Checkable)finalizeView.findViewById(R.id.launcherCheck)).isChecked());
			//			match.setDefense(((Checkable)finalizeView.findViewById(R.id.defenseCheck)).isChecked());
			//			match.setBroken(((Checkable)finalizeView.findViewById(R.id.brokenCheck)).isChecked());
			//
			//			match.setRating(((RatingBar)finalizeView.findViewById(R.id.ratingBar1)).getRating());

			match.setNotes(((EditText)findViewById(R.id.notesBox)).getEditableText().toString());
			matches.add(match);

			Intent send = new Intent(android.content.Intent.ACTION_SEND);
			send.setType("text/plain");
			send.putExtra(android.content.Intent.EXTRA_TEXT, match.toString());

			startActivity(Intent.createChooser(send, "Send Using"));

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
	
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
		
		private AutonomousSectionFragment autonomousSection;
		private TeleopSectionFragment teleopSection;
		private FinalizeSectionFragment finalizeSection;

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			autonomousSection = new AutonomousSectionFragment();
			teleopSection =  new TeleopSectionFragment();
			finalizeSection =  new FinalizeSectionFragment();
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return autonomousSection;
			case 1:
				return teleopSection;
			case 2:
				return finalizeSection;
			}
			return new Fragment();
		}

		@Override
		public int getCount() {
			return 3;
		}

	}
	
}
