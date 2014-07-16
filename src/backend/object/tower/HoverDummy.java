package backend.object.tower;

import exception.PlayingFieldException;
import frontend.gui.FloatingText;
import game.GoodTowerDefenseGame;
import game.PlayState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.InputAdapter;

import util.Const;
import util.IRenderable;
import util.IRenderer;
import backend.playingfield.PlayingField;
import backend.playingfield.PlayingFieldMetaCell;

public class HoverDummy extends InputAdapter implements IRenderable {
	private static final Color possibleRangeColour = new Color(0, 255, 0, 50);
	private static final Color possibleRangeBorderColour = new Color(0, 255, 0);
	private static final Color impossibleRangeColour = new Color(255, 0, 0, 50);
	private static final Color impossibleRangeBorderColour = new Color(255, 0,
			0);

	private final Tower tower;
	private final Image img, possibleImage, impossibleImage;
	private final float alpha;
	private final float range;

	public HoverDummy(final Tower tow) {
		this.tower = tow;
		this.alpha = 0.7f;
		this.img = (Image) tow.getRenderableRepresentation();
		this.possibleImage = this.img.copy();
		this.impossibleImage = this.img.copy();
		this.range = tow.getRange();

		for (int i = 0; i < 4; i++) {
			this.impossibleImage.setColor(i, 1, 0, 0, this.alpha);
		}

		for (int i = 0; i < 4; i++) {
			this.possibleImage.setColor(i, 0, 1, 0, this.alpha);
		}
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		final Input in = gc.getInput();
		final int mx = Math.max(0, in.getMouseX());
		final int my = Math.max(0, in.getMouseY());
		final GoodTowerDefenseGame game = GoodTowerDefenseGame.getInstance();
		final Vector2f vp = game.getPlayState().getViewport();
		final PlayingField field = game.getPlayState().getPlayingField();
		try {

			final PlayingFieldMetaCell abscell = field.getCellAt(new Vector2f(
					mx, my));
			Image draw;
			Color borderColour, rangeColour;
			if (this.tower.canBeBuiltOn(abscell)) {
				draw = this.possibleImage;
				borderColour = possibleRangeBorderColour;
				rangeColour = possibleRangeColour;
			} else {
				draw = this.impossibleImage;
				borderColour = impossibleRangeBorderColour;
				rangeColour = impossibleRangeColour;
			}
			g.setColor(rangeColour);
			g.fillOval(abscell.getCenterPosition().x + vp.x - this.range / 2,
					abscell.getCenterPosition().y + vp.y - this.range / 2,
					this.range, this.range);
			g.setColor(borderColour);
			g.drawOval(abscell.getCenterPosition().x + vp.x - this.range / 2,
					abscell.getCenterPosition().y + vp.y - this.range / 2,
					this.range, this.range);
			draw.draw(abscell.getPosition().x + vp.x, abscell.getPosition().y
					+ vp.y);
		} catch (final PlayingFieldException pfe) {
			// player hovers with the mouse over the window decoration. Not an
			// issue - just don't display the dummy
		}
	}

	@Override
	public Renderable getRenderableRepresentation() {
		return this.img;
	}

	@Override
	public void mouseClicked(final int button, final int x, final int y,
			final int clickCount) {
		final PlayState ps = GoodTowerDefenseGame.getInstance().getPlayState();
		if (button == Input.MOUSE_LEFT_BUTTON) {
			final PlayingField field = ps.getPlayingField();
			final Vector2f pos = new Vector2f(x, y);
			final PlayingFieldMetaCell cell = field.getCellAt(pos);
			if (this.tower.canBeBuiltOn(cell)) {
				if (ps.getPlayer().getMoney() >= this.tower.getBuildPrice()) {
					this.tower.setPosition(cell.getPosition());
					field.getBuiltTowers().add(this.tower);
					cell.setTower(this.tower);
					ps.getListeners().add(this.tower);
					this.setAcceptingInput(false);
					this.tower.startUpgrade();
				} else {
					this.tower.destruct();
					field.addFloatingText(new FloatingText(GoodTowerDefenseGame
							.getInstance(), GoodTowerDefenseGame.getInstance()
							.getLanguage().getString(Const.L_NO_MONEY)));
				}
			}
		} else if (button == Input.MOUSE_RIGHT_BUTTON) {
			this.setAcceptingInput(false);
		}
		this.destruct();

	}

	public void destruct() {
		this.setAcceptingInput(false);
		GoodTowerDefenseGame.getInstance().getInput().removeMouseListener(this);
		GoodTowerDefenseGame.getInstance().getPlayState().setHoverDummy(null);
	}

	@Override
	public IRenderer getRenderer() {
		return null;
	}

	@Override
	public void setRenderer(final IRenderer r) {

	}
}
