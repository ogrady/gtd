package frontend;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageLoader {
	public static Image loadImage(final String path) {
		Image img = null;
		try {
			img = new Image(path);
		} catch (final SlickException e) {
			e.printStackTrace();
		}
		return img;
	}
}
