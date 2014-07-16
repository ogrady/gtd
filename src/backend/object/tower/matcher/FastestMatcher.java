package backend.object.tower.matcher;

import backend.object.monster.Monster;

/**
 * Finds the fastest Monster in a list
 * @author Daniel
 */
public class FastestMatcher extends BestMatcher {

	/**
	 * gets the fastest Monster
	 */
	@Override
	protected Monster best(Monster m1, Monster m2) {
		return m1.getSpeed() > m2.getSpeed() ? m1 : m2;
	}

}
