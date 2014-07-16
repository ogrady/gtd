package frontend.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import backend.object.Bar;
import backend.object.monster.Monster;

/**
 * Renderer to display monsters as defined in {@link MonsterDefaultRenderer},
 * but also draws their healthbar beneath the monster.
 * 
 * @author Daniel
 * 
 */
public class MonsterBarRenderer extends MonsterDefaultRenderer {

	/**
	 * Constructor
	 * 
	 * @param mon
	 *            the monster to render (which also holds the {@link Bar} that
	 *            will be rendered, too).
	 */
	public MonsterBarRenderer(final Monster mon) {
		super(mon);
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		this.mon.getHealthBar().getRenderer().render(gc, g);
		super.render(gc, g);
	}

}
