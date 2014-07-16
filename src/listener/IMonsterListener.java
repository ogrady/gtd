package listener;

import org.newdawn.slick.geom.Vector2f;

import util.IDamageSource;
import backend.object.monster.Monster;


/**
 * Listener for Monster relevant Events
 * @author Daniel
 */
public interface IMonsterListener extends IListener {
	
	/**
	 * Fired when a Monster receives damage
	 * @param mon the firing Monster
	 * @param src the source of the damage
	 */
	public void onTakeDamage(Monster mon, IDamageSource src);
	
	/**
	 * Fired when the Monster dies
	 * @param mon the firing Monster
	 * @param cause the entity that caused the death
	 */
	public void onDie(Monster mon, IDamageSource cause);
	
	/**
	 * Fired when the Monster reaches the Home
	 * @param mon the firing Monster
	 */
	public void onReachedStronghold(Monster mon);
	
	/**
	 * Fired when the {@link Monster} moves
	 * @param mon moving {@link Monster}
	 * @param oldPos position before movement
	 * @param newPos position after movement
	 */
	public void onMove(Monster mon, Vector2f oldPos, Vector2f newPos);
}
