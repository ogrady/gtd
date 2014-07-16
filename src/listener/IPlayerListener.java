package listener;

public interface IPlayerListener extends IListener {
	void onChangeMoney(int oldAmount, int newAmount);

	void onChangeLife(int oldAmount, int newAmount);
}
