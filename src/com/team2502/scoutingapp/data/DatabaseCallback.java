package com.team2502.scoutingapp.data;

import java.util.Map;

public interface DatabaseCallback {
	
	public void onMatchDataReceived(Map <String, Match> matches);
	public void onMatchDataAdded(Match match, boolean success);
	
}
