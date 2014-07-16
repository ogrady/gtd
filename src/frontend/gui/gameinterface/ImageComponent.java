package frontend.gui.gameinterface;

import java.awt.Dimension;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ImageComponent extends Component {
	private final Image img;

	public ImageComponent(final Image img) {
		super(new Dimension(img.getWidth(), img.getHeight()));
		this.img = img;
	}

	@Override
	protected void paint(final Graphics g) {
		g.drawImage(this.img, this.position.x, this.position.y);

	}

}
