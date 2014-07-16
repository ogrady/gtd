package backend.playingfield;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path.Step;

import backend.object.monster.Monster;
import backend.object.monster.util.Path;


public class Pathfinder {
	private AStarPathFinder finder;
	private PlayingField field;
	
	public Pathfinder(PlayingField field) {
		this.field = field;
		this.finder = new AStarPathFinder(field, field.getColumns() * field.getRows(), false);
	}
	
	/**
	 * Finds a {@link Path} between two {@link GridCoordinate}s
	 * @param from start position
	 * @param to end position
	 * @return {@link Path} between those two. Empty if not {@link Path} exists
	 */
	public Path find(Monster mon, GridCoordinate from, GridCoordinate to) {
		org.newdawn.slick.util.pathfinding.Path path = finder.findPath(mon, from.getColumn(), from.getRow(), to.getColumn(), to.getRow());
		Path result = new Path();
		Step step;
		if(path != null ) {
			for(int i = 0; i < path.getLength(); i++) {
				step = path.getStep(i);
				result.add(this.field.getCellAt(step.getX(), step.getY()));
			}
		}
		return result;
	}
}
