package com.team2502.scoutingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.team2502.scoutingapp.data.Match;

public class TeleopSectionFragment extends Fragment implements OnClickListener {
	private SparseArrayCompat<EditText> buttons = new SparseArrayCompat<EditText>();
	private EditText groundCountText;
	private EditText startedCountText;
	private EditText receivedCountText;
	private EditText secStartedCountText;
	private EditText secReceivedCountText;
	private EditText scoredHighCountText;
	private EditText scoredLowCountText;
	private EditText overTrussCountText;
	private EditText fromTrussCountText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_teleop, container, false);
		groundCountText = (EditText) view.findViewById(R.id.groundCount);
		startedCountText = (EditText) view.findViewById(R.id.startedCount);
		receivedCountText = (EditText) view.findViewById(R.id.receivedCount);
		secStartedCountText = (EditText) view.findViewById(R.id.secStartedCount);
		secReceivedCountText = (EditText) view.findViewById(R.id.secReceivedCount);
		scoredHighCountText = (EditText) view.findViewById(R.id.scoredHighCount);
		scoredLowCountText = (EditText) view.findViewById(R.id.scoredLowCount);
		overTrussCountText = (EditText) view.findViewById(R.id.overTrussCount);
		fromTrussCountText = (EditText) view.findViewById(R.id.fromTrussCount);
		buttons.put(R.id.addGround, groundCountText);
		buttons.put(R.id.addStarted, startedCountText);
		buttons.put(R.id.addReceived, receivedCountText);
		buttons.put(R.id.addSecStarted, secStartedCountText);
		buttons.put(R.id.addSecReceived, secReceivedCountText);
		buttons.put(R.id.addHigh, scoredHighCountText);
		buttons.put(R.id.addLow, scoredLowCountText);
		buttons.put(R.id.addOverTruss, overTrussCountText);
		buttons.put(R.id.addFromTruss, fromTrussCountText);
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
			if (count > 10)
				count = 10;
			countText.setText(String.valueOf(count));
		} catch (NumberFormatException e) {
			countText.setText("0");
		}
	}
	
	public Match inputMatchData(Match match) {
		match.setOffGround(Integer.parseInt(groundCountText.getEditableText().toString()));
		match.setAssistsStarted(Integer.parseInt(startedCountText.getEditableText().toString()));
		match.setAssistsReceived(Integer.parseInt(receivedCountText.getEditableText().toString()));
		match.setSecAssistsStarted(Integer.parseInt(secStartedCountText.getEditableText().toString()));
		match.setSecAssistsReceived(Integer.parseInt(secReceivedCountText.getEditableText().toString()));
		match.setScoredLow(Integer.parseInt(scoredLowCountText.getEditableText().toString()));
		match.setScoredHigh(Integer.parseInt(scoredHighCountText.getEditableText().toString()));
		match.setOverTruss(Integer.parseInt(overTrussCountText.getEditableText().toString()));
		match.setFromTruss(Integer.parseInt(fromTrussCountText.getEditableText().toString()));
		if (match.getAssistsStarted() < 0 || match.getAssistsStarted() > 10)
			return null;
		if (match.getAssistsReceived() < 0 || match.getAssistsReceived() > 10)
			return null;
		if (match.getSecAssistsStarted() < 0 || match.getSecAssistsStarted() > 10)
			return null;
		if (match.getSecAssistsReceived() < 0 || match.getSecAssistsReceived() > 10)
			return null;
		if (match.getOverTruss() < 0 || match.getOverTruss() > 10)
			return null;
		if (match.getFromTruss() < 0 || match.getFromTruss() > 10)
			return null;
		if (match.getOffGround() < 0 || match.getOffGround() > 10)
			return null;
		return match;
	}

	public void reset() {
		groundCountText.setText("0");
		startedCountText.setText("0");
		receivedCountText.setText("0");
		secStartedCountText.setText("0");
		secReceivedCountText.setText("0");
		scoredLowCountText.setText("0");
		scoredHighCountText.setText("0");
		overTrussCountText.setText("0");
		fromTrussCountText.setText("0");
	}
}
