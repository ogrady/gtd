package util;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;

/**
 * {@link IRenderable} is the interface to be implemented by all classes that
 * should be able to have themselves displayed by a {@link IRenderer}.
 * 
 * @author Daniel
 * 
 */
public interface IRenderable {
	/**
	 * Causes the {@link IRenderable} to render itself by passing the call to
	 * the {@link IRenderer}.
	 * 
	 * @param gc
	 *            the game-container passed by the game
	 * @param g
	 *            the graphics to draw itself on
	 */
	void render(GameContainer gc, Graphics g);

	/**
	 * Getter for the representation {@link Renderable} from slick. Could be a
	 * static image, an {@link Animation} or anything else.
	 * 
	 * @return the current representation of the {@link IRenderable}
	 */
	Renderable getRenderableRepresentation();

	/**
	 * Getter for the renderer
	 * 
	 * @return the currently employed renderer
	 */
	IRenderer getRenderer();

	/**
	 * Setter for the renderer
	 * 
	 * @param r
	 *            the new renderer to use from now on
	 */
	void setRenderer(IRenderer r);
}
