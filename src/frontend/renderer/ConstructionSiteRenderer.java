package frontend.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import util.Const;
import backend.object.Bar;
import backend.object.tower.Tower;
import frontend.ImageLoader;
import game.GoodTowerDefenseGame;

public class ConstructionSiteRenderer extends PositionableDefaultRenderer {
	private final Tower tower;
	private static final Image site = ImageLoader.loadImage(Const.PATH_SITE);

	public ConstructionSiteRenderer(final Tower tower) {
		super(tower);
		this.tower = tower;
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		// super.render(gc, g);
		final Vector2f pos = this.positionable.getPosition();
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		site.draw(pos.x + vp.x, pos.y + vp.y);
		new BarDefaultRenderer(new Bar(this.tower.getCenterPosition(),
				(float) this.tower.getBuildDelayAccu(),
				(float) this.tower.getBuildDelay())).render(gc, g);
	}
}
