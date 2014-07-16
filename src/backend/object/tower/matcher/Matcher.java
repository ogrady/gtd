package backend.object.tower.matcher;

import java.util.ArrayList;

import backend.object.monster.Monster;

/**
 * Matcher that finds from a list a matching target
 * @author Daniel
 */
abstract public class Matcher {
	/**
	 * Method to run over the list. Can either be while or for-each. Depending on the purpose of the Matcher
	 * @param list list of Monsters that should be checked 
	 * @return a Monster that passed the matching
	 */
	abstract public Monster getMatch(ArrayList<Monster> list);
}
