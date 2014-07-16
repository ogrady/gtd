package backend.playingfield;

import java.util.HashMap;

import backend.object.buildings.Spawner;
import backend.object.buildings.Stronghold;
import backend.object.monster.Monster;
import backend.object.monster.util.Path;

/**
 * A 4D-Grid with [Monsterclass][Spawner][Stronghold][Path] to access certain
 * heavily used {@link Path}s more easily
 * 
 * @author Daniel
 * 
 */
public class PathMatrix {
	private final HashMap<String, HashMap<Spawner, HashMap<Stronghold, Path>>> matrix;
	private final PlayingField field;

	public PathMatrix(final PlayingField field) {
		this.field = field;
		this.matrix = new HashMap<String, HashMap<Spawner, HashMap<Stronghold, Path>>>();
	}

	public Path getPath(final Monster mon, final Spawner sp, final Stronghold st) {
		final String clname = mon.getClass().getSimpleName();
		HashMap<Spawner, HashMap<Stronghold, Path>> monsterdim = this.matrix
				.get(clname);
		if (monsterdim == null) {
			monsterdim = new HashMap<Spawner, HashMap<Stronghold, Path>>();
			this.matrix.put(clname, monsterdim);
		}
		HashMap<Stronghold, Path> spawnerdim = monsterdim.get(sp);
		if (spawnerdim == null) {
			spawnerdim = new HashMap<Stronghold, Path>();
			monsterdim.put(sp, spawnerdim);
		}
		Path p = spawnerdim.get(st);
		if (p == null) {
			final Pathfinder pf = this.field.getPathFinder();
			p = pf.find(mon, sp.getStandOn().getCoordinate(), st.getStandOn()
					.getCoordinate());
			spawnerdim.put(st, p);
		}
		return p;
	}
}
