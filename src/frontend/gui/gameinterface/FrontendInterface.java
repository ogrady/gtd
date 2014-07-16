package frontend.gui.gameinterface;

import java.awt.Dimension;

import org.newdawn.slick.Color;

import util.Const;
import frontend.ImageLoader;

public class FrontendInterface extends Component {
	public Label money, life;

	public FrontendInterface(final Dimension size) {
		super(size);
		this.money = new Label("              ", Color.white);
		this.life = new Label("              ", Color.white);
		this.backgroundColour = Color.darkGray;
		this.add(new ImageComponent(ImageLoader
				.loadImage(Const.PATH_MONEY_ICON)));
		this.add(this.money);
		this.add(new ImageComponent(ImageLoader
				.loadImage(Const.PATH_HEALTH_ICON)));
		this.add(this.life);
	}
}
