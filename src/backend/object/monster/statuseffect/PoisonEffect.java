package backend.object.monster.statuseffect;

import util.IDamageSource;
import game.GoodTowerDefenseGame;
import backend.object.monster.Monster;

/**
 * Effect that frequently damages the target Monster
 * @author Daniel
 */
public class PoisonEffect extends StatusEffect implements IDamageSource {
	private int damage;
	private int frequency;
	private int milliAccu;
	
	/**
	 * generic Constructor
	 * @param m target Monster
	 */
	public PoisonEffect(Monster m) {
		this(m, 2000, 1, 200);
	}

	/**
	 * Constructor
	 * @param m target Monster
	 * @param duration duration for the Effect in milliseconds
	 * @param dpt damage per tick
	 * @param freq frequency by which damage should be applied
	 */
	public PoisonEffect(Monster m, long duration, int dpt, int freq) {
		super(m, duration);
		this.damage = dpt;
		this.frequency = freq;
	}

	/**
	 * Nothing
	 */
	@Override
	public void apply() {	}

	/**
	 * Nothing
	 */
	@Override
	public void remove() {}
	
	/**
	 * Damage the target Monster and catches up if it missed seconds
	 */
	@Override
	public void onTick(GoodTowerDefenseGame game, long millis) {
		milliAccu += millis;
		this.target.takeDamage(this, (milliAccu / this.frequency) * this.damage);
		milliAccu %= this.frequency;
		super.onTick(game, millis);
	}
}
