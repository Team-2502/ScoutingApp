package com.team2502.scoutingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.RatingBar;

import com.team2502.scoutingapp.data.Match;

public class FinalizeSectionFragment extends Fragment implements OnClickListener {
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
