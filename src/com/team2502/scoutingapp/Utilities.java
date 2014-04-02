package com.team2502.scoutingapp;

public class Utilities {
	
	public static final int parseIntSafe(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static final float parseFloatSafe(String str) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
}
