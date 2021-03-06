package com.team2502.scoutingapp.data;

import java.security.InvalidParameterException;

public class Match {
	
	public enum GameType {
		PRACTICE("Practice", "P"), QUALIFICATION("Qualification", "Q"),
		ELIMINATION("Elimination", "E"), INVALID("Invalid", "I");
		
		private String name;
		private String shortName;
		
		GameType(String name, String shortName) {
			this.name = name;	
			this.shortName = shortName;
		}
		
		public String getName() {
			return name;
		}
		
		public String getShortName() {
			return shortName;
		}

		public String toString() {
			return name;
		}
	}
	
	private String entryTimestamp;
	private String regional;
	private Team team;
	private int matchNumber;
	private GameType gameType;
	
	//Autonomus
	private boolean autoMoved;
	private int autoScoredLow;
	private int autoScoredHigh;
	private boolean autoScoredHot;

	//Teleop
	private int offGround;
	private int assistsStarted;
	private int assistsReceived;
	private int secAssistsStarted;
	private int secAssistsReceived;
	private int scoredLow;
	private int scoredHigh;
	private int overTruss;
	private int fromTruss;

	//Final
	private boolean goalie;
	private boolean passer;
	private boolean catcher;
	private boolean launcher;
	private boolean defense;
	private boolean broken;
	private float rating;

	private String notes;
	
