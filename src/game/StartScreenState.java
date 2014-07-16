package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import util.Const;

public class StartScreenState extends BasicGameState implements KeyListener {

	@Override
	public void init(final GameContainer container, final StateBasedGame game)
			throws SlickException {

	}

	@Override
	public void render(final GameContainer container,
			final StateBasedGame game, final Graphics g) throws SlickException {
		g.setColor(Color.blue);
		g.drawRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(Color.green);
		g.drawString("menu", 100, 100);
		container.getInput().addKeyListener(this);

	}

	@Override
	public void update(final GameContainer container,
			final StateBasedGame game, final int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			game.enterState(Const.STATE_PLAY, new FadeOutTransition(),
					new FadeInTransition());
		}

	}

	@Override
	public int getID() {
		return Const.STATE_START_SCREEN;
	}

}
