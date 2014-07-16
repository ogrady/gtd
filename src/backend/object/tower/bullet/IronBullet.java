package backend.object.tower.bullet;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Const;
import backend.object.monster.Monster;
import backend.object.tower.Tower;

public class IronBullet extends Projectile {

	public IronBullet(final Tower src, final Monster target) {
		super(src, target, 10, 25);
		try {
			this.representation = new Image(Const.PATH_BULLET);
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void impact() {
		this.target.takeDamage(this.source, this.damage);
		super.impact();
	}
}
