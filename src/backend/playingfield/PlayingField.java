package backend.playingfield;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import listener.IFloatingTextListener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.InputAdapter;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import util.Const;
import util.IRenderable;
import util.IRenderer;
import backend.object.buildings.Spawner;
import backend.object.buildings.Stronghold;
import backend.object.monster.Monster;
import backend.object.tower.GunTower;
import backend.object.tower.HoverDummy;
import backend.object.tower.SlowTower;
import backend.object.tower.SplashTower;
import backend.object.tower.Tower;
import backend.object.tower.bullet.Projectile;
import exception.PlayingFieldException;
import frontend.gui.FloatingText;
import frontend.gui.MenuItem;
import frontend.gui.RingMenu;
import game.GoodTowerDefenseGame;
import game.PlayState;

/**
 * {@link PlayingField} is the map the game is being played on. It is not just
 * the grid of cells a player can place towers on but rather a complete
 * collection of everything that is currently going on. As such it holds
 * information about the entities on the map:
 * <ul>
 * <li>all active {@link Monster}s</li>
 * <li>the flying {@link Projectile}s</li>
 * <li>structures, such as {@link Spawner}s and {@link Stronghold}s</li>
 * </ul>
 * And furthermore meta-information, such as as {@link PathMatrix} to facilitate
 * path-finding.
 * 
 * @author Daniel
 * 
 */