	/**
	 * @return the timestamp of match entry
	 */
	public String getEntryTimestamp() {
		return entryTimestamp;
	}
	
	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}
	
	/**
	 * @return the regional
	 */
	public String getRegional() {
		return regional;
	}

	/**
	 * @return the match number
	 */
	public int getMatchNumber() {
		return matchNumber;
	}

	/**
	 * @return the game type
	 */
	public GameType getGameType() {
		return gameType;
	}

	/**
	 * @return true if moved during autonomous period
	 */
	public boolean isAutoMoved() {
		return autoMoved;
	}

	/**
	 * @return true if scored in the low goal during autonomous period
	 */
	public int getAutoScoredLow() {
		return autoScoredLow;
	}

	/**
	 * @return true if scored in the high goal during autonomous period
	 */
	public int getAutoScoredHigh() {
		return autoScoredHigh;
	}

	/**
	 * @return true if scored while the goal was hot during autonomous period
	 */
	public boolean isAutoScoredHot() {
		return autoScoredHot;
	}

	/**
	 * @return the number of balls picked up off the ground
	 */
	public int getOffGround() {
		return offGround;
	}

	/**
	 * @return the number of assists started
	 */
	public int getAssistsStarted() {
		return assistsStarted;
	}

	/**
	 * @return the number of assists received
	 */
	public int getAssistsReceived() {
		return assistsReceived;
	}

	/**
	 * @return the number of secondary assists started
	 */
	public int getSecAssistsStarted() {
		return secAssistsStarted;
	}

	/**
	 * @return the number of secondary assists received
	 */
	public int getSecAssistsReceived() {
		return secAssistsReceived;
	}

	/**
	 * @return the number of goals scored in he low goal
	 */
	public int getScoredLow() {
		return scoredLow;
	}

	/**
	 * @return the number of goals scored in the high goal
	 */
	public int getScoredHigh() {
		return scoredHigh;
	}

	/**
	 * @return the number of balls thrown over the truss
	 */
	public int getOverTruss() {
		return overTruss;
	}

	/**
	 * @return the number of balls caught over the truss
	 */
	public int getFromTruss() {
		return fromTruss;
	}

	/**
	 * @return true if the robot is a goalie
	 */
	public boolean isGoalie() {
		return goalie;
	}

	/**
	 * @return true if the robot is a passer
	 */
	public boolean isPasser() {
		return passer;
	}

	/**
	 * @return true if the robot is a catcher
	 */
	public boolean isCatcher() {
		return catcher;
	}

	/**
	 * @return true if the robot is a launcher
	 */
	public boolean isLauncher() {
		return launcher;
	}

	/**
	 * @return true if the robot is defensive
	 */
	public boolean isDefense() {
		return defense;
	}

	/**
	 * @return true if the robot is broken
	 */
	public boolean isBroken() {
		return broken;
	}

	/**
	 * @return the rating of the robot (0-1)
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @return the notes of the match
	 */
	public String getNotes() {
		return notes;
	}
	
	/**
	 * @param timestamp the timestamp of match entry to set
	 */
	public void setEntryTimestamp(String entryTimestamp) {
		this.entryTimestamp = entryTimestamp;
	}
	
	/**
	 * @param team the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}
	
	/**
	 * @param regional the regional to set
	 */
	public void setRegional(String regional) {
		this.regional = regional;
	}
	
	/**
	 * @param matchNumber the matchNumber to set
	 */
	public void setMatchNumber(int matchNumber) {
		if (matchNumber < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.matchNumber = matchNumber;
	}

	/**
	 * @param gameType the gameType to set
	 */
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	/**
	 * @param autoMoved the autoMoved to set
	 */
	public void setAutoMoved(boolean autoMoved) {
		this.autoMoved = autoMoved;
	}

	/**
	 * @param autoScoredLow the autoScoredLow to set
	 */
	public void setAutoScoredLow(int autoScoredLow) {
		this.autoScoredLow = autoScoredLow;
	}

	/**
	 * @param autoScoredHigh the autoScoredHigh to set
	 */
	public void setAutoScoredHigh(int autoScoredHigh) {
		this.autoScoredHigh = autoScoredHigh;
	}

	/**
	 * @param autoScoredHot the autoScoredHot to set
	 */
	public void setAutoScoredHot(boolean autoScoredHot) {
		this.autoScoredHot = autoScoredHot;
	}

	/**
	 * @param offGround the offGround to set
	 */
	public void setOffGround(int offGround) {
		if (offGround < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.offGround = offGround;
	}

	/**
	 * @param assistsStarted the assistsStarted to set
	 */
	public void setAssistsStarted(int assistsStarted) {
		if (assistsStarted < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.assistsStarted = assistsStarted;
	}

	/**
	 * @param assistsReceived the assistsReceived to set
	 */
	public void setAssistsReceived(int assistsReceived) {
		if (assistsReceived < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.assistsReceived = assistsReceived;
	}

	/**
	 * @param secAssistsStarted the secAssistsStarted to set
	 */
	public void setSecAssistsStarted(int secAssistsStarted) {
		if (secAssistsStarted < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.secAssistsStarted = secAssistsStarted;
	}

	/**
	 * @param secAssistsReceived the secAssistsReceived to set
	 */
	public void setSecAssistsReceived(int secAssistsReceived) {
		if (secAssistsReceived < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.secAssistsReceived = secAssistsReceived;
	}

	/**
	 * @param scoredLow the scoredLow to set
	 */
	public void setScoredLow(int scoredLow) {
		if (scoredLow < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.scoredLow = scoredLow;
	}

	/**
	 * @param scoredHigh the scoredHigh to set
	 */
	public void setScoredHigh(int scoredHigh) {
		if (scoredHigh < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.scoredHigh = scoredHigh;
	}

	/**
	 * @param overTruss the overTruss to set
	 */
	public void setOverTruss(int overTruss) {
		if (overTruss < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.overTruss = overTruss;
	}

	/**
	 * @param fromTruss the fromTruss to set
	 */
	public void setFromTruss(int fromTruss) {
		if (fromTruss < 0)
			throw new InvalidParameterException("Value must be non-negative");
		this.fromTruss = fromTruss;
	}

	/**
	 * @param goalie the goalie to set
	 */
	public void setGoalie(boolean goalie) {
		this.goalie = goalie;
	}

	/**
	 * @param passer the passer to set
	 */
	public void setPasser(boolean passer) {
		this.passer = passer;
	}

	/**
	 * @param catcher the catcher to set
	 */
	public void setCatcher(boolean catcher) {
		this.catcher = catcher;
	}

	/**
	 * @param launcher the launcher to set
	 */
	public void setLauncher(boolean launcher) {
		this.launcher = launcher;
	}

	/**
	 * @param defense the defense to set
	 */
	public void setDefense(boolean defense) {
		this.defense = defense;
	}

	/**
	 * @param broken the broken to set
	 */
	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		if (rating < 0 || rating > 5)
			throw new InvalidParameterException("Value must be between 0 and 5");
		this.rating = rating;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	/**
	 * Calculates the number of autonomous points scored
	 * by this team during this match
	 * @return the number of autonomous points scored
	 */
	public double getAutonomousPoints() {
		double points = 0;
		points += 6*getAutoScoredLow() + 15*getAutoScoredHigh();
		if (isAutoScoredHot())
			points += 5 * (getAutoScoredLow() + getAutoScoredHigh());
		if (isAutoMoved())
			points += 5;
		return points;
	}
	
	/**
	 * Calculates the number of teleoperated points scored
	 * by this team during this match
	 * @return the number of teleoperated points scored
	 */
	public double getTeleoperatedPoints() {
		double points = 0;
		points += 10 * getOverTruss();
		points += 10 * getFromTruss();
		points += getScoredLow();
		points += 10 * getScoredHigh();
		points += 5 * getAssistsStarted();
		points += 5 * getAssistsReceived();
		points += 15 * getSecAssistsStarted();
		points += 15 * getSecAssistsReceived();
		return points;
	}

	/**
	 * @return human-readable summary of the match
	 */
	@Override
	public String toString() {
		String summary = "Regional: " + getRegional() + "\n";
		summary += "Team: " + getTeam().getTeamNumber() + "\n";
		summary += "Match #: " + getMatchNumber() + "\n";
		summary += "Match Type: " + getGameType() + "\n\n";
		summary += "Moved: " + (isAutoMoved() ? "Yes" : "No") + "\n";
		summary += "Scored Low: " + getAutoScoredLow() + "\n";
		summary += "Scored High: " + getAutoScoredHigh() + "\n";
		summary += "Scored Hot: " + (isAutoScoredHot() ? "Yes" : "No") + "\n\n";
		summary += "Strategy: " + (isGoalie() ? "Goalie " : "") + (isPasser() ? "Passer " : "") + (isCatcher() ? "Catcher " : "") + (isLauncher() ? "Launcher " : "") + (isDefense() ? "Defense " : "") + (isBroken() ? "Broken " : "")  + "\n";
		summary += "Picked Off Ground: " + getOffGround() + "\n";
		summary += "Assists Initiated: " + getAssistsStarted() + "\n";
		summary += "Assists Acquired: " + getAssistsReceived() + "\n";
		summary += "Second Assists Initiated: " + getSecAssistsStarted() + "\n";
		summary += "Second Assists Acquired: " + getSecAssistsReceived() + "\n";
		summary += "Low Goals: " + getScoredLow() + "\n";
		summary += "High Goals: " + getScoredHigh() + "\n";
		summary += "Thrown Over Truss: " + getOverTruss() + "\n";
		summary += "Caught Over Truss: " + getFromTruss() + "\n\n";
		summary += "Rating: " + getRating() + "\n";
		summary += "Notes: " + getNotes();
		return summary;
	}

}
