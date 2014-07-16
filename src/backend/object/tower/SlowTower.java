package backend.object.tower;

import listener.IProjectileListener;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import util.Const;
import backend.object.tower.bullet.Projectile;
import backend.object.tower.bullet.SlowBullet;
import backend.object.tower.matcher.UnslowedMatcher;
import backend.player.Player;
import backend.playingfield.GridCoordinate;
import backend.playingfield.GroundType;
import backend.playingfield.PlayingField;

/**
 * Tower that slows passing monsters (directed)
 * 
 * @author Daniel
 * 
 */
public class SlowTower extends Tower implements IProjectileListener {
	public static final String IMG_PATH = Const.PATH_SLOWTOWER;

	/**
	 * Costructor
	 * 
	 * @param owner
	 *            owning {@link Player}
	 * @param field
	 *            {@link PlayingField} to stand on
	 * @param coord
	 *            {@link GridCoordinate} to stand on
	 */
	public SlowTower(final Player owner, final PlayingField field,
			final GridCoordinate coord) {
		super(owner, field, coord, 500, 1000, 3000, 100);
		this.placeableTerrain.add(GroundType.SAND);
		this.matcher = new UnslowedMatcher();
		try {
			this.representation = new Image(IMG_PATH);
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Projectile produceProjectile() {
		final SlowBullet sb = new SlowBullet(this, this.target);
		sb.getListeners().registerListener(this);
		return sb;
	}

	@Override
	public void onImpact(final Projectile proj, final Vector2f position) {
		this.target = null;
	}

}
