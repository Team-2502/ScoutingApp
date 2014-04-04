package com.team2502.scoutingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	
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
