package backend.object.monster.util;

import backend.object.buildings.Spawner;
import backend.object.monster.Monster;

/**
 * Wraps around a regular {@link Monster} to give it a delay. When the delay expired the {@link Monster} will be spawned.
 * This will be scheduled by the containing {@link Spawner}.
 * @author Daniel
 *
 */
public class WaitingMonster {
	private long delay;
	private Monster monster;
	
	public long getDelay() {
		return this.delay;
	}
	
	public Monster getMonster() {
		return this.monster;
	}
	
	public WaitingMonster(long delay, Monster monster) {
		this.delay = delay;
		this.monster = monster;
	}
}
