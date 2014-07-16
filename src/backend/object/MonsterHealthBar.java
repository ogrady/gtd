package backend.object;


import org.newdawn.slick.geom.Vector2f;



import backend.object.monster.Monster;

public class MonsterHealthBar extends Bar {
	private Monster mon;
	
	@Override
	public Vector2f getPosition() {
		Vector2f pos = this.mon.getCenterPosition().copy();
		pos.x -= this.getRepresentationWidth()/2;
		return pos;
	}
	
	@Override
	public Float getValue() {
		return this.mon.getHitpoints();
	}
	
	public Float getMaximum() {
		return this.mon.getMaxHitpoints();
	}

	public MonsterHealthBar(Monster mon) {
		super(mon.getPosition(), mon.getHitpoints(), mon.getMaxHitpoints());
		this.mon = mon;
	}
}
