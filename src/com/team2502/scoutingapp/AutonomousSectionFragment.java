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
	private View inflatedView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_autonomous, container, false);
		buttons.put(R.id.addScoredLow, (EditText) view.findViewById(R.id.scoredLowCount));
		buttons.put(R.id.addScoredHigh, (EditText) view.findViewById(R.id.scoredHighCount));
		Spinner gameTypeSpinner = (Spinner) view.findViewById(R.id.gametypeSelector);
		ArrayAdapter<CharSequence> gameTypeAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.gamemodes_array, android.R.layout.simple_spinner_item);
		gameTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gameTypeSpinner.setAdapter(gameTypeAdapter);
		Spinner competitionSpinner = (Spinner) view.findViewById(R.id.eventName);
		ArrayAdapter<CharSequence> competitionAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.competitions, android.R.layout.simple_spinner_item);
		competitionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		competitionSpinner.setAdapter(competitionAdapter);
		competitionSpinner.setOnItemSelectedListener(this);
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
			if (count > 3)
				count = 3;
			countText.setText(String.valueOf(count));
		} catch (NumberFormatException e) {
			countText.setText("0");
		}
	}
	
	public Match inputMatchData(Match match) {
		String [] competitions = getResources().getStringArray(R.array.competitions);
		long competitionId = ((Spinner)getInflatedView().findViewById(R.id.eventName)).getSelectedItemId();
		if (competitionId < 0 || competitionId >= competitions.length)
			throw new InvalidParameterException("Competition ID must be between 0 and " + competitions.length);
		String val = competitions[(int)competitionId];
		match.setRegional(val);
		Team team = new Team();
		team.setTeamNumber(Integer.parseInt(((EditText) getInflatedView().findViewById(R.id.teamNumber)).getEditableText().toString()));
		match.setTeam(team);
		match.setMatchNumber(Integer.parseInt(((EditText) getInflatedView().findViewById(R.id.matchNumber)).getEditableText().toString()));
		switch(((Spinner) getInflatedView().findViewById(R.id.gametypeSelector)).getSelectedItemPosition()) {
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
		int low = Utilities.parseIntSafe(((EditText)getInflatedView().findViewById(R.id.scoredLowCount)).getEditableText().toString());
		int high = Utilities.parseIntSafe(((EditText)getInflatedView().findViewById(R.id.scoredHighCount)).getEditableText().toString());
		if (low > 3 || low < 0)
			return null;
		if (high > 3 || high < 0)
			return null;
		match.setAutoMoved(((Checkable) getInflatedView().findViewById(R.id.movedSwitch)).isChecked());
		match.setAutoScoredHot(((Checkable) getInflatedView().findViewById(R.id.scoredHotSwitch)).isChecked());
		match.setAutoScoredLow(low);
		match.setAutoScoredHigh(high);
		return match;
	}
	
	public void reset() {
		((EditText) getInflatedView().findViewById(R.id.teamNumber)).setText("");
		((EditText) getInflatedView().findViewById(R.id.matchNumber)).setText("");
		((Spinner) getInflatedView().findViewById(R.id.gametypeSelector)).setSelection(0);
		
		((Checkable) getInflatedView().findViewById(R.id.movedSwitch)).setChecked(false);
		((Checkable) getInflatedView().findViewById(R.id.scoredHotSwitch)).setChecked(false);
		((EditText) getInflatedView().findViewById(R.id.scoredLowCount)).setText(R.string.default_counter_value);
		((EditText) getInflatedView().findViewById(R.id.scoredHighCount)).setText(R.string.default_counter_value);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
