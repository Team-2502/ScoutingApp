package com.team2502.scoutingapp;

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
import android.widget.CheckedTextView;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity implements TabListener{

	private AppSectionsPagerAdapter appSectionsPagerAdapter;
	private ViewPager viewPager;

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
	
	public void addToTeleopCount(View v) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new AutonomousSectionFragment();
			case 1:
				return new TeleopSectionFragment();
			case 2:
				return new FinalizeSectionFragment();
			}
			return new Fragment();
		}
		@Override
		public int getCount() {
			return 3;
		}

	}

	public static class AutonomousSectionFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_section_autonomous, container, false);
			return view;
		}
	}
	
	public static class TeleopSectionFragment extends Fragment implements OnClickListener {
		private SparseArrayCompat<EditText> buttons = new SparseArrayCompat<EditText>();
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
			return view;
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
	}
	
	public static class FinalizeSectionFragment extends Fragment implements OnClickListener {
		private SparseArrayCompat<CheckedTextView> checkboxes = new SparseArrayCompat<CheckedTextView>();
		
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
			return view;
		}
		
		@Override
		public void onClick(View v) {
			if (!(v instanceof CheckedTextView))
				return;
			((CheckedTextView)v).toggle();
		}
		
	}
	
}
