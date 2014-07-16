package backend.object.monster;

import java.util.ArrayList;

import listener.IListenable;
import listener.IMonsterListener;
import listener.ListenerSet;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.Mover;

import util.IDamageSource;
import util.IDamageable;
import backend.object.Bar;
import backend.object.MonsterHealthBar;
import backend.object.buildings.Spawner;
import backend.object.buildings.Stronghold;
import backend.object.monster.statuseffect.StatusEffect;
import backend.object.monster.util.Path;
import backend.object.monster.util.StatusEffectSet;
import backend.player.Player;
import backend.playingfield.GroundType;
import backend.playingfield.PlayingField;
import frontend.renderer.MonsterBarRenderer;
import frontend.renderer.MonsterDefaultRenderer;
import game.GoodTowerDefenseGame;

/**
 * {@link Monster}s are mobile entities that move from the {@link Spawner} to
 * the players {@link Stronghold} to damage it and ultimately make the player
 * lose the game
 * 
 * @author Daniel
 * 
 */
abstract public class Monster extends backend.object.Mobile implements
		MouseListener, Mover, IDamageable, IDamageSource,
		IListenable<IMonsterListener> {
	protected Path path;
	protected Float maxHitpoints;
	protected Float hitpoints;
	protected float attackPower;
	protected float direction;
	protected StatusEffectSet effects;
	protected ArrayList<GroundType> walkableTerrain;
	protected ArrayList<GroundType> hatedTerrain;
	protected ListenerSet<IMonsterListener> listeners;
	protected Bar healthbar;
	protected Input input;
	protected boolean acceptingInput;
	protected Stronghold targetStronghold;
	protected int reward;

	/**
	 * @return currency the {@link Player} receives for killing this
	 *         {@link Monster}
	 */
	public int getReward() {
		return this.reward;
	}

	/**
	 * @return a list of {@link GroundType}s the {@link Monster} won't walk on
	 */
	public ArrayList<GroundType> getHatedTerrain() {
		return this.hatedTerrain;
	}

	/**
	 * @return a list of {@link GroundType}s the {@link Monster} can walk on
	 */
	public ArrayList<GroundType> getWalkableTerrain() {
		return this.walkableTerrain;
	}

	/**
	 * @return a list of {@link StatusEffect}s the {@link Monster} is currently
	 *         suffering from
	 */
	public StatusEffectSet getEffects() {
		return this.effects;
	}

	/**
	 * @return the {@link Path} the {@link Monster} is currently following
	 */
	public Path getPath() {
		return this.path;
	}

	/**
	 * @param p
	 *            the new {@link Path} the {@link Monster} should now follow
	 */
	public void setPath(final Path p) {
		this.path = p;
	}

	/**
	 * @return the max amount of hitpoints this {@link Monster} has
	 */
	public Float getMaxHitpoints() {
		return maxHitpoints;
	}

	/**
	 * @param maxHitpoints
	 *            the new max amount of hitpoints
	 */
	public void setMaxHitpoints(final Float maxHitpoints) {
		this.maxHitpoints = maxHitpoints;
	}

	/**
	 * @return the current amount of hitpoints
	 */
	public Float getHitpoints() {
		return hitpoints;
	}

	/**
	 * @param hitpoints
	 *            the new current amount of hitpoints
	 */
	public void setHitpoints(final Float hitpoints) {
		this.hitpoints = hitpoints;
	}

	/**
	 * @return the monsters attack-power. This is the amount of damage the
	 *         {@link Monster} will inflict once it reaches the
	 *         {@link Stronghold}
	 */
	public float getAttackPower() {
		return attackPower;
	}

	/**
	 * @param attackPower
	 *            changes the {@link Monster}s attack-power
	 */
	public void setAttackPower(final float attackPower) {
		this.attackPower = attackPower;
	}

	/**
	 * @return the direction the {@link Monster} is currently facing
	 */
	public float getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the new direction the {@link Monster} is facing
	 */
	public void setDirection(final float direction) {
		this.direction = direction;
	}

	/**
	 * @return the {@link Bar} that is depicting the currentHP / maxHP ratio
	 */
	public Bar getHealthBar() {
		return this.healthbar;
	}

	/**
	 * Constructor
	 * 
	 * @param pos
	 *            initial position
	 * @param hp
	 *            maxHp
	 * @param spd
	 *            speed
	 * @param atk
	 *            attackpower
	 * @param dir
	 *            initial direction
	 * @param reward
	 *            reward for killing the {@link Monster}
	 */
	public Monster(final Vector2f pos, final float hp, final float spd,
			final float atk, final float dir, final int reward) {
		super(pos, spd);
		// this.renderer = new MonsterDefaultRenderer(this);
		this.renderer = new MonsterBarRenderer(this);
		this.listeners = new ListenerSet<IMonsterListener>();
		this.walkableTerrain = new ArrayList<GroundType>();
		this.hatedTerrain = new ArrayList<GroundType>();
		this.effects = new StatusEffectSet();
		this.maxHitpoints = hp;
		this.hitpoints = hp;
		this.attackPower = atk;
		this.direction = dir;
		this.reward = reward;
		this.healthbar = new MonsterHealthBar(this);
	}

	/**
	 * Constructor
	 * 
	 * @param sp
	 *            the {@link Spawner} to spawn from
	 * @param hp
	 *            maxHp
	 * @param spd
	 *            speed
	 * @param atk
	 *            attackpower
	 * @param dir
	 *            initial direction
	 */
	public Monster(final Spawner sp, final float hp, final float spd,
			final float atk, final float dir, final int reward) {
		this(sp.getPosition(), hp, spd, atk, dir, reward);
	}

	/**
	 * Move along the {@link Path} the {@link Monster} is currently following
	 */
	public void moveOn() {
		if (!this.path.isDone()) {
			boolean reached = false;
			reached = this.moveTowards(this.path.getNextTarget()
					.getCenterPosition());
			if (reached) {
				this.path.advance();
			}
		} else {
			this.reachStronghold();
		}
	}

	/**
	 * @param sp
	 *            the {@link Spawner} the {@link Monster} should spawn from
	 */
	public void spawnFrom(final Spawner sp) {
		this.position = sp.getCenterPosition().copy();
		final GoodTowerDefenseGame game = GoodTowerDefenseGame.getInstance();
		final PlayingField field = game.getPlayState().getPlayingField();
		this.targetStronghold = field.getRandomStronghold();
		this.path = field.getPathMatrix()
				.getPath(this, sp, this.targetStronghold).copy();
		field.getActiveMonsters().add(this);
		game.getPlayState().getListeners().add(this);
		game.getInput().addMouseListener(this);
		// set to true when we implement diverse renderers
		this.acceptingInput = false;
	}

	/**
	 * Called, when the {@link Monster} reaches the {@link Stronghold} and
	 * damages it. It will then remove itself from the game
	 */
	public void reachStronghold() {
		this.targetStronghold.takeDamage(this, this.attackPower);
		for (final IMonsterListener ml : this.listeners) {
			ml.onReachedStronghold(this);
		}
		final GoodTowerDefenseGame game = GoodTowerDefenseGame.getInstance();
		game.getPlayState().getListeners().unregisterListener(this);
		game.getPlayState().getPlayingField().getActiveMonsters().remove(this);
		this.acceptingInput = false;
	}

	/**
	 * Called, when the {@link Monster} dies from damage
	 * 
	 * @param cause
	 *            the damage source that caused the {@link Monster} to die
	 */
	public void die(final IDamageSource cause) {
		for (final IMonsterListener ml : this.listeners) {
			ml.onDie(this, cause);
		}
		final GoodTowerDefenseGame game = GoodTowerDefenseGame.getInstance();
		game.getPlayState().getListeners().unregisterListener(this);
		game.getPlayState().getPlayingField().getActiveMonsters().remove(this);
		this.acceptingInput = false;
	}

	@Override
	public boolean moveTowards(final Vector2f to) {
		final Vector2f oldPos = this.position.copy();
		final boolean reached = super.moveTowards(to);
		final Vector2f newPos = this.position.copy();
		for (final IMonsterListener ml : this.listeners) {
			ml.onMove(this, oldPos, newPos);
		}
		return reached;
	}

	@Override
	public void takeDamage(final IDamageSource src, final float damage) {
		this.hitpoints -= damage;
		if (this.hitpoints <= 0) {
			this.die(src);
		}
	}

	@Override
	public int getRepresentationWidth() {
		return ((Image) this.representation).getWidth();
	}

	@Override
	public int getRepresentationHeight() {
		return ((Image) this.representation).getHeight();
	}

	@Override
	public void onTick(final GoodTowerDefenseGame game, final long ms) {
		this.moveOn();
	}

	@Override
	public void onWaveStart(final int wave) {
	}

	@Override
	public ListenerSet<IMonsterListener> getListeners() {
		return this.listeners;
	}

	@Override
	public void setInput(final Input input) {
		this.input = input;
	}

	@Override
	public boolean isAcceptingInput() {
		return this.acceptingInput;
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public void mouseWheelMoved(final int change) {
	}

	@Override
	public void mouseClicked(final int button, final int x, final int y,
			final int clickCount) {
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		if (this.renderer.collides(new Vector2f(x + vp.x, y + vp.y))) {
			this.setRenderer(new MonsterBarRenderer(this));
		} else {
			this.setRenderer(new MonsterDefaultRenderer(this));
		}
	}

	@Override
	public void mousePressed(final int button, final int x, final int y) {
	}

	@Override
	public void mouseReleased(final int button, final int x, final int y) {
	}

	@Override
	public void mouseMoved(final int oldx, final int oldy, final int newx,
			final int newy) {
	}

	@Override
	public void mouseDragged(final int oldx, final int oldy, final int newx,
			final int newy) {
	}
}
