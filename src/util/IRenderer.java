package util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public interface IRenderer {
	public void render(GameContainer gc, Graphics g);
	public IRenderable getRenderable();
	public boolean collides(Vector2f point);
	public boolean collides(IRenderer other);
}
