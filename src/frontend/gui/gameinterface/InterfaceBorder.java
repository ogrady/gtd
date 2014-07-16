package frontend.gui.gameinterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class InterfaceBorder {
	protected Component component;
	protected int width;
	protected Color colour;

	public InterfaceBorder(final Component component, final int width,
			final Color col) {
		this.component = component;
		this.width = width;
		this.colour = col;
	}

	abstract public void render(Graphics g);
}
