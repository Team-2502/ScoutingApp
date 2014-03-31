package com.team2502.scoutingapp;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.Team;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RatingBar;
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

	public static class AutonomousSectionFragment extends Fragment {
		private View inflatedView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_section_autonomous, container, false);
			this.inflatedView = view;
			return view;
		}

		public View getInflatedView() {
			return inflatedView;
		}

		public Match inputMatchData(Match match) {
			Team team = new Team();
			team.setTeamNumber(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.teamNumber)).getEditableText().toString()));
			match.setTeam(team);
			match.setMatchNumber(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.matchNumber)).getEditableText().toString()));

			match.setAutoMoved(((Checkable)getInflatedView().findViewById(R.id.movedSwitch)).isChecked());
			match.setAutoScoredLow(((Checkable)getInflatedView().findViewById(R.id.scoredLowSwitch)).isChecked());
			match.setAutoScoredHigh(((Checkable)getInflatedView().findViewById(R.id.scoredHighSwitch)).isChecked());
			match.setAutoScoredHot(((Checkable)getInflatedView().findViewById(R.id.scoredHotSwitch)).isChecked());
			return match;
		}

		public void reset() {
			((EditText)getInflatedView().findViewById(R.id.teamNumber)).setText("");
			((EditText)getInflatedView().findViewById(R.id.matchNumber)).setText("");

			((Checkable)getInflatedView().findViewById(R.id.movedSwitch)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.scoredLowSwitch)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.scoredHighSwitch)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.scoredHotSwitch)).setChecked(false);
		}
	}

	public static class TeleopSectionFragment extends Fragment implements OnClickListener {
		private SparseArrayCompat<EditText> buttons = new SparseArrayCompat<EditText>();
		private View inflatedView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_section_teleop, container, false);
			buttons.put(R.id.addGround, (EditText) view.findViewById(R.id.groundCount));
			buttons.put(R.id.addStarted, (EditText) view.findViewById(R.id.startedCount));
			buttons.put(R.id.addReceived, (EditText) view.findViewById(R.id.receivedCount));
			buttons.put(R.id.addSecStarted, (EditText) view.findViewById(R.id.secStartedCount));
			buttons.put(R.id.addSecReceived, (EditText) view.findViewById(R.id.secReceivedCount));
			buttons.put(R.id.addHigh, (EditText) view.findViewById(R.id.scoredHighCount));
			buttons.put(R.id.addLow, (EditText) view.findViewById(R.id.scoredLowCount));
			buttons.put(R.id.addOverTruss, (EditText) view.findViewById(R.id.overTrussCount));
			buttons.put(R.id.addFromTruss, (EditText) view.findViewById(R.id.fromTrussCount));
			for(int i = 0; i < buttons.size(); i++) {
				((Button) view.findViewById(buttons.keyAt(i))).setOnClickListener(this);
			}
			this.inflatedView = view;
			return view;
		}

		public View getInflatedView() {
			return inflatedView;
		}

		@Override
		public void onClick(View v) {
			EditText countText = buttons.get(v.getId());
			try {
				int count = (int) Double.parseDouble(countText.getText().toString());
				count++;
				countText.setText(String.valueOf(count));
			} catch (NumberFormatException e) {
				countText.setText("0");
			}
		}

		public Match inputMatchData(Match match) {
			match.setOffGround(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.groundCount)).getEditableText().toString()));
			match.setAssistsStarted(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.startedCount)).getEditableText().toString()));
			match.setAssistsReceived(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.receivedCount)).getEditableText().toString()));
			match.setSecAssistsStarted(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.secStartedCount)).getEditableText().toString()));
			match.setSecAssistsReceived(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.secReceivedCount)).getEditableText().toString()));
			match.setScoredLow(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.scoredLowCount)).getEditableText().toString()));
			match.setScoredHigh(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.scoredHighCount)).getEditableText().toString()));
			match.setOverTruss(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.overTrussCount)).getEditableText().toString()));
			match.setFromTruss(Integer.parseInt(((EditText)getInflatedView().findViewById(R.id.fromTrussCount)).getEditableText().toString()));
			return match;
		}

		public void reset() {
			((EditText)getInflatedView().findViewById(R.id.groundCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.startedCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.receivedCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.secStartedCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.secReceivedCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.scoredLowCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.scoredHighCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.overTrussCount)).setText("0");
			((EditText)getInflatedView().findViewById(R.id.fromTrussCount)).setText("0");
		}
	}

	public static class FinalizeSectionFragment extends Fragment implements OnClickListener {
		private SparseArrayCompat<CheckedTextView> checkboxes = new SparseArrayCompat<CheckedTextView>();
		private View inflatedView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_section_finalize, container, false);
			checkboxes.put(R.id.goalieCheck, (CheckedTextView) view.findViewById(R.id.goalieCheck));
			checkboxes.put(R.id.passerCheck, (CheckedTextView) view.findViewById(R.id.passerCheck));
			checkboxes.put(R.id.catcherCheck, (CheckedTextView) view.findViewById(R.id.catcherCheck));
			checkboxes.put(R.id.launcherCheck, (CheckedTextView) view.findViewById(R.id.launcherCheck));
			checkboxes.put(R.id.defenseCheck, (CheckedTextView) view.findViewById(R.id.defenseCheck));
			checkboxes.put(R.id.brokenCheck, (CheckedTextView) view.findViewById(R.id.brokenCheck));
			for(int i = 0; i < checkboxes.size(); i++) {
				checkboxes.valueAt(i).setOnClickListener(this);
			}
			view.findViewById(R.id.submitButton).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity)getActivity()).submit();
				}
			});
			view.findViewById(R.id.resetButton).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity)getActivity()).reset();
				}
			});
			this.inflatedView = view;
			return view;
		}

		public View getInflatedView() {
			return inflatedView;
		}

		@Override
		public void onClick(View v) {
			if (!(v instanceof CheckedTextView))
				return;
			((CheckedTextView)v).toggle();
		}

		public Match inputMatchData(Match match) {
			match.setGoalie(((Checkable)getInflatedView().findViewById(R.id.goalieCheck)).isChecked());
			match.setPasser(((Checkable)getInflatedView().findViewById(R.id.passerCheck)).isChecked());
			match.setCatcher(((Checkable)getInflatedView().findViewById(R.id.catcherCheck)).isChecked());
			match.setLauncher(((Checkable)getInflatedView().findViewById(R.id.launcherCheck)).isChecked());
			match.setDefense(((Checkable)getInflatedView().findViewById(R.id.defenseCheck)).isChecked());
			match.setBroken(((Checkable)getInflatedView().findViewById(R.id.brokenCheck)).isChecked());

			match.setRating(((RatingBar)getInflatedView().findViewById(R.id.robotRating)).getRating());
			return match;
		}

		public void reset() {
			((Checkable)getInflatedView().findViewById(R.id.goalieCheck)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.passerCheck)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.catcherCheck)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.launcherCheck)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.defenseCheck)).setChecked(false);
			((Checkable)getInflatedView().findViewById(R.id.brokenCheck)).setChecked(false);

			((RatingBar)getInflatedView().findViewById(R.id.robotRating)).setRating(0.0F);
		}

	}
}
