package com.team2502.scoutingapp.data;

public interface Database {
	
	/**
	 * Adds a match to this database
	 * @param match the match to add
	 */
	public void addMatchData(final Match match);
	
	/**
	 * Adds a match to this database
	 * @param match the match to add
	 * @param the callback that will be told the result of the addition
	 */
	public void addMatchData(final Match match, DatabaseCallback callback);
	
	/**
	 * Requests to get the match data for a team at a regional
	 * @param team the team
	 * @param regional the regional
	 * @param callback the callback that will be given the data
	 */
	public void requestTeamData(Team team, String regional, DatabaseCallback callback);
	
	/**
	 * Requests to get all match data for a team
	 * @param team the team
	 * @param callback the callback that will be given the data
	 */
	public void requestTeamData(Team team, DatabaseCallback callback);
	
	/**
	 * Requests to get all match data at a regional
	 * @param regional the regional
	 * @param callback the callback that will be given the data
	 */
	public void requestRegionalData(String regional, DatabaseCallback callback);
	
	/**
	 * Requests to get the first 100 rows of match data
	 * @param callback the callback that will be given the data
	 */
	public void requestRows(DatabaseCallback callback);
	
	/**
	 * Requests to get a variable amount of rows of match data
	 * @param start the starting row
	 * @param end the ending row
	 * @param callback the callback that will be given the data
	 */
	public void requestRows(int start, int end, DatabaseCallback callback);
	
}
