package backend.object.tower.matcher;

import org.newdawn.slick.geom.Vector2f;

import backend.object.monster.Monster;

public class NearestMatcher extends BestMatcher {
	private Vector2f reference;
	
	public NearestMatcher(Vector2f ref) {
		this.reference = ref;
	}

	@Override
	protected Monster best(Monster m1, Monster m2) {
		return (this.reference.distance(m1.getCenterPosition()) < this.reference.distance(m2.getCenterPosition())) ? m1: m2;
	}

}
