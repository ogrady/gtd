package backend.object.buildings;

import game.GoodTowerDefenseGame;

import java.util.Iterator;

import listener.IListenable;
import listener.IStrongholdListener;
import listener.ListenerSet;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Const;
import util.IDamageSource;
import util.IDamageable;
import backend.playingfield.GridCoordinate;
import backend.playingfield.PlayingField;

public class Stronghold extends backend.object.Structure implements
		IDamageable, IListenable<IStrongholdListener> {
	private final ListenerSet<IStrongholdListener> listeners;

	public Stronghold(final PlayingField field, final GridCoordinate coord,
			final int life) throws SlickException {
		super(field, coord);
		this.listeners = new ListenerSet<IStrongholdListener>();
		this.representation = new Image(Const.PATH_STRONGHOLD);
		GoodTowerDefenseGame.getInstance().getPlayState().getPlayingField()
				.getStrongholds().add(this);
	}

	@Override
	public void takeDamage(final IDamageSource src, final float damage) {
		final Iterator<IStrongholdListener> it = this.listeners.iterator();
		while (it.hasNext()) {
			it.next().onTakeDamage(src, damage);
		}
	}

	@Override
	public ListenerSet<IStrongholdListener> getListeners() {
		return this.listeners;
	}
}