public class PlayingField extends InputAdapter implements TileBasedMap,
		IRenderable, IFloatingTextListener {
	private static final int MIN_ROW_COUNT = 2;
	private static final int MIN_COL_COUNT = 2;

	private final Pathfinder pathfinder;
	private final Dimension cellSize;
	private final PlayingFieldMetaCell[][] grid;
	private TiledMap tiledMap;
	private final PathMatrix paths;
	private final ArrayList<Projectile> bullets;
	private final CopyOnWriteArrayList<Monster> activeMonsters;
	private final ArrayList<Tower> builtTowers;
	private final ArrayList<Spawner> spawners;
	private final ArrayList<Stronghold> strongholds;
	private final ArrayList<FloatingText> floatingTexts;

	/**
	 * @return the currently employed {@link PathMatrix}
	 */
	public PathMatrix getPathMatrix() {
		return this.paths;
	}

	/**
	 * @return the underlying {@link TiledMap}
	 */
	public TiledMap getTiledMap() {
		return this.tiledMap;
	}

	/**
	 * @param tm
	 *            the new {@link TiledMap}
	 */
	public void setTiledMap(final TiledMap tm) {
		this.tiledMap = tm;
	}

	/**
	 * @return the {@link PathFinder} used to fill the {@link PathMatrix}
	 */
	public Pathfinder getPathFinder() {
		return this.pathfinder;
	}

	/**
	 * @return the size of one cell in pixel
	 */
	public Dimension getCellSize() {
		return this.cellSize;
	}

	/**
	 * @return the list of currently active {@link FloatingText}s
	 */
	public ArrayList<FloatingText> getFloatingTexts() {
		return this.floatingTexts;
	}

	/**
	 * @return the list of currently active {@link Projectile}s
	 */
	public ArrayList<Projectile> getBullets() {
		return this.bullets;
	}

	/**
	 * @return the list of currently builded {@link Tower}s
	 */
	public ArrayList<Tower> getBuiltTowers() {
		return this.builtTowers;
	}

	/**
	 * @return the list of currently active {@link Monster}s (that excludes
	 *         {@link Monster}s that are waiting inside of a {@link Spawner})
	 */
	public Collection<Monster> getActiveMonsters() {
		return this.activeMonsters;
	}

	/**
	 * @return the list of {@link Spawner}s on the map
	 */
	public ArrayList<Spawner> getSpawners() {
		return this.spawners;
	}

	/**
	 * @return the list of {@link Stronghold}s on the map
	 */
	public ArrayList<Stronghold> getStrongholds() {
		return this.strongholds;
	}

	/**
	 * @return one arbitrary {@link Stronghold} to employ random-spawn or NULL
	 *         if no stronghold exists
	 */
	public Stronghold getRandomStronghold() {
		return this.strongholds.size() > 0 ? this.strongholds.get(new Random()
				.nextInt(this.strongholds.size())) : null;
	}

	/**
	 * @return the number of rows the map-grid holds
	 */
	public int getRows() {
		return this.grid[0].length;
	}

	/**
	 * @return the number of columns the map-grid holds
	 */
	public int getColumns() {
		return this.grid.length;
	}

	@Override
	public int getWidthInTiles() {
		return this.getColumns();
	}

	@Override
	public int getHeightInTiles() {
		return this.getRows();
	}

	/**
	 * @return the total width of the {@link PlayingField} in pixels
	 */
	public int getWidthInPixels() {
		return this.getColumns() * this.cellSize.width;
	}

	/**
	 * @return the total height of the {@link PlayingField} in pixels
	 */
	public int getHeightInPixels() {
		return this.getRows() * this.cellSize.height;
	}

	/**
	 * Adds another {@link FloatingText} to the list of {@link FloatingText}s
	 * and registers the {@link PlayingField} itself as listener for said text,
	 * to remove the text from the field once it reached 0 opacity
	 * 
	 * @param text
	 *            the {@link FloatingText} to add
	 */
	public void addFloatingText(final FloatingText text) {
		this.floatingTexts.add(text);
		text.getListeners().add(this);
	}

	public PlayingField(final Dimension fieldSize, int cols, int rows) {
		this.bullets = new ArrayList<Projectile>();
		this.activeMonsters = new CopyOnWriteArrayList<Monster>();
		this.builtTowers = new ArrayList<Tower>();
		this.spawners = new ArrayList<Spawner>();
		this.strongholds = new ArrayList<Stronghold>();
		this.floatingTexts = new ArrayList<FloatingText>();
		this.paths = new PathMatrix(this);

		rows = Math.max(MIN_ROW_COUNT, rows);
		cols = Math.max(MIN_COL_COUNT, cols);
		this.grid = new PlayingFieldMetaCell[cols][rows];
		this.pathfinder = new Pathfinder(this);
		final int cellWidth = fieldSize.width / cols;
		final int cellHeight = fieldSize.height / rows;
		this.cellSize = new Dimension(cellWidth, cellHeight);
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				this.grid[i][j] = new PlayingFieldMetaCell(new GridCoordinate(
						this, i, j), this.cellSize);
			}
		}
		GoodTowerDefenseGame.getInstance().getContainer().getInput()
				.addMouseListener(this);
	}

	/**
	 * Get a specific cell from the grid
	 * 
	 * @param row
	 *            row count
	 * @param column
	 *            column count
	 * @return cell on the specified coordinates
	 * @throws PlayingFieldException
	 *             if row or column is bigger than the grid size
	 */
	public PlayingFieldMetaCell getCellAt(final int column, final int row) {
		return this.grid[column][row];
	}

	public PlayingFieldMetaCell getCellAt(final GridCoordinate coord)
			throws PlayingFieldException {
		PlayingFieldMetaCell res = null;
		try {
			res = this.getCellAt(coord.getColumn(), coord.getRow());
		} catch (final IndexOutOfBoundsException ioobe) {
			throw new PlayingFieldException(String.format(
					"Could not retrieve a valid cell from %1$d|%2$d",
					coord.getColumn(), coord.getRow()));
		}
		return res;
	}

	public PlayingFieldMetaCell getCellAt(final Vector2f position) {
		final Vector2f viewport = GoodTowerDefenseGame.getInstance()
				.getPlayState().getViewport();
		return this.getCellAt(new GridCoordinate(this,
				(position.x - viewport.x) / this.cellSize.width,
				(position.y - viewport.y) / this.cellSize.height));
	}

	/**
	 * Transforms a {@link Vector2f} into a coordinate
	 * 
	 * @param point
	 *            the {@link Vector2f} to transform to
	 * @return the transformed coordinate
	 */
	public GridCoordinate pointToCoordinate(final Vector2f point) {
		return new GridCoordinate(this, point.x / this.cellSize.width, point.y
				/ this.cellSize.height);
	}

	/**
	 * Calculates the top-left-corner-in-pixel-position of a given coordinate
	 * 
	 * @param coord
	 *            the coordinate to get the position from
	 * @return the top-left-corner-in-pixel-position
	 */
	public Vector2f topLeftPointOf(final GridCoordinate coord) {
		return new Vector2f(coord.getColumn() * this.cellSize.width,
				coord.getRow() * this.cellSize.height);
	}

	/**
	 * Calculates the center in pixel of a given coordinate
	 * 
	 * @param coord
	 *            the coordinate to get the position from
	 * @return the center position in pixel
	 */
	public Vector2f centerPointOf(final GridCoordinate coord) {
		final int w = this.cellSize.width;
		final int h = this.cellSize.height;
		return new Vector2f(coord.getRow() * h + h / 2, coord.getColumn() * w
				+ w / 2);
	}

	@Override
	public void pathFinderVisited(final int x, final int y) {
	}

	@Override
	public boolean blocked(final PathFindingContext context, final int tx,
			final int ty) {
		final Monster mon = (Monster) context.getMover();
		final GroundType cellGroundType = this.getCellAt(
				new GridCoordinate(this, tx, ty)).getGroundType();
		return cellGroundType != null
				&& (!mon.getWalkableTerrain().contains(cellGroundType) || mon
						.getHatedTerrain().contains(cellGroundType));
	}

	@Override
	public float getCost(final PathFindingContext context, final int tx,
			final int ty) {
		return 1.0f;
	}

	@Override
	public void render(final GameContainer gc, final Graphics g) {
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		this.tiledMap.render((int) vp.x, (int) vp.y);
		for (final Tower t : this.builtTowers) {
			t.render(gc, g);
		}
		for (final Spawner s : this.spawners) {
			s.render(gc, g);
		}
		for (final Stronghold s : this.strongholds) {
			s.render(gc, g);
		}
		for (final Monster m : this.activeMonsters) {
			m.render(gc, g);
		}
		for (final Projectile b : this.bullets) {
			b.render(gc, g);
		}
		for (final FloatingText t : this.floatingTexts) {
			t.render(gc, g);
		}
	}

	@Override
	public Renderable getRenderableRepresentation() {
		return null;
	}

	@Override
	public IRenderer getRenderer() {
		return null;
	}

	@Override
	public void setRenderer(final IRenderer r) {
	}

	@Override
	public void mousePressed(final int button, final int x, final int y) {
		final Vector2f vp = GoodTowerDefenseGame.getInstance().getPlayState()
				.getViewport();
		final float px = x - vp.x;
		final float py = y - vp.y;
		final PlayState pgs = GoodTowerDefenseGame.getInstance().getPlayState();
		if (pgs.getRingMenu() == null && pgs.getHoverDummy() == null) {
			final RingMenu rm = new RingMenu(new Vector2f(px, py));
			try {
				rm.addMenuItem(new MenuItem(new Image(Const.PATH_GUNTOWER)) {

					@Override
					public void onClick() {
						pgs.setHoverDummy(new HoverDummy(new GunTower(
								GoodTowerDefenseGame.getInstance()
										.getPlayState().getPlayer(),
								PlayingField.this, PlayingField.this
										.pointToCoordinate(new Vector2f(px
												+ vp.x, py + vp.y)))));

					}
				});
				rm.addMenuItem(new MenuItem(new Image(Const.PATH_SLOWTOWER)) {

					@Override
					public void onClick() {
						pgs.setHoverDummy(new HoverDummy(new SlowTower(
								GoodTowerDefenseGame.getInstance()
										.getPlayState().getPlayer(),
								PlayingField.this, PlayingField.this
										.pointToCoordinate(new Vector2f(px
												+ vp.x, py + vp.y)))));

					}
				});
				rm.addMenuItem(new MenuItem(new Image(Const.PATH_SPLASHTOWER)) {

					@Override
					public void onClick() {
						pgs.setHoverDummy(new HoverDummy(new SplashTower(
								GoodTowerDefenseGame.getInstance()
										.getPlayState().getPlayer(),
								PlayingField.this, PlayingField.this
										.pointToCoordinate(new Vector2f(px
												+ vp.x, py + vp.y)))));

					}
				});
				pgs.setRingMenu(rm);
			} catch (final SlickException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFadeOut(final FloatingText text) {
		this.floatingTexts.remove(text);
	}
}
