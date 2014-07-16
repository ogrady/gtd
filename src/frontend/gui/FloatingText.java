package frontend.gui;

import frontend.renderer.FloatingTextRenderer;
import game.GoodTowerDefenseGame;

import java.util.Iterator;

import listener.IFloatingTextListener;
import listener.IListenable;
import listener.IPlayStateListener;
import listener.ListenerSet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Vector2f;

import util.IRenderable;
import util.IRenderer;
import backend.object.Positionable;
import backend.playingfield.PlayingField;

/**
 * {@link FloatingText}s are strings that are placed on a particular position on
 * the {@link PlayingField} and will then proceed to float upwards with every
 * tick and fade out (in terms of their alpha-component) until they are
 * completely transparent and disappear.
 * 
 * @author Daniel
 * 
 */
public class FloatingText extends Positionable implements IPlayStateListener,
		IRenderable, IListenable<IFloatingTextListener> {
	private IRenderer renderer;
	private final String text;
	private final int floatMax;
	private int currentFloat;
	private final int floatPerTick;
	private final ListenerSet<IFloatingTextListener> listeners;
	private final int width, height;

	/**
	 * @return the current alpha component (0 < getAlpha < 255), starting with
	 *         255
	 */
	public int getAlpha() {
		return 255 - 255 / floatMax * currentFloat;
	}

	/**
	 * @return the string this text consists of
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Constructor
	 * 
	 * @param game
	 *            the game-context
	 * @param pos
	 *            the upper left position of the text
	 * @param text
	 *            the string for the text
	 */
	public FloatingText(final GoodTowerDefenseGame game, final Vector2f pos,
			final String text) {
		super(pos);
		this.listeners = new ListenerSet<IFloatingTextListener>();
		this.text = text;
		game.getPlayState().getListeners().registerListener(this);
		this.floatMax = 50;
		this.floatPerTick = 1;
		this.width = game.getContainer().getDefaultFont().getWidth(text);
		this.height = game.getContainer().getDefaultFont().getHeight(text);
		this.setRenderer(new FloatingTextRenderer(this));

	}

	/**
	 * Constructor that centers the text on the screen upon creation
	 * 
	 * @param game
	 *            the game-context
	 * @param text
	 *            the string for the text
	 */
	public FloatingText(final GoodTowerDefenseGame game, final String text) {
		this(game, new Vector2f(), text);
		this.position.x = game.getContainer().getWidth() / 2
				- this.getRepresentationWidth() / 2;
		this.position.y = game.getContainer().getHeight() / 2;
	}

	@Override
	public void onTick(final GoodTowerDefenseGame game, final long ms) {
		currentFloat += floatPerTick;
		if (currentFloat >= floatMax) {
			game.getPlayState().getListeners().unregisterListener(this);
			final Iterator<IFloatingTextListener> it = this.getListeners()
					.iterator();
			while (it.hasNext()) {
				it.next().onFadeOut(this);
			}
		}
		this.position.y -= floatPerTick;
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		this.renderer.render(gc, g);
	}

	@Override
	public Renderable getRenderableRepresentation() {
		return null;
	}

	@Override
	public IRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public void setRenderer(final IRenderer r) {
		this.renderer = r;
	}

	@Override
	public int getRepresentationWidth() {
		return this.width;
	}

	@Override
	public int getRepresentationHeight() {
		return this.height;
	}

	@Override
	public ListenerSet<IFloatingTextListener> getListeners() {
		return this.listeners;
	}

	@Override
	public void onWaveStart(final int wave) {
	}

}
