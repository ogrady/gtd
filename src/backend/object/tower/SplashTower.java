package backend.object.tower;

import util.Const;
import backend.object.monster.Monster;
import backend.object.tower.bullet.Explosion;
import backend.object.tower.bullet.Projectile;
import backend.player.Player;
import backend.playingfield.GridCoordinate;
import backend.playingfield.GroundType;
import backend.playingfield.PlayingField;
import frontend.ImageLoader;
import game.GoodTowerDefenseGame;

/**
 * Tower that produces a circular "bullet" around themselves to damage all
 * {@link Monster}s within that radius
 * 
 * @author Daniel
 * 
 */
public class SplashTower extends Tower {

	/**
	 * Constructor
	 * 
	 * @param owner
	 *            owning {@link Player}
	 * @param field
	 *            {@link PlayingField} to stand on
	 * @param coord
	 *            {@link GridCoordinate} to stand on
	 */
	public SplashTower(final Player owner, final PlayingField field,
			final GridCoordinate coord) {
		super(owner, field, coord, 200, 2000, 1000, 200);
		this.placeableTerrain.add(GroundType.SAND);
		this.representation = ImageLoader.loadImage(Const.PATH_SPLASHTOWER);
	}

	@Override
	public Projectile produceProjectile() {
		return new Explosion(this, this.range, 20, 20, GoodTowerDefenseGame
				.getInstance().getPlayState().getPlayingField()
				.getActiveMonsters());
	}

}
