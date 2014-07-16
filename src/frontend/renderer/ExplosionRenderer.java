package frontend.renderer;

import game.GoodTowerDefenseGame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import backend.object.tower.bullet.Explosion;

/**
 * Renderer for {@link Explosion}s
 * 
 * @author Daniel
 * 
 */
public class ExplosionRenderer extends PositionableDefaultRenderer {

	/**
	 * Constructor
	 * 
	 * @param expl
	 *            {@link Explosion} to render
	 */
	public ExplosionRenderer(final Explosion expl) {
		super(expl);
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		final Vector2f pos = this.positionable.getPosition();
		final Explosion expl = (Explosion) this.positionable;
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		g.setColor(new Color(255, 0, 0, 50));
		g.fillOval(pos.x + vp.x - expl.getCurrentRadius() / 2, pos.y + vp.y
				- expl.getCurrentRadius() / 2, expl.getCurrentRadius(),
				expl.getCurrentRadius());
	}

}
