package game;

import java.awt.Dimension;

import listener.IListenable;
import listener.IPlayStateListener;
import listener.IPlayerListener;
import listener.IRingMenuListener;
import listener.IStrongholdListener;
import listener.ListenerSet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Const;
import util.IDamageSource;
import backend.object.buildings.Spawner;
import backend.object.buildings.Stronghold;
import backend.object.monster.Blob;
import backend.object.monster.TestMonster;
import backend.object.tower.HoverDummy;
import backend.player.Player;
import backend.playingfield.GridCoordinate;
import backend.playingfield.PlayingField;
import backend.playingfield.PlayingFieldParser;
import exception.InitException;
import exception.MapParserException;
import exception.PlayingFieldException;
import frontend.gui.RingMenu;
import frontend.gui.gameinterface.FrontendInterface;

public class PlayState extends BasicGameState implements IPlayerListener,
		IRingMenuListener, IListenable<IPlayStateListener>, IStrongholdListener {
	public static final int FIELD_PAN = 50;
	public static final float PAN_SPEED = 3f;
	private GoodTowerDefenseGame game;
	private PlayingField field;
	private RingMenu ringMenu;
	private HoverDummy hoverdummy;
	private Vector2f viewport;
	private int currentWave;
	private ListenerSet<IPlayStateListener> listeners;
	private FrontendInterface gui;
	private Player player;

	private void startNextWave() {
		this.currentWave++;
		for (final IPlayStateListener pl : this.listeners) {
			pl.onWaveStart(this.currentWave);
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	@Override
	public ListenerSet<IPlayStateListener> getListeners() {
		return this.listeners;
	}

	public int getCurrentWaveNumber() {
		return this.currentWave;
	}

	public Vector2f getViewport() {
		return this.viewport;
	}

	public void setViewport(final Vector2f vp) {
		this.viewport = vp;
	}

	public PlayingField getPlayingField() {
		return this.field;
	}

	public void setPlayingField(final PlayingField field) {
		this.field = field;
	}

	public HoverDummy getHoverDummy() {
		return this.hoverdummy;
	}

	public void setHoverDummy(final HoverDummy hov) {
		this.hoverdummy = hov;
		if (hov != null) {
			game.getContainer().getInput().removeMouseListener(hov);
			game.getContainer().getInput().addMouseListener(hov);
		}
	}

	public void setRingMenu(final RingMenu rm) {
		if (this.ringMenu != null) {
			game.getContainer().getInput().removeMouseListener(this.ringMenu);
			this.ringMenu.getListeners().remove(this);
		}
		if (rm != null) {
			rm.getListeners().add(this);
			game.getContainer().getInput().addMouseListener(rm);
		}
		this.ringMenu = rm;
	}

	public RingMenu getRingMenu() {
		return this.ringMenu;
	}

	@Override
	public void init(final GameContainer container, final StateBasedGame game)
			throws SlickException {
		this.listeners = new ListenerSet<IPlayStateListener>();
		this.game = (GoodTowerDefenseGame) game;
		this.viewport = new Vector2f(0, 0);
		this.player = new Player();
		this.player.getListeners().registerListener(this);
		final String path = "map/Level1Desert/tiled_map_desert_1.tmx";
		final PlayingFieldParser parser = new PlayingFieldParser();
		try {
			this.setPlayingField(parser.parse(path, "map/Level1Desert"));
		} catch (final MapParserException e) {
			e.printStackTrace();
		}
		final PlayingField field = this.getPlayingField();
		new Stronghold(field, new GridCoordinate(field, 12, 14), 100);
		final Spawner sp1 = new Spawner(field, new GridCoordinate(field, 5, 3));
		sp1.queue(1, new Blob(sp1));
		for (int i = 0; i < 200; i++) {
			sp1.queue(1, 300, new Blob(sp1));
		}
		// sp1.queue(1, new TestMonster(sp1));
		sp1.queue(3, new TestMonster(sp1));
		sp1.queue(1, 10000, new TestMonster(sp1));
		sp1.queue(1, new TestMonster(sp1));
		sp1.queue(1, new TestMonster(sp1));
		sp1.queue(1, new TestMonster(sp1));
		for (int i = 0; i < 10000; i++) {
			sp1.queue(1, 4000, new TestMonster(sp1));
		}

		if (field == null) {
			throw new InitException("No field defined yet");
		}
		if (field.getSpawners().size() <= 0) {
			throw new InitException("Need at least one spawner to start");
		}
		if (field.getStrongholds().size() <= 0) {
			throw new InitException("Need at leasts one stronghold to start");
		}

		for (final Stronghold s : field.getStrongholds()) {
			s.getListeners().registerListener(this);
		}
		this.gui = new FrontendInterface(
				new Dimension(container.getWidth(), 30));
		this.gui.money.setText(Integer.toString(this.player.getMoney()));
		this.gui.life.setText(Integer.toString(this.player.getLife()));
		this.startNextWave();
	}

	@Override
	public void render(final GameContainer container,
			final StateBasedGame game, final Graphics g) throws SlickException {
		this.getPlayingField().render(container, g);
		final HoverDummy hd = this.getHoverDummy();
		if (hd != null && hd.isAcceptingInput()) {
			try {
				hd.render(container, g);
			} catch (final PlayingFieldException pfe) {
				// player dragged the dummy out of the field. Not an issue. Just
				// don't render and proceed
			}
		}
		if (ringMenu != null) {
			ringMenu.render(container, g);
		}
		this.gui.paintAll(g);

	}

	@Override
	public void update(final GameContainer gc, final StateBasedGame game,
			final int delta) throws SlickException {
		this.game.setInput(gc.getInput());
		final Input input = gc.getInput();
		final int mx = input.getMouseX();
		final int my = input.getMouseY();
		// panning
		final Vector2f vp = this.getViewport();
		final PlayingField field = this.getPlayingField();
		if (mx < FIELD_PAN && vp.x < 0) {
			vp.x += PAN_SPEED;
		} else if (mx > gc.getWidth() - FIELD_PAN
				&& vp.x > -(field.getWidthInPixels() - gc.getWidth())) {
			vp.x -= PAN_SPEED;
		}
		if (my < FIELD_PAN && vp.y < 0) {
			vp.y += PAN_SPEED;
		} else if (my > gc.getHeight() - FIELD_PAN
				&& vp.y > -(field.getHeightInPixels() - gc.getHeight())) {
			vp.y -= PAN_SPEED;
		}

		/*if (input.isKeyPressed(Input.KEY_1)) {
			final HoverDummy hd = new HoverDummy(new GunTower(field,
					new GridCoordinate(field, 0, 0)));
			this.setHoverDummy(hd);
		}
		if (input.isKeyPressed(Input.KEY_2)) {
			final HoverDummy hd = new HoverDummy(new SlowTower(field,
					new GridCoordinate(field, 0, 0)));
			this.setHoverDummy(hd);
		}*/
		for (final IPlayStateListener pl : this.getListeners()) {
			pl.onTick(this.game, delta);
		}
	}

	@Override
	public int getID() {
		return Const.STATE_PLAY;
	}

	@Override
	public void onRingMenuClose() {
		this.setRingMenu(null);
	}

	@Override
	public void onChangeMoney(final int oldAmount, final int newAmount) {
		this.gui.money.setText(Integer.toString(newAmount));
	}

	@Override
	public void onChangeLife(final int oldAmount, final int newAmount) {
		this.gui.life.setText(Integer.toString(newAmount));

	}

	@Override
	public void onTakeDamage(final IDamageSource src, final float amount) {
		this.player.changeLife((int) -amount);
	}
}
