package backend.object.tower.bullet;

import java.util.Collection;
import java.util.Iterator;

import org.newdawn.slick.geom.Circle;

import backend.object.monster.Monster;
import backend.object.tower.Tower;
import frontend.renderer.ExplosionRenderer;
import game.GoodTowerDefenseGame;

public class Explosion extends Projectile {
	private final float maxRadius;
	private float currentRadius;
	private final Collection<Monster> activeMonsters;

	/**
	 * @return the current radius of the {@link Explosion} (centered on the
	 *         position)
	 */
	public float getCurrentRadius() {
		return currentRadius;
	}

	/**
	 * Constructor
	 * 
	 * @param source
	 *            tower that caused the {@link Explosion}
	 * @param maxRadius
	 *            radius when the {@link Explosion} will stop growing
	 * @param radiusDelta
	 *            delta by which the {@link Explosion} grows each tick
	 * @param dmg
	 *            damage the {@link Explosion} causes when it touches a
	 *            {@link Monster}
	 * @param activeMonsters
	 *            list of active Monsters to iterate through (will be used by
	 *            reference)
	 */
	public Explosion(final Tower source, final float maxRadius,
			final float radiusDelta, final float dmg,
			final Collection<Monster> activeMonsters) {
		super(source, null, radiusDelta, dmg);
		this.activeMonsters = activeMonsters;
		this.maxRadius = maxRadius;
		this.currentRadius = 0;
		this.renderer = new ExplosionRenderer(this);
	}

	/**
	 * The explosion grows until it reached its maximum radius and damages
	 * everything from the list of active monsters it touches.
	 */
	@Override
	public void onTick(final GoodTowerDefenseGame game, final long ms) {
		this.currentRadius = Math.min(this.maxRadius, this.currentRadius
				+ this.speed);
		final Circle c = new Circle(this.source.getCenterPosition().x,
				this.source.getCenterPosition().y, this.currentRadius);
		final Iterator<Monster> it = this.activeMonsters.iterator();
		while (it.hasNext()) {
			final Monster m = it.next();
			if (m.getHitbox().intersects(c)) {
				m.takeDamage(this.source, this.damage);
			}
		}
		if (this.currentRadius >= this.maxRadius) {
			this.impact();
		}
	}

}
