package backend.object.tower.bullet;

import game.GoodTowerDefenseGame;
import listener.IListenable;
import listener.IProjectileListener;
import listener.ListenerSet;
import backend.object.monster.Monster;
import backend.object.tower.Tower;

abstract public class Projectile extends backend.object.Mobile implements
		IListenable<IProjectileListener> {
	protected ListenerSet<IProjectileListener> listeners;
	protected Monster target;
	protected Tower source;
	protected float damage;

	public Tower getSource() {
		return this.source;
	}

	public Projectile(final Tower source, final Monster target,
			final float speed, final float dmg) {
		super(source.getCenterPosition().copy(), speed);
		this.listeners = new ListenerSet<IProjectileListener>();
		GoodTowerDefenseGame.getInstance().getPlayState().getPlayingField()
				.getBullets().add(this);
		GoodTowerDefenseGame.getInstance().getPlayState().getListeners()
				.registerListener(this);
		this.source = source;
		this.target = target;
		this.damage = dmg;
	}

	@Override
	public void onTick(final GoodTowerDefenseGame game, final long ms) {
		if (this.moveTowards(target.getCenterPosition())) {
			this.impact();
		}
	}

	protected void impact() {
		GoodTowerDefenseGame.getInstance().getPlayState().getListeners()
				.unregisterListener(this);
		GoodTowerDefenseGame.getInstance().getPlayState().getPlayingField()
				.getBullets().remove(this);
		for (final IProjectileListener pl : this.listeners) {
			pl.onImpact(this, this.position);
		}
	}

	@Override
	public void onWaveStart(final int wave) {
	}

	@Override
	public int getRepresentationWidth() {
		return 0;
	}

	@Override
	public int getRepresentationHeight() {
		return 0;
	}

	@Override
	public ListenerSet<IProjectileListener> getListeners() {
		return this.listeners;
	}

}
