package backend.player;

import java.util.Iterator;

import listener.IListenable;
import listener.IPlayerListener;
import listener.ListenerSet;

public class Player implements IListenable<IPlayerListener> {
	private int money;
	private int life;
	private Team team;
	private final ListenerSet<IPlayerListener> listeners;

	public void changeMoney(final int delta) {
		if (delta != 0) {
			this.setMoney(this.money + delta);
		}
	}

	public void changeLife(final int delta) {
		if (delta != 0) {
			this.setLife(this.life + delta);
		}
	}

	public void setMoney(final int money) {
		final int old = this.money;
		this.money = money;
		final Iterator<IPlayerListener> it = this.listeners.iterator();
		while (it.hasNext()) {
			it.next().onChangeMoney(old, this.money);
		}
	}

	public void setLife(final int life) {
		final int old = this.life;
		this.life = life;
		final Iterator<IPlayerListener> it = this.listeners.iterator();
		while (it.hasNext()) {
			it.next().onChangeLife(old, this.life);
		}
	}

	public Team getTeam() {
		return this.team;
	}

	public int getLife() {
		return this.life;
	}

	public int getMoney() {
		return this.money;
	}

	public Player() {
		this.listeners = new ListenerSet<IPlayerListener>();
		this.money = 2000;
		this.life = 100;
	}

	@Override
	public ListenerSet<IPlayerListener> getListeners() {
		return this.listeners;
	}
}
