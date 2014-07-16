package frontend.gui;

import game.GoodTowerDefenseGame;

import java.util.ArrayList;
import java.util.Iterator;

import listener.IListenable;
import listener.IRingMenuListener;
import listener.ListenerSet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.InputAdapter;

/**
 * {@link RingMenu} are menus the contain {@link MenuItem}, arranged in a
 * circular manner around a certain point (probably the mouse-position).<br>
 * When opening the menu, it will unfold from the center and expand to a size
 * that all {@link MenuItem}s have enough space to be displayed.
 * 
 * @author Daniel
 * 
 */
public class RingMenu extends InputAdapter implements MouseListener,
		IListenable<IRingMenuListener> {
	private final ArrayList<MenuItem> items;
	private int radius;
	private final Vector2f position;
	private Input input;
	private final ListenerSet<IRingMenuListener> listeners;
	private boolean acceptingInput;

	public RingMenu(final Vector2f pos) {
		this.listeners = new ListenerSet<IRingMenuListener>();
		this.items = new ArrayList<MenuItem>();
		this.position = pos;
		this.acceptingInput = true;
	}

	/**
	 * Add another menu-item to the list of items in the menu
	 * 
	 * @param item
	 *            the new item to add
	 */
	public void addMenuItem(final MenuItem item) {
		this.items.add(item);
	}

	/**
	 * @return the current radius of the {@link RingMenu}
	 */
	public int getRadius() {
		return this.radius;
	}

	/**
	 * @return the maximum radius this {@link RingMenu} will have when fully
	 *         unfolded. Dependend on the number of items in the menu: the more
	 *         items, the bigger the menu to leave space for all the items
	 */
	public int getMaxRadius() {
		return this.items.size() * 10;
	}

	/**
	 * @return the growth by which the menu expands until it is fully unfolded.
	 *         Dependent on the number of {@link MenuItem}s
	 */
	public int getGrowth() {
		return (int) Math.ceil(this.items.size() * 1.5);
	}

	public void render(final GameContainer gc, final Graphics g) {
		this.radius = Math.min(this.radius + this.getGrowth(),
				this.getMaxRadius());
		final double angle = Math.toRadians(360 / items.size());
		for (int i = 0; i < items.size(); i++) {
			final Image img = items.get(i).getImage();
			final float x = (float) (-radius * Math.sin(angle * i))
					- img.getWidth() / 2;
			final float y = (float) (-radius * Math.cos(angle * i))
					- img.getHeight() / 2;
			final Vector2f vp = GoodTowerDefenseGame.getInstance()
					.getPlayState().getViewport();
			g.drawImage(img, vp.x + x + position.x, vp.y + y + position.y);
			items.get(i).position.x = vp.x + x + position.x;
			items.get(i).position.y = vp.y + y + position.y;
		}
	}

	/**
	 * Closes the menu. Makes the menu stop receive input and notify its
	 * listeners with a {@link IRingMenuListener#onRingMenuClose()}-event
	 */
	private void close() {
		this.acceptingInput = false;
		if (this.input != null) {
			this.input.removeMouseListener(this);
		}
		final Iterator<IRingMenuListener> it = this.listeners.iterator();
		while (it.hasNext()) {
			it.next().onRingMenuClose();
		}
	}

	@Override
	public boolean isAcceptingInput() {
		return this.acceptingInput;
	}

	@Override
	public void setInput(final Input input) {
		this.input = input;
	}

	/**
	 * Closes the menu. If the release happened over the area of a
	 * {@link MenuItem} its {@link MenuItem#onClick()} will be called
	 */
	@Override
	public void mouseReleased(final int button, final int x, final int y) {
		boolean found = false;
		int i = 0;
		while (i < this.items.size() && !found) {
			final MenuItem mi = this.items.get(i);
			final Rectangle r = new Rectangle(mi.position.x, mi.position.y, mi
					.getImage().getWidth(), mi.getImage().getHeight());
			found = r.contains(x, y);
			i++;
		}
		if (found) {
			this.items.get(i - 1).onClick();
		}
		this.close();
	}

	@Override
	public ListenerSet<IRingMenuListener> getListeners() {
		return this.listeners;
	}
}
