package frontend.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public abstract class MenuItem {
	private final Image image;
	public final Vector2f position;

	public MenuItem(final Image image) {
		this.image = image;
		this.position = new Vector2f();
	}

	public Image getImage() {
		return this.image;
	}

	/**
	 * Called when the mouse is released over the {@link MenuItem}
	 */
	abstract public void onClick();
}
