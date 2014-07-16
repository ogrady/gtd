package backend.playingfield;

/**
 * A 2D-coordinate within the {@link PlayingField}
 * 
 * @author Daniel
 * 
 */
public class GridCoordinate {
	private final int row;
	private final int column;
	private PlayingField field;

	/**
	 * The {@link PlayingField} the coordinate lies on
	 * 
	 * @return the {@link PlayingField} the coordinate lies on
	 */
	public PlayingField getField() {
		return this.field;
	}

	/**
	 * @return the x-part of the coordinate
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * @return the y-part of the coordinate
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * Constructor
	 * 
	 * @param field
	 *            {@link PlayingField} this coordinate lies on
	 * @param column
	 *            y-part of the coordinate
	 * @param row
	 *            x-part of the coordinate
	 */
	public GridCoordinate(final PlayingField field, final int column,
			final int row) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Constructor
	 * 
	 * @param field
	 *            {@link PlayingField} this coordinate lies on
	 * @param column
	 *            y-part of the coordinate
	 * @param row
	 *            x-part of the coordinate
	 */
	public GridCoordinate(final PlayingField field, final float column,
			final float row) {
		this(field, (int) column, (int) row);
	}

	@Override
	public String toString() {
		return String.format("[c:%1$d|r:%2$d]", this.column, this.row);
	}
}
