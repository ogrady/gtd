package frontend.gui.gameinterface;

abstract public class LayoutManager {
	protected Component component;

	public void setComponent(final Component component) {
		this.component = component;
	}

	public LayoutManager(final Component component) {
		this.setComponent(component);
	}

	abstract public void add(final Component component);

	abstract public void remove(final Component component);
}
