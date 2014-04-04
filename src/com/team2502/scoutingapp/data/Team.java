package com.team2502.scoutingapp.data;

import java.security.InvalidParameterException;

public class Team {
	
	private int teamNumber;
	
	public Team() {
		this(0);
	}
	
	public Team(int teamNumber) {
		this.teamNumber = teamNumber;
	}

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
	
	@Override
	public String toString() {
		return "Team #" + teamNumber;
	}
	
	@Override
	public int hashCode() {
		return teamNumber;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof Integer)
			return teamNumber == (Integer)o;
		if (o instanceof Team)
			return teamNumber == ((Team)o).teamNumber;
		return false;
	}
	
}
