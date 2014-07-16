package frontend.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import util.IRenderable;
import util.IRenderer;
import frontend.gui.FloatingText;

public class FloatingTextRenderer implements IRenderer {
	private final FloatingText text;

	public FloatingTextRenderer(final FloatingText text) {
		this.text = text;
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		g.setColor(new Color(0, 0, 0, this.text.getAlpha()));
		g.drawString(this.text.getText(), this.text.getPosition().x,
				this.text.getPosition().y);
	}

	@Override
	public IRenderable getRenderable() {
		return this.text;
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
