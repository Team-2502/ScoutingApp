package com.team2502.scoutingapp.data;

import java.util.ArrayList;

public interface DatabaseCallback {
	
	public void onMatchDataReceived(ArrayList <Match> matches);
	public void onMatchDataAdded(Match match, boolean success);
	public void onDatabaseException(Exception exception);
	
}
