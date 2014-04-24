package com.team2502.scoutingapp;

import java.security.InvalidParameterException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Spinner;

import com.team2502.scoutingapp.data.Match;
import com.team2502.scoutingapp.data.Match.GameType;
import com.team2502.scoutingapp.data.Team;

public class AutonomousSectionFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	private SparseArrayCompat<EditText> buttons = new SparseArrayCompat<EditText>();

	private Spinner competitionSpinner;
	private EditText teamNumberText;
	private EditText matchNumberText;
	private Spinner gameTypeSpinner;
	private EditText scoredLowText;
	private EditText scoredHighText;
	private Checkable movedSwitch;
	private Checkable scoredHotSwitch;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_autonomous, container, false);

		teamNumberText = (EditText) view.findViewById(R.id.teamNumber);
		matchNumberText = (EditText) view.findViewById(R.id.matchNumber);
		scoredLowText = (EditText) view.findViewById(R.id.scoredLowCount);
		scoredHighText = (EditText) view.findViewById(R.id.scoredHighCount);
		movedSwitch = (Checkable) view.findViewById(R.id.movedSwitch);
		scoredHotSwitch = (Checkable) view.findViewById(R.id.scoredHotSwitch);

		buttons.put(R.id.addScoredLow, scoredLowText);
		buttons.put(R.id.addScoredHigh, scoredHighText);

		gameTypeSpinner = (Spinner) view.findViewById(R.id.gametypeSelector);
		ArrayAdapter<CharSequence> gameTypeAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.gamemodes_array, android.R.layout.simple_spinner_item);
		gameTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gameTypeSpinner.setAdapter(gameTypeAdapter);

		competitionSpinner = (Spinner) view.findViewById(R.id.eventName);
		ArrayAdapter<CharSequence> competitionAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.competitions, android.R.layout.simple_spinner_item);
		competitionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		competitionSpinner.setAdapter(competitionAdapter);
		competitionSpinner.setOnItemSelectedListener(this);

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
			if (count > 3)
				count = 3;
			countText.setText(String.valueOf(count));
		} catch (NumberFormatException e) {
			countText.setText("0");
		}
	}

	public Match inputMatchData(Match match) {
		String [] competitions = getResources().getStringArray(R.array.competitions);
		long competitionId = competitionSpinner.getSelectedItemId();
		if (competitionId < 0 || competitionId >= competitions.length)
			throw new InvalidParameterException("Competition ID must be between 0 and " + competitions.length);
		String val = competitions[(int)competitionId];
		match.setRegional(val);
		Team team = new Team();
		team.setTeamNumber(Integer.parseInt(teamNumberText.getEditableText().toString()));
		match.setTeam(team);
		match.setMatchNumber(Integer.parseInt(matchNumberText.getEditableText().toString()));
		switch(gameTypeSpinner.getSelectedItemPosition()) {
		case 0:
			match.setGameType(GameType.PRACTICE);
			break;
		case 1:
			match.setGameType(GameType.QUALIFICATION);
			break;
		case 2:
			match.setGameType(GameType.ELIMINATION);
			break;
		}
		int low = Utilities.parseIntSafe(scoredLowText.getEditableText().toString());
		int high = Utilities.parseIntSafe(scoredHighText.getEditableText().toString());
		if (low > 3 || low < 0)
			throw new InvalidParameterException("Low goal score must be between 0 and 3");
		if (high > 3 || high < 0)
			throw new InvalidParameterException("High goal score must be between 0 and 3");
		match.setAutoMoved(movedSwitch.isChecked());
		match.setAutoScoredHot(scoredHotSwitch.isChecked());
		match.setAutoScoredLow(low);
		match.setAutoScoredHigh(high);
		return match;
	}

	public void reset() {
		teamNumberText.setText("");
		matchNumberText.setText("");

		movedSwitch.setChecked(false);
		scoredHotSwitch.setChecked(false);
		scoredLowText.setText(R.string.default_counter_value);
		scoredHighText.setText(R.string.default_counter_value);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
