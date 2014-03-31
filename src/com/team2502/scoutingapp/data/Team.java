package com.team2502.scoutingapp.data;

import java.security.InvalidParameterException;

public class Team {
	
	private int teamNumber;

	/**
	 * @return the team number
	 */
	public int getTeamNumber() {
		return teamNumber;
	}

	/**
	 * @param teamNumber the teamNumber to set
	 */
	public void setTeamNumber(int teamNumber) {
		if (teamNumber < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.teamNumber = teamNumber;
	}
	
}
