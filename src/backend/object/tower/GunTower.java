package backend.object.tower;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Const;
import backend.object.tower.bullet.IronBullet;
import backend.object.tower.bullet.Projectile;
import backend.player.Player;
import backend.playingfield.GridCoordinate;
import backend.playingfield.GroundType;
import backend.playingfield.PlayingField;

public class GunTower extends Tower {
	public GunTower(final Player owner, final PlayingField field,
			final GridCoordinate coord) {
		super(owner, field, coord, 700, 500, 1000, 500);
		this.placeableTerrain.add(GroundType.SAND);
		try {
			this.representation = new Image(Const.PATH_GUNTOWER);
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Projectile produceProjectile() {
		return new IronBullet(this, this.target);
	}
}
