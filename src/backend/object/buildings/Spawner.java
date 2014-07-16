package backend.object.buildings;

import game.GoodTowerDefenseGame;

import java.util.ArrayList;

import listener.IPlayStateListener;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Const;
import backend.object.monster.Monster;
import backend.object.monster.util.WaitingMonster;
import backend.object.monster.util.Wave;
import backend.playingfield.GridCoordinate;
import backend.playingfield.PlayingField;

public class Spawner extends backend.object.Structure implements
		IPlayStateListener {
	public static final long DEFAULT_DELAY = 100;
	public static final long DEFAULT_BREATHER = 100000;

	private final ArrayList<Wave> waves;
	private WaitingMonster next;
	private int wave;
	private long accu;

	/**
	 * Constructor
	 * 
	 * @param pos
	 *            position
	 * @throws SlickException
	 */
	public Spawner(final PlayingField field, final GridCoordinate coord)
			throws SlickException {
		super(field, coord);
		this.representation = new Image(Const.PATH_SPAWNER);
		final GoodTowerDefenseGame game = GoodTowerDefenseGame.getInstance();
		field.getSpawners().add(this);
		game.getPlayState().getListeners().registerListener(this);
		this.waves = new ArrayList<Wave>();
	}

	/**
	 * Add a new {@link Monster} to the queue. Adds waves if needed
	 * 
	 * @param wave
	 *            wave in which the {@link Monster} should appear
	 * @param wmon
	 *            {@link WaitingMonster} that encapsulates the {@link Monster}
	 */
	public void queue(final int wave, final WaitingMonster wmon) {
		if (this.waves.size() < wave) {
			this.createWaves(wave);
		}
		this.waves.get(wave).add(wmon);
	}

	/**
	 * @param wave
	 *            wave in which the {@link Monster} should appear
	 * @param delay
	 *            delay before the {@link Monster} spawns
	 * @param mon
	 *            {@link Monster} to spawn
	 */
	public void queue(final int wave, final long delay, final Monster mon) {
		this.queue(wave, new WaitingMonster(delay, mon));
	}

	/**
	 * Takes the DEFAULT_DELAY for the queued {@link Monster}
	 * 
	 * @param wave
	 *            wave in which the {@link Monster} should appear
	 * @param mon
	 *            {@link Monster} to spawn
	 */
	public void queue(final int wave, final Monster mon) {
		this.queue(wave, new WaitingMonster(DEFAULT_DELAY, mon));
	}

	@Override
	public void onTick(final GoodTowerDefenseGame game, final long ms) {
		if (this.next == null) {
			this.next = this.waves.get(this.wave).poll();
		}
		if (this.next != null) {
			this.accu += ms;
			if (this.accu / this.next.getDelay() > 0) {
				this.accu -= this.next.getDelay();
				final Monster mon = this.next.getMonster();
				mon.spawnFrom(this);
				game.getPlayState().getListeners().registerListener(mon);
				this.next = null;
			}
		}
	}

	@Override
	public void onWaveStart(final int wave) {
		this.wave = wave;
		this.createWaves(wave);
	}

	/**
	 * Utility function to fill the {@link Spawner} up with {@link Wave}s.
	 * Creates empty {@link Wave}s which is useful in case the {@link Spawner}
	 * should be idle for a round - but not throw an exception.
	 * 
	 * @param upto
	 *            highest index of {@link Wave}s. Note that upto = 3 won't
	 *            create 3 {@link Wave}s but rather adds waves until the
	 *            wavecount is 3.
	 */
	private void createWaves(final int upto) {
		for (int i = this.waves.size(); i < upto + 1; i++) {
			this.waves.add(new Wave());
		}
	}
}
