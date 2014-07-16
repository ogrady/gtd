package frontend.gui.gameinterface;

import game.GoodTowerDefenseGame;

import java.awt.Dimension;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * {@link Component} that contains setable text
 * 
 * @author Daniel
 * 
 */
public class Label extends Component {
	private String text;

	public void setText(final String text) {
		this.text = text;
		this.size.width = GoodTowerDefenseGame.getInstance().getContainer()
				.getDefaultFont().getWidth(text);
		this.size.height = GoodTowerDefenseGame.getInstance().getContainer()
				.getDefaultFont().getHeight(text);
	}

	public Label(final String text, final Color colour) {
		super(new Dimension(0, 0));
		this.setText(text);
		setForegroundColour(colour);
	}

	@Override
	protected void paint(final Graphics g) {
		g.setColor(this.foregroundColour);
		g.drawString(this.text, this.position.x, this.position.y);
	}

}
