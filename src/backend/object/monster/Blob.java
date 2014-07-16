package backend.object.monster;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import util.Const;
import backend.object.buildings.Spawner;
import backend.playingfield.GroundType;

public class Blob extends Monster {
	@SuppressWarnings("unused")
	private Animation left, down, up, right, current;

	public Blob(final Spawner sp) {
		super(sp, 200, 2, 1, 1f, 100);
		try {
			left = new Animation(new SpriteSheet(Const.PATH_BLOB + "left.png",
					28, 19), 1000);
			right = new Animation(new SpriteSheet(
					Const.PATH_BLOB + "right.png", 24, 19), 50);
			up = new Animation(new SpriteSheet(Const.PATH_BLOB + "up.png", 24,
					19), 50);
			down = new Animation(new SpriteSheet(Const.PATH_BLOB + "down.png",
					24, 19), 50);
			current = left;
			this.representation = current;
			this.walkableTerrain.add(GroundType.SAND_WAY);
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getRepresentationWidth() {
		return current.getCurrentFrame().getWidth();
	}

	@Override
	public int getRepresentationHeight() {
		return current.getCurrentFrame().getHeight();
	}

	@Override
	public void moveOn() {
		current.update(50);
		super.moveOn();
	}

}
