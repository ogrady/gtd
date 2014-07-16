package backend.object.tower.bullet;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Const;
import backend.object.monster.Monster;
import backend.object.monster.statuseffect.SlowEffect;
import backend.object.tower.Tower;

public class SlowBullet extends Projectile {

	public SlowBullet(final Tower source, final Monster target) {
		super(source, target, 6, 0);
		try {
			this.representation = new Image(Const.PATH_FROSTBULLET);
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void impact() {
		final SlowEffect se = new SlowEffect(this.target, 5000, 0.9f);
		this.target.getEffects().add(se);
		se.apply();
		super.impact();
	}
}
