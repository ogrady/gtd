package backend.playingfield;

import java.awt.Dimension;
import java.util.ArrayList;

import listener.ITowerListener;

import org.newdawn.slick.geom.Vector2f;

import backend.object.tower.Tower;

public class PlayingFieldMetaCell implements ITowerListener {
	public static final Dimension DEFAULT_CELL_SIZE = new Dimension(40, 40);

	private final Dimension size;
	private final GridCoordinate coordinate;
	private boolean walkable;
	private boolean buildable;
	private GroundType groundtype;
	private final ArrayList<CellAttribute> attributes;
	private Tower tower;

	public Tower getTower() {
		return this.tower;
	}

	public void setTower(final Tower t) {
		if (this.tower != null) {
			this.tower.getListeners().unregisterListener(this);
		}
		if (t != null) {
			t.getListeners().registerListener(this);
		}
		this.tower = t;
	}

	public boolean isWalkable() {
		return this.walkable;
		// return this.attributes.contains(CellAttribute.UNWALKABLE);
	}

	public void setWalkable(final boolean w) {
		this.walkable = w;
	}

	public boolean isBuildable() {
		return this.buildable;
		// return this.attributes.contains(CellAttribute.UNBUILDABLE);
	}

	public void setBuildable(final boolean b) {
		this.buildable = b;
	}

	// not used yet
	public GroundType getGroundType() {
		return this.groundtype;
	}

	public void setGroundType(final GroundType type) {
		this.groundtype = type;
	}

	// not used yet
	public ArrayList<CellAttribute> getCellAttributes() {
		return this.attributes;
	}

	public GridCoordinate getCoordinate() {
		return this.coordinate;
	}

	public int getWidth() {
		return this.size.width;
	}

	public int getHeight() {
		return this.size.height;
	}

	public Vector2f getPosition() {
		final int x = this.coordinate.getColumn() * this.getWidth();
		final int y = this.coordinate.getRow() * this.getHeight();
		return new Vector2f(x, y);
	}

	public Vector2f getCenterPosition() {
		final int x = this.coordinate.getColumn() * this.getWidth()
				+ this.getWidth() / 2;
		final int y = this.coordinate.getRow() * this.getHeight()
				+ this.getHeight() / 2;
		return new Vector2f(x, y);
	}

	public PlayingFieldMetaCell(final GridCoordinate coord, final Dimension size) {
		this.attributes = new ArrayList<CellAttribute>();
		this.coordinate = coord;
		this.size = size;
		this.buildable = true;
		this.walkable = false;
	}

	@Override
	public String toString() {
		return String.format("(cell-%1$s (%2$dx%3$d))",
				this.coordinate.toString(), this.size.width, this.size.height);
	}

	@Override
	public void onUpgrade(final Tower t, final int lvl) {

	}

	@Override
	public void onSell(final Tower t, final int revenues) {
		this.setTower(null);
	}
}
