package frontend.renderer;

import game.GoodTowerDefenseGame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import util.IRenderable;
import util.IRenderer;
import backend.object.Positionable;

public class PositionableDefaultRenderer implements IRenderer {
	protected Positionable positionable;

	public PositionableDefaultRenderer(final Positionable pos) {
		this.positionable = pos;
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		final Vector2f pos = this.positionable.getPosition();
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		this.positionable.getRenderableRepresentation().draw(pos.x + vp.x,
				pos.y + vp.y);
	}

	@Override
	public IRenderable getRenderable() {
		return this.positionable;
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
