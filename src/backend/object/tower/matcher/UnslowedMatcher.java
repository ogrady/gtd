package backend.object.tower.matcher;

import backend.object.monster.Monster;
import backend.object.monster.statuseffect.SlowEffect;


public class UnslowedMatcher extends AnyMatcher {

	@Override
	public boolean matches(Monster m) {
		return !m.getEffects().contains(new SlowEffect());
	}
}
