package backend.object.tower.matcher;

import backend.object.monster.Monster;

public class FurthestMatcher extends BestMatcher {

	@Override
	protected Monster best(Monster m1, Monster m2) {
		return (m1.getTraveledDistance() > m2.getTraveledDistance()) ? m1 : m2;
	}

}
