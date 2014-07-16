package backend.object.monster.statuseffect;

import backend.object.monster.Monster;

/**
 * Effect to slow down a Monster for a while
 * 
 * @author Daniel
 */
public class SlowEffect extends StatusEffect {
	private float slowBy;

	/**
	 * generic Constructor
	 * 
	 * @param m
	 *            target Monster
	 */
	public SlowEffect(final Monster m) {
		this(m, 2000, 0.4f);
	}

	/**
	 * Dummy Effect that can be used to check whether a Monster already has this
	 * Effect in its effect-list
	 */
	public SlowEffect() {
	}

	/**
	 * Constructor
	 * 
	 * @param m
	 *            target Monster
	 * @param duration
	 *            duration for the Effect in milliseconds
	 * @param slowBy
	 *            amount by which the Monster will be slowed
	 */
	public SlowEffect(final Monster m, final long duration, final float slowBy) {
		super(m, duration);
		this.slowBy = slowBy;
	}

	/**
	 * Slow the Monster down by the Effects slow-amount.<br>
	 * Make sure the Monster does not completely stand still
	 */
	@Override
	public void apply() {
		if (this.target.getSpeed() - this.slowBy <= 0) {
			this.slowBy = this.target.getSpeed() - 0.1f;
		}
		this.target.setSpeed(this.target.getSpeed() - this.slowBy);
	}

	/**
	 * Revert the slowdown Effect again by speeding the Monster up by the
	 * slow-amount
	 */
	@Override
	public void remove() {
		this.target.setSpeed(this.target.getSpeed() + this.slowBy);
		this.cleanUp();
	}
}
