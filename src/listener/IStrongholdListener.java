package listener;

import util.IDamageSource;

public interface IStrongholdListener extends IListener {
	void onTakeDamage(IDamageSource src, float amount);
}
