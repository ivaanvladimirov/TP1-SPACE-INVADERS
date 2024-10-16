package tp1.logic;

/**
 *  Container class for position
 *  Provides methods for simple interactions with positions.
 */
public class Position {

	// final instance attributes
	public final int col;
	public final int row;

	/**
	 * Position constructor
	 * @param col the x-axis position
	 * @param row the y-axis position
	 */
	public Position(int col, int row) {
		this.col = col;
		this.row = row;
	}

	/**
	 * Return new position based on the current position and Move
	 * @param move the direction of movement
	 * @return the updated position
	 */
	public Position move(Move move) {
		return new Position(
				col + move.getX(),
				row + move.getY()
		);
	}

	/**
	 * Compare two positions
	 * @param other position
	 * @return true if equal, false otherwise
	 */
	public boolean equals(Position other) {
		return col == other.col && row == other.row;
	}

	/**
	 * Compare position with col and row as integers
	 * @param col x-axis
	 * @param row y-axis
	 * @return true if equal, false otherwise
	 */
	public boolean equals(int col, int row) {
		return this.equals(new Position(col, row));
	}

	/**
	 * Check if current position is located in specified row
	 * @param row the row to be checked
	 * @return true if is in row, false otherwise
	 */
	public boolean inRow(int row) { return this.row == row; }

	/**
	 * @return String representative of this position
	 */
	@Override
	public String toString() {
		return "col: "+ col + "| row: " + row;
	}

}
