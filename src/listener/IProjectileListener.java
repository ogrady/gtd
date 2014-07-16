package listener;

import org.newdawn.slick.geom.Vector2f;

import backend.object.tower.bullet.Projectile;


/**
 * Listener for Projectile relevant Events
 * @author Daniel
 */
public interface IProjectileListener extends IListener {
	public void onImpact(Projectile proj, Vector2f position);
}
