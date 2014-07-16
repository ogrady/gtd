package backend.object.tower.matcher;

import java.util.ArrayList;

import exception.CommonException;
import backend.object.monster.Monster;



/**
 * This Matcher finds the Monster that is fitted best
 * Used to find the fastest, strongest, toughest etc Monster 
 * @author Daniel
 */
abstract public class BestMatcher extends Matcher {

	/**
	 * Runs over the list in a for-each loop as we have to check every element.
	 * It assumes that the list contains at least 1 element as it uses list.get(0).
	 * This also ensures that BestMatchers NEVER return null (but can of course throw ArrayOutOfBounds Exceptions)
	 * @throws ArrayOutOfBoundException if not at least 1 Monster is inside the list
	 */
	@Override
	public Monster getMatch(ArrayList<Monster> list) {
		// We do not accept null, however we accept empty lists
		if(list == null) {
			throw new CommonException(CommonException.ILLEGAL_ARGUMENTS);
		}
		if(list.size() == 0) return null;
		Monster best = list.get(0);
		for(Monster m : list) {
			best = this.best(best, m);
		}
		return best;
	}
	
	/**
	 * Used to determine which of the Monsters is better (depending on the criteria specified in inheritors)
	 * @param m1 
	 * @param m2
	 * @return the better Monster
	 */
	abstract protected Monster best(Monster m1, Monster m2);
}
