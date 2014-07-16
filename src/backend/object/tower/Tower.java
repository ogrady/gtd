package backend.object.tower;

import frontend.gui.FloatingText;
import frontend.gui.MenuItem;
import frontend.gui.RingMenu;
import frontend.renderer.ConstructionSiteRenderer;
import frontend.renderer.PositionableDefaultRenderer;
import game.GoodTowerDefenseGame;
import game.PlayState;

import java.util.ArrayList;

import listener.IListenable;
import listener.IMonsterListener;
import listener.IPlayStateListener;
import listener.ITowerListener;
import listener.ListenerSet;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import util.Const;
import util.IDamageSource;
import backend.object.Positionable;
import backend.object.monster.Monster;
import backend.object.tower.bullet.Projectile;
import backend.object.tower.matcher.FurthestMatcher;
import backend.object.tower.matcher.Matcher;
import backend.player.Player;
import backend.playingfield.GridCoordinate;
import backend.playingfield.GroundType;
import backend.playingfield.PlayingField;
import backend.playingfield.PlayingFieldMetaCell;

/**
 * {@link Tower}s are structures that can be built on the {@link PlayingField}
 * and are the main-damage-dealers in the game.<br>
 * 
 * @author Daniel
 * 
 */
abstract public class Tower extends backend.object.Structure implements
		IDamageSource, IPlayStateListener, IMonsterListener, MouseListener,
		IListenable<ITowerListener> {
	protected ArrayList<GroundType> placeableTerrain;
	protected Monster target;
	protected Matcher matcher;
	protected ListenerSet<ITowerListener> listeners;
	protected float range;
	protected long shootDelay;
	protected long shootDelayAccu;
	protected long lastShot;
	protected long buildDelay;
	protected long buildDelayAccu;
	protected boolean building;
	protected int level;
	protected int basePrice;
	protected boolean acceptingInput;
	protected Player owner;

	/**
	 * @return the price to build or upgrade the {@link Tower}
	 */
	public int getBuildPrice() {
		return this.basePrice * (this.level + 1);
	}

	/**
	 * @return the amount of money we get back for selling the {@link Tower}
	 */
	public int getSellPrice() {
		return (int) Math.floor(this.getBuildPrice() * 0.75);
	}

	/**
	 * @return the total delay in milliseconds until a build is done (upgrade or
	 *         initial build). The time should be maintained by the subclasses
	 *         via the {@link #levelUp()}-method, as different {@link Tower}s
	 *         have different times to be built. This value can always be
	 *         retrieved - even if the {@link Tower} is currently not in
	 *         building-state - and only changes upon leveling up
	 */
	public long getBuildDelay() {
		return buildDelay;
	}

	/**
	 * @return the current time spent in the building-state. When the value
	 *         reaches {@link #getBuildDelay()}, the tower will call
	 *         {@link #levelUp()}
	 */
	public long getBuildDelayAccu() {
		return buildDelayAccu;
	}

	/**
	 * @return whether the {@link Tower} is currently in building-state. This
	 *         can have two causes: the initial build, when the {@link Tower} is
	 *         placed on the field, or when the {@link Tower} is being upgraded.
	 *         As long as this returns true, the {@link Tower} won't look for
	 *         enemies or attack
	 */
	public boolean isBuilding() {
		return this.building;
	}

	/**
	 * @return a list of {@link GroundType}s this specific {@link Tower} can be
	 *         built on (some {@link Tower}s might be aquatic, whilst others can
	 *         only be built on land)
	 */
	public ArrayList<GroundType> getPlaceableTerrain() {
		return this.placeableTerrain;
	}

	/**
	 * @return the currently used {@link Matcher} to pick the next enemy
	 */
	public Matcher getMatcher() {
		return this.matcher;
	}

	/**
	 * @param m
	 *            a new {@link Matcher} to look for enemies from now on (!=
	 *            null)
	 */
	public void setMatcher(final Matcher m) {
		if (m != null) {
			this.matcher = m;
		}
	}

	/**
	 * @return the range in pixel the {@link Tower} is able to shoot over
	 */
	public float getRange() {
		return this.range;
	}

	/**
	 * @param range
	 *            the new range in pixel the {@link Tower} is able to shoot over
	 */
	public void setRange(final float range) {
		this.range = range;
	}

	/**
	 * @return the delay between two shots in milliseconds
	 */
	public long getShootDelay() {
		return this.shootDelay;
	}

	/**
	 * @param delay
	 *            the new delay in milliseconds between two shots (>= 1)
	 */
	public void setShootDelay(final long delay) {
		this.shootDelay = Math.max(1, delay);
	}

	/**
	 * Constructor
	 * 
	 * @param owner
	 *            owning {@link Player}
	 * @param field
	 *            {@link PlayingField} to stand on
	 * @param coord
	 *            {@link GridCoordinate} to stand on
	 * @param range
	 *            range of the tower
	 * @param shootDelay
	 *            delay between two shots
	 * @param buildDelay
	 *            delay to build or upgrade the tower in ms
	 * @param basePrice
	 *            price to derivate the initial- and upgrade-prices from
	 */
	public Tower(final Player owner, final PlayingField field,
			final GridCoordinate coord, final float range,
			final long shootDelay, final long buildDelay, final int basePrice) {
		super(field, coord);
		this.listeners = new ListenerSet<ITowerListener>();
		this.placeableTerrain = new ArrayList<GroundType>();
		this.range = range;
		this.setShootDelay(shootDelay);
		this.matcher = new FurthestMatcher();
		this.owner = owner;
		this.buildDelay = buildDelay;
		this.basePrice = basePrice;
		GoodTowerDefenseGame.getInstance().getContainer().getInput()
				.addMouseListener(this);
	}

	public void destruct() {
		GoodTowerDefenseGame.getInstance().getContainer().getInput()
				.removeMouseListener(this);
		if (this.target != null) {
			this.target.getListeners().unregisterListener(this);
		}
		this.listeners.unregisterAll();
	}

	public void sell() {
		this.owner.changeMoney(this.getSellPrice());
		this.destruct();
	}

	@Override
	public void onTick(final GoodTowerDefenseGame game, final long ms) {
		if (building) {
			this.buildDelayAccu += ms;
			if (this.buildDelayAccu >= this.buildDelay) {
				this.levelUp();
			}
		} else {
			this.shootDelayAccu += ms;
			if (target == null) {
				this.target = this.getNextTarget();
				if (this.target != null) {
					this.target.getListeners().registerListener(this);
				}
			}
			if (target != null) {
				if (this.shootDelayAccu / this.shootDelay > 0) {
					this.shootDelayAccu = 0;
					this.produceProjectile();
				}
			}
		}
	}

	/**
	 * Starts a upgrade. This will put the {@link Tower} in the building-state
	 * and change the renderer
	 * 
	 * @return
	 */
	protected boolean startUpgrade() {
		boolean buildingStarted = false;
		if (this.owner.getMoney() >= this.getBuildPrice()) {
			this.owner.changeMoney(-this.getBuildPrice());
			this.building = true;
			this.setRenderer(new ConstructionSiteRenderer(this));
			buildingStarted = true;
		}
		return buildingStarted;
	}

	/**
	 * Called when the {@link Tower} exits the building-state. This will
	 * increase the level of the tower, reset the buildDelayAccu and reset the
	 * renderer. Subclasses can append to this method to change the values, such
	 * as selling price, power and such, but should always call the
	 * parent-version of the function, to ensure mainting the basic
	 * functionality of the method
	 */
	protected void levelUp() {
		this.level++;
		this.buildDelayAccu = 0;
		this.building = false;
		this.setRenderer(new PositionableDefaultRenderer(this));
	}

	/**
	 * @return the next best enemy to target as determined by the currently used
	 *         {@link Matcher}
	 */
	public Monster getNextTarget() {
		final ArrayList<Monster> inrange = new ArrayList<Monster>();
		for (final Monster m : GoodTowerDefenseGame.getInstance()
				.getPlayState().getPlayingField().getActiveMonsters()) {
			if (this.inRange(m)) {
				inrange.add(m);
			}
		}
		return matcher.getMatch(inrange);
	}

	/**
	 * @param pos
	 *            a position to check
	 * @return whether the passed position is within the range of the
	 *         {@link Tower}
	 */
	public boolean inRange(final Vector2f pos) {
		return pos.distance(this.getCenterPosition()) <= this.range;
	}

	/**
	 * @param pos
	 *            the {@link Positionable} to check
	 * @return whether the passed {@link Positionable} is in range of the
	 *         {@link Tower}
	 */
	public boolean inRange(final Positionable pos) {
		return this.inRange(pos.getCenterPosition());
	}

	/**
	 * @param cell
	 *            the {@link PlayingFieldMetaCell} to check, whether this
	 *            {@link Tower} can be built on it
	 * @return true, if the {@link Tower} can be built on the passed cell, which
	 *         is the case, when no {@link Tower} is built on it yet and the
	 *         ground is fine for the {@link Tower} (see:
	 *         {@link #getPlaceableTerrain()})
	 */
	public boolean canBeBuiltOn(final PlayingFieldMetaCell cell) {
		return cell.getTower() == null && this.canStandOn(cell);
	}

	/**
	 * @param gt
	 *            the {@link GroundType} to check against
	 * @return whether this {@link Tower} can stand on the passed
	 *         {@link GroundType}, determined via {@link #getPlaceableTerrain()}
	 */
	public boolean canStandOn(final GroundType gt) {
		return gt != null && this.placeableTerrain.contains(gt);
	}

	/**
	 * @param cell
	 *            the {@link PlayingFieldMetaCell} to check against
	 * @return whether this {@link Tower} can stand on the passed
	 *         {@link PlayingFieldMetaCell}
	 */
	public boolean canStandOn(final PlayingFieldMetaCell cell) {
		return this.canStandOn(cell.getGroundType());
	}

	/**
	 * @param field
	 *            the {@link PlayingField} to extract the
	 *            {@link PlayingFieldMetaCell} from
	 * @param gc
	 *            the {@link GridCoordinate} we are looking for the
	 *            {@link PlayingFieldMetaCell} on
	 * @return whether this {@link Tower} can stand on the
	 *         {@link PlayingFieldMetaCell} on the passed {@link GridCoordinate}
	 */
	public boolean canStandOn(final PlayingField field, final GridCoordinate gc) {
		return this.canStandOn(field.getCellAt(gc));
	}

	/**
	 * @param field
	 *            the {@link PlayingField} to extract the
	 *            {@link PlayingFieldMetaCell} from
	 * @param pos
	 *            the position we are looking for the
	 *            {@link PlayingFieldMetaCell} on
	 * @return whether this {@link Tower} can stand on the
	 *         {@link PlayingFieldMetaCell} on the passed position
	 */
	public boolean canStandOn(final PlayingField field, final Vector2f pos) {
		return this.canStandOn(field.getCellAt(pos));
	}

	/**
	 * Makes the {@link Tower} spawn a new {@link Projectile}, which must be
	 * overriden by the subclasses as every {@link Tower} spawns its very own
	 * {@link Projectile}-types
	 * 
	 * @return the spawned {@link Projectile}
	 */
	abstract public Projectile produceProjectile();

	@Override
	public void onWaveStart(final int wave) {
	}

	@Override
	public void onTakeDamage(final Monster mon, final IDamageSource src) {
	}

	@Override
	public void onDie(final Monster mon, final IDamageSource cause) {
		if (mon == this.target) {
			this.target = null;
		}
		if (cause == this) {
			this.owner.changeMoney(mon.getReward());
		}
		mon.getListeners().unregisterListener(this);
	}

	@Override
	public void onReachedStronghold(final Monster mon) {
		if (mon == this.target) {
			this.target = null;
		}
		mon.getListeners().unregisterListener(this);
	}

	@Override
	public void onMove(final Monster mon, final Vector2f oldPos,
			final Vector2f newPos) {
	}

	@Override
	public void setInput(final Input input) {

	}

	@Override
	public boolean isAcceptingInput() {
		// level = 0 -> it's just a hoverdummy
		return this.level > 0 && !this.isBuilding();
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
	}

	@Override
	public void mousePressed(final int button, final int x, final int y) {
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		final Rectangle r = new Rectangle(vp.x + this.position.x, vp.y
				+ this.position.y, this.getRepresentationWidth(),
				this.getRepresentationHeight());
		if (r.contains(x, y)) {
			final PlayState pgs = GoodTowerDefenseGame.getInstance()
					.getPlayState();
			final RingMenu rm = new RingMenu(getCenterPosition());
			try {
				rm.addMenuItem(new MenuItem(new Image(Const.PATH_SELL_ICON)) {

					@Override
					public void onClick() {
						final GoodTowerDefenseGame game = GoodTowerDefenseGame
								.getInstance();
						Tower.this.field.addFloatingText(new FloatingText(game,
								game.ressources
										.getString(Const.L_NOT_IMPLEMENTED)));
					}
				});
				rm.addMenuItem(new MenuItem(new Image(Const.PATH_UPGRADE_ICON)) {

					@Override
					public void onClick() {
						final GoodTowerDefenseGame game = GoodTowerDefenseGame
								.getInstance();
						Tower.this.field.addFloatingText(new FloatingText(game,
								game.ressources
										.getString(Const.L_NOT_IMPLEMENTED)));
					}
				});
			} catch (final SlickException e) {
				e.printStackTrace();
			}
			pgs.setRingMenu(rm);
		}

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

	@Override
	public ListenerSet<ITowerListener> getListeners() {
		return this.listeners;
	}
}
