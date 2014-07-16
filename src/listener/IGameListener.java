package listener;

import game.GoodTowerDefenseGame;

public interface IGameListener extends IListener {
	void onTick(GoodTowerDefenseGame game, long ms);
}
