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
			if (count > 10)
				count = 10;
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
