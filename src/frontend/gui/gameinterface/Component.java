package frontend.gui.gameinterface;

import java.awt.Dimension;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Lightweight components for rudimentary GUI-building
 * 
 * @author Daniel
 * 
 */
public class Component {
	protected Vector2f position;
	protected Dimension size;
	protected Component parent;
	protected ArrayList<Component> children;
	protected Color backgroundColour, foregroundColour;
	protected LayoutManager layout;

	/**
	 * @param layout
	 *            the new {@link LayoutManager} (!= null)
	 */
	public void setLayoutManager(final LayoutManager layout) {
		if (layout != null) {
			this.layout = layout;
		}
	}

	/**
	 * @return the {@link Color} used to render the foreground (aka text)
	 */
	public Color getForegroundColour() {
		return foregroundColour;
	}

	/**
	 * @param foregroundColour
	 *            the new {@link Color} (!= null) to render the foreground
	 */
	public void setForegroundColour(final Color foregroundColour) {
		if (foregroundColour != null) {
			this.foregroundColour = foregroundColour;
		}
	}

	/**
	 * @return the new colour used to paint the background with
	 */
	public Color getBackgroundColour() {
		return backgroundColour;
	}

	/**
	 * @param backgroundColour
	 *            the colour (!= null) used to paint the background with
	 */
	public void setBackgroundColour(final Color backgroundColour) {
		if (backgroundColour != null) {
			this.backgroundColour = backgroundColour;
		}
	}

	/**
	 * @return the own position (relative to the parent, if any)
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return the size of the {@link Component}
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * @return a list of child-components
	 */
	public ArrayList<Component> getChildren() {
		return children;
	}

	/**
	 * @return the parent, if any
	 */
	public Component getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the new parent. Can be null to "free" the {@link Component}
	 */
	public void setParent(final Component parent) {
		this.parent = parent;
	}

	public Component(final Dimension size) {
		this.children = new ArrayList<Component>();
		this.setParent(parent);
		this.position = new Vector2f();
		this.size = size;
		this.backgroundColour = new Color(0, 0, 0, 0);
		this.setLayoutManager(new HorizontalLayout(this));
	}

	/**
	 * Adds a new component to the list of child-components. This
	 * {@link Component} will be the parent of the passed {@link Component}.
	 * Will then call the {@link LayoutManager} to arrange the new
	 * {@link Component}.
	 * 
	 * @param comp
	 *            the new child to add
	 */
	public void add(final Component comp) {
		this.children.add(comp);
		comp.setParent(this);
		this.layout.add(comp);
		/*comp.getPosition().x += this.getPosition().x + this.usedSize.width;
		comp.getPosition().y += this.getPosition().y + this.usedSize.height;
		this.usedSize.width += comp.getSize().width;
		this.usedSize.height += comp.getSize().height;
		if (this.usedSize.width > this.size.width) {
			this.size.width += this.usedSize.width;
		}
		if (this.usedSize.height > this.size.height) {
			this.size.height += this.usedSize.height;
		}*/
	}

	/**
	 * Removes a {@link Component} from the list of subcomponents. Will set the
	 * parent to null and call the {@link LayoutManager} to adjust the layout,
	 * but only if the passed element was actually a child of this.
	 * 
	 * @param comp
	 *            the {@link Component} to remove
	 */
	public void remove(final Component comp) {
		if (comp.getParent() == this) {
			this.children.remove(comp);
			comp.setParent(null);
			this.layout.remove(comp);
		}
	}

	/**
	 * Renders the {@link Component} itself and then all child-components
	 * 
	 * @param g
	 *            the canvas to draw on
	 */
	public void paintAll(final Graphics g) {
		paint(g);
		for (final Component child : this.children) {
			child.paintAll(g);
		}
	}

	/**
	 * Renders the {@link Component} itself
	 * 
	 * @param g
	 *            the canvas to draw on
	 */
	protected void paint(final Graphics g) {
		g.setColor(this.backgroundColour);
		g.fillRect(this.position.x, this.position.y, this.size.width,
				this.size.height);

	}
}
