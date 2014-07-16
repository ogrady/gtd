package backend.object.monster.statuseffect;

import game.GoodTowerDefenseGame;

import java.util.Iterator;

import listener.IEffectListener;
import listener.IListenable;
import listener.IMonsterListener;
import listener.IPlayStateListener;
import listener.ListenerSet;

import org.newdawn.slick.geom.Vector2f;

import util.IDamageSource;
import backend.object.monster.Monster;

/**
 * Effects that can be applied to a {@link Monster} They effect them when they
 * get attached and their Effect will be reversed when they are removed which
 * can be caused by having their counter timed-up or other causes, such as
 * support from other {@link Monster}s
 * 
 * @author Daniel
 */
public abstract class StatusEffect implements IListenable<IEffectListener>,
		IPlayStateListener, IMonsterListener {
	protected long duration;
	protected Monster target;
	protected ListenerSet<IEffectListener> listeners;

	/**
	 * @return the Monster the Effect has been applied to
	 */
	public Monster getTarget() {
		return target;
	}

	/**
	 * @return the reamining duration of the Effect
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Constructor
	 * 
	 * @param m
	 *            target Monster for the Effect
	 * @param d
	 *            duration of the Effect in milliseconds
	 */
	public StatusEffect(final Monster m, final long d) {
		this.listeners = new ListenerSet<IEffectListener>();
		GoodTowerDefenseGame.getInstance().getPlayState().getListeners()
				.registerListener(this);
		this.target = m;
		this.duration = d;
	}

	/**
	 * Dummy Effect that can be used to check whether a Monster already has some
	 * sort of Effect in its effect-list
	 */
	public StatusEffect() {
	}

	/**
	 * Specifies what happens when the Effect is applied to the Monster (slow
	 * down or such)
	 */
	abstract public void apply();

	/**
	 * Specifies what happens when the Effect is removed again (speed the
	 * Monster up again or such)
	 */
	abstract public void remove();

	/**
	 * Cleanup that unregisters self as EnvironmentObserver and sets the Monster
	 * to null
	 */
	protected void cleanUp() {
		GoodTowerDefenseGame.getInstance().getPlayState().getListeners()
				.remove(this);
		this.target = null;
	}

	/**
	 * If the Effect has been ingame longer than its duration then remove it
	 * notify the observing Monster of this change the Monster will then call
	 * the onRemove method to reverse the Effect
	 */
	@Override
	public void onTick(final GoodTowerDefenseGame game, final long passedMillis) {
		this.duration -= passedMillis;
		if (this.duration <= 0) {
			final Iterator<IEffectListener> it = this.listeners.iterator();
			while (it.hasNext()) {
				it.next().onWearOff(this);
			}
			this.target.getEffects().remove(this);
			this.remove();
		}
	}

	@Override
	public void onWaveStart(final int wave) {
	}

	@Override
	public void onTakeDamage(final Monster mon, final IDamageSource src) {
	}

	/**
	 * Set the duration to zero make the Effect disappear in the next iteration
	 * to have it and the Monster removed from the garbage collector asap
	 */
	@Override
	public void onDie(final Monster mon, final IDamageSource src) {
		this.duration = 0;
	}

	/**
	 * Set the duration to zero make the Effect disappear in the next iteration
	 * to have it and the Monster removed from the garbage collector asap
	 */
	@Override
	public void onReachedStronghold(final Monster mon) {
		this.duration = 0;
	}

	@Override
	public void onMove(final Monster mon, final Vector2f oldPos,
			final Vector2f newPos) {
	};

	@Override
	public ListenerSet<IEffectListener> getListeners() {
		return this.listeners;
	}
}
