package backend.object.tower.matcher;

import backend.object.monster.Monster;


public class StrongestMatcher extends BestMatcher {

	@Override
	protected Monster best(Monster m1, Monster m2) {
		return m1.getHitpoints() > m2.getHitpoints()?m1:m2;
	}

}
