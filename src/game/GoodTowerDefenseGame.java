package game;

import java.awt.Dimension;
import java.util.ListResourceBundle;

import local.EnglishLocal;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import backend.Config;

public class GoodTowerDefenseGame extends StateBasedGame {
	private static GoodTowerDefenseGame instance = new GoodTowerDefenseGame();

	public static final String GAME_NAME = "Good Tower Defense Game";
	public static final String GAME_VERSION = "0.0";
	public static final Dimension DEFAULT_CELL_SIZE = new Dimension(40, 40);
	public ListResourceBundle ressources = new EnglishLocal();
	private PlayState pgs;
	private StartScreenState sss;
	private Input input;

	public ListResourceBundle getLanguage() {
		return this.ressources;
	}

	public StartScreenState getStartScreenState() {
		return this.sss;
	}

	public PlayState getPlayState() {
		return this.pgs;
	}

	public static GoodTowerDefenseGame getInstance() {
		return instance;
	}

	public Input getInput() {
		return this.input;
	}

	@Override
	public void setInput(final Input in) {
		this.input = in;
	}

	private GoodTowerDefenseGame() {
		super(String.format("%1$s v.%2$s", GAME_NAME, GAME_VERSION));
	}

	public static void main(final String[] args) {
		try {
			final GoodTowerDefenseGame game = GoodTowerDefenseGame
					.getInstance();
			final AppGameContainer app = new AppGameContainer(game);
			app.setDisplayMode(Config.WINDOW_SIZE.width,
					Config.WINDOW_SIZE.height, false);
			app.setVSync(true);
			app.setShowFPS(false);
			app.setAlwaysRender(true);
			app.start();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(final GameContainer container)
			throws SlickException {
		this.pgs = new PlayState();
		this.sss = new StartScreenState();
		this.addState(sss);
		this.addState(pgs);

	}
}