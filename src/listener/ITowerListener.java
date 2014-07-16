package listener;

import backend.object.tower.Tower;

public interface ITowerListener extends IListener {
	void onUpgrade(Tower t, int lvl);

	void onSell(Tower t, int revenues);
}
