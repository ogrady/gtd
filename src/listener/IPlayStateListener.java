package listener;

import game.GoodTowerDefenseGame;

public interface IPlayStateListener extends IListener {
	void onWaveStart(int wave);

	void onTick(GoodTowerDefenseGame game, long delta);
}
