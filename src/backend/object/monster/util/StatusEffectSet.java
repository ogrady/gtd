package backend.object.monster.util;

import java.util.ArrayList;

import backend.object.monster.statuseffect.StatusEffect;

public class StatusEffectSet extends ArrayList<StatusEffect> {
	private static final long serialVersionUID = 1L;

	public boolean contains(final StatusEffect e) {
		int i = 0;
		while (i < this.size() && !this.get(i).getClass().equals(e.getClass())) {
			i++;
		}
		return i < this.size();
	}

	@Override
	public boolean add(final StatusEffect e) {
		boolean success = false;
		if (!this.contains(e)) {
			success = super.add(e);
		}
		return success;
	}

	public boolean remove(final StatusEffect e) {
		int i = 0;
		boolean found = false;
		while (i < this.size() && !found) {
			found = this.get(i).getClass().equals(e.getClass());
			if (found) {
				this.remove(i);
			}
			i++;
		}
		return found;
	}
}
