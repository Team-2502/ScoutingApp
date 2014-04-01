package com.team2502.scoutingapp.data;

public class CellAddress {
	public final int row;
	public final int col;
	public final String idString;
	
	/**
	 * Constructs a CellAddress representing the specified {@code row} and
	 * {@code col}. The idString will be set in 'RnCn' notation.
	 */
	public CellAddress(int row, int col) {
		this.row = row;
		this.col = col;
		this.idString = String.format("R%sC%s", row, col);
	}
}
