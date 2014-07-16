package listener;

import backend.object.monster.statuseffect.StatusEffect;

/**
 * Effect-related Events
 * @author Daniel
 */
public interface IEffectListener extends IListener {
	/**
	 * Thrown when the Effect wears off
	 * @param e Effect that just wore off
	 */
	public void onWearOff(StatusEffect e);
}
