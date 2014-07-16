package frontend.renderer;

import game.GoodTowerDefenseGame;

import java.awt.Dimension;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import util.IRenderable;
import util.IRenderer;
import backend.object.Bar;

/**
 * Default renderer for health-bars.<br>
 * Renders the health-points as flat bars with colour (as specified in the
 * {@link Bar}) for the filled portion, while the empty part of the bar is
 * transparent. The bar also has a border as defined in the {@link Bar}.
 * 
 * 
 * @author Daniel
 * 
 */
public class BarDefaultRenderer implements IRenderer {
	private final Bar bar;

	public BarDefaultRenderer(final Bar b) {
		this.bar = b;
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		final Vector2f pos = this.bar.getPosition();
		final Vector2f viewport = GoodTowerDefenseGame.getInstance()
				.getPlayState().getViewport();
		final Dimension size = this.bar.getSize();
		g.setColor(this.bar.getBorderColor());
		final float x = pos.x - this.bar.getSize().width / 2 + viewport.x;
		final float y = pos.y + viewport.y;
		g.drawRect(x, y, size.width, size.height);
		final float perc = 100 / this.bar.getMaximum() * this.bar.getValue();
		final float fullWidth = perc * size.width / 100;
		g.setColor(this.bar.getFullColor());
		g.fillRect(x + this.bar.getBorderSize(), y + this.bar.getBorderSize(),
				fullWidth - this.bar.getBorderSize(),
				size.height - this.bar.getBorderSize());
		// uncomment the following lines for filled parts of empty bars
		// g.setColor(this.bar.getEmptyColor());
		// g.fillRect(x+fullWidth, y+this.bar.getBorderSize(),
		// size.width-fullWidth, size.height - this.bar.getBorderSize());
	}

	@Override
	public IRenderable getRenderable() {
		return this.bar;
	}

	@Override
	public boolean collides(final Vector2f point) {
		return false;
	}

	@Override
	public boolean collides(final IRenderer other) {
		return false;
	}

}
