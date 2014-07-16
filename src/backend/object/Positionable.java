package backend.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import util.IRenderable;
import util.IRenderer;
import frontend.renderer.PositionableDefaultRenderer;

/**
 * Positionables are entities that are placed on the playing-field at a certain
 * position.<br>
 * They are also rennderable and by default are just rendered at their current
 * position, using the representation.
 * 
 * @author Daniel
 * 
 */
abstract public class Positionable implements IRenderable {
	protected Vector2f position;
	protected Renderable representation;
	protected IRenderer renderer;

	/**
	 * @return the hitbox used for collision-detection (by default, that's an
	 *         rectangle, using the position and the size of the representation)
	 */
	public Shape getHitbox() {
		return new Rectangle(this.position.x, this.position.y,
				this.getRepresentationWidth(), this.getRepresentationHeight());
	}

	/**
	 * @return the width of the representation in pixels
	 */
	abstract public int getRepresentationWidth();

	/**
	 * @return the height of the representation in pixels
	 */
	abstract public int getRepresentationHeight();

	@Override
	public IRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public void setRenderer(final IRenderer ren) {
		this.renderer = ren;
	}

	/**
	 * @return the current position of the {@link Positionable}
	 */
	public Vector2f getPosition() {
		return this.position;
	}

	/**
	 * @param newPos
	 *            the new position
	 */
	public void setPosition(final Vector2f newPos) {
		this.position = newPos;
	}

	/**
	 * @return the center point of the representation, relative to the current
	 *         position
	 */
	public Vector2f getCenterPosition() {
		final Vector2f pos = this.position.copy();
		pos.x += this.getRepresentationWidth() / 2;
		pos.y += this.getRepresentationHeight() / 2;
		return pos;
	}

	/**
	 * Constructor
	 * 
	 * @param pos
	 *            initial position
	 */
	public Positionable(final Vector2f pos) {
		this.setPosition(pos);
		this.renderer = new PositionableDefaultRenderer(this);
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 *            x-portion for the initial position
	 * @param y
	 *            y-portion for the initial position
	 */
	public Positionable(final float x, final float y) {
		this(new Vector2f(x, y));
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		this.renderer.render(gc, g);
	}

	@Override
	public Renderable getRenderableRepresentation() {
		return this.representation;
	}
}
