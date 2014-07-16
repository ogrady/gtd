package frontend.renderer;

import game.GoodTowerDefenseGame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import util.IRenderable;
import util.IRenderer;
import backend.object.monster.Monster;

/**
 * The default renderer to display monsters centered at their current position.
 * 
 * @author Daniel
 * 
 */
public class MonsterDefaultRenderer implements IRenderer {
	protected Monster mon;

	/**
	 * Constructor
	 * 
	 * @param mon
	 *            the {@link Monster} to render
	 */
	public MonsterDefaultRenderer(final Monster mon) {
		this.mon = mon;
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		final Vector2f monpos = this.mon.getPosition();
		final float x = monpos.x - this.mon.getRepresentationWidth() / 2 + vp.x;
		final float y = monpos.y - this.mon.getRepresentationHeight() / 2
				+ vp.y;
		this.mon.getRenderableRepresentation().draw(x, y);
	}

	@Override
	public IRenderable getRenderable() {
		return this.mon;
	}

	@Override
	public boolean collides(final Vector2f point) {
		final float x = this.mon.getPosition().x
				- this.mon.getRepresentationWidth() / 2;
		final float y = this.mon.getPosition().y
				- this.mon.getRepresentationHeight() / 2;
		return point.x < x + this.mon.getRepresentationWidth() && point.x > x
				&& point.y < y + this.mon.getRepresentationHeight()
				&& point.y > y;
	}

	@Override
	public boolean collides(final IRenderer other) {
		return false;
	}
}
