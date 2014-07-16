package frontend.gui.gameinterface;

import org.newdawn.slick.geom.Vector2f;

public class HorizontalLayout extends LayoutManager {
	private final int spacing;
	private final Vector2f offset;

	public HorizontalLayout(final Component component) {
		super(component);
		this.offset = new Vector2f(component.getPosition());
		this.spacing = 20;
	}

	@Override
	public void add(final Component component) {
		component.getPosition().set(this.offset);
		this.offset.x += component.getSize().width + spacing;
	}

	@Override
	public void remove(final Component component) {
		this.offset.x -= component.getSize().width + spacing;
	}
}
