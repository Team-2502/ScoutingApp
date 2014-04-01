package com.team2502.scoutingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.EditText;

import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.Team;

public class AutonomousSectionFragment extends Fragment {
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
		team.setTeamNumber(Integer.parseInt(((EditText) getInflatedView().findViewById(R.id.teamNumber)).getEditableText().toString()));
		match.setTeam(team);
		match.setMatchNumber(Integer.parseInt(((EditText) getInflatedView().findViewById(R.id.matchNumber)).getEditableText().toString()));
		
		match.setAutoMoved(((Checkable) getInflatedView().findViewById(R.id.movedSwitch)).isChecked());
		match.setAutoScoredLow(((Checkable) getInflatedView().findViewById(R.id.scoredLowSwitch)).isChecked());
		match.setAutoScoredHigh(((Checkable) getInflatedView().findViewById(R.id.scoredHighSwitch)).isChecked());
		match.setAutoScoredHot(((Checkable) getInflatedView().findViewById(R.id.scoredHotSwitch)).isChecked());
		return match;
	}
	
	public void reset() {
		((EditText) getInflatedView().findViewById(R.id.teamNumber)).setText("");
		((EditText) getInflatedView().findViewById(R.id.matchNumber)).setText("");
		
		((Checkable) getInflatedView().findViewById(R.id.movedSwitch)).setChecked(false);
		((Checkable) getInflatedView().findViewById(R.id.scoredLowSwitch)).setChecked(false);
		((Checkable) getInflatedView().findViewById(R.id.scoredHighSwitch)).setChecked(false);
		((Checkable) getInflatedView().findViewById(R.id.scoredHotSwitch)).setChecked(false);
	}
}
