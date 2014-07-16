package backend.object;

import listener.IPlayStateListener;

import org.newdawn.slick.geom.Vector2f;

public abstract class Mobile extends Positionable implements IPlayStateListener {
	protected float speed;
	protected float traveledDistance;

	public float getTraveledDistance() {
		return this.traveledDistance;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(final float speed) {
		this.speed = speed;
	}

	public Mobile(final float x, final float y, final float speed) {
		this(new Vector2f(x, y), speed);
	}

	public Mobile(final Vector2f pos, final float speed) {
		super(pos);
		this.speed = speed;
	}

	@Override
	public void setPosition(final Vector2f newPos) {
		this.position = newPos;
	}

	public void moveTowards(final int x, final int y) {
		this.moveTowards(new Vector2f(x, y));
	}

	public boolean moveTowards(final Vector2f destination) {
		boolean reached = false;
		final Vector2f vec = destination.copy().sub(this.position);
		if (vec.length() > this.speed) {
			vec.normalise().scale(this.speed);
			this.position.add(vec);
			this.traveledDistance += vec.length();
		} else {
			this.traveledDistance += this.position.distance(destination);
			this.position = destination.copy();
			reached = true;
		}
		return reached;
	}
}
