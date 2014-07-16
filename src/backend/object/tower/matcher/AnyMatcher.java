package backend.object.tower.matcher;

import java.util.ArrayList;

import backend.object.monster.Monster;

/**
 * This Matcher stops looking for a Monster as soon as it determined one that fits
 * Useful for Towers that only apply Effects to Monster as they could look out for
 * Monster that don't have the Effect they apply yet.
 * @author Daniel
 */
abstract public class AnyMatcher extends Matcher {

	/**
	 * Runs over the list with a while loop. Possibly faster than the BestMatcher
	 * as it doesn't have to look at any element. But can return null if no Element
	 * in the list matches the criteria. It therefore doesn't throw Exceptions for that as
	 * the BestMatcher does
	 */
	@Override
	public Monster getMatch(ArrayList<Monster> list) {
		Monster match = null;
		int i = 0;
		while(i < list.size() && match == null) {
			if(this.matches(list.get(i))) {
				match = list.get(i);
			}
			i++;
		}
		return match;
	}
	
	/**
	 * Finds out whether the given Monster matches the filter of this Matcher
	 * @param m Monster to check
	 * @return true, if it matches the filter
	 */
	abstract protected boolean matches(Monster m);
}
