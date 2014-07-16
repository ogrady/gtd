package backend.object.monster.util;

import java.util.ArrayList;

import util.ICopyable;

import backend.playingfield.GridCoordinate;
import backend.playingfield.PlayingFieldMetaCell;

public class Path extends ArrayList<PlayingFieldMetaCell> implements ICopyable<Path> {
	private static final long serialVersionUID = 1L;
	private int iterator;
	private boolean done;
	
	public boolean isDone() {
		return this.done || this.isEmpty();
	}
	
	public Path() {
		this.iterator = 0;
	}

	@Override
	public Path copy() {
		Path copy = new Path();
		for(PlayingFieldMetaCell pfc : this) {
			copy.add(pfc);
		}
		return copy;
	}
	
	/**
	 * @return next target if any or null
	 */
	public PlayingFieldMetaCell getNextTarget() {
		return this.get(iterator);
		//return this.iterator < this.size() ? this.get(iterator) : null;
	}
	
	public void advance() {
		this.iterator++;
		if(this.iterator >= this.size()) {
			this.iterator--;
			this.done = true;
		}
	}
	
	public void retreat() {
		if(iterator > 0) {
			this.iterator--;
		}
	}
	
	@Override
	public String toString() {
		String template = "%1$d:[%2$d|%3$d], ";
		String template2 = "*%1$d:[%2$d|%3$d]*, ";
		String result = String.format("%1$s@%2$s (it: %3$d) => ", this.getClass().getSimpleName(), System.identityHashCode(this), this.iterator);
		GridCoordinate coord;
		for(int i = 0; i < this.size(); i++) {
			coord = this.get(i).getCoordinate();
			result += String.format(i == this.iterator ? template2 : template, i, coord.getRow(), coord.getColumn());
		}
		return result;
	}
}
