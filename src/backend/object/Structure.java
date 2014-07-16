package backend.object;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import backend.playingfield.GridCoordinate;
import backend.playingfield.PlayingField;
import backend.playingfield.PlayingFieldMetaCell;

abstract public class Structure extends Positionable {
	protected PlayingFieldMetaCell standOn;
	protected PlayingField field;

	public PlayingFieldMetaCell getStandOn() {
		return this.standOn;
	}

	public Structure(final PlayingField field, final Vector2f pos) {
		super(pos.copy());
		this.field = field;
		this.standOn = field.getCellAt(field.pointToCoordinate(pos.copy()));
	}

	public Structure(final PlayingField field, final GridCoordinate coord) {
		super(field.topLeftPointOf(coord));
		this.field = field;
		this.standOn = field.getCellAt(coord);
	}

	public Structure(final PlayingField field, final float x, final float y) {
		this(field, new Vector2f(x, y));
	}

	@Override
	public String toString() {
		return String.format("%1$s %2$s", this.getClass().getSimpleName(),
				this.standOn);
	}

	@Override
	public int getRepresentationWidth() {
		return ((Image) this.representation).getWidth();
	}

	@Override
	public int getRepresentationHeight() {
		return ((Image) this.representation).getHeight();
	}
}
