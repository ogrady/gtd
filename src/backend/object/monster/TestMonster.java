package backend.object.monster;

import java.util.Random;

import org.newdawn.slick.Image;

import backend.object.buildings.Spawner;
import backend.playingfield.GroundType;

public class TestMonster extends Monster {
	public TestMonster(final Spawner pos) {
		super(pos, 10000, 2, 1, 1f, 100);
		final int i = new Random().nextInt(5);
		this.walkableTerrain.add(GroundType.SAND_WAY);
		// this.walkableTerrain.add(GroundType.GENERIC_OBSTACLE);
		final String data = "img/monster/test" + i + ".png";
		try {
			this.representation = new Image(data);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
