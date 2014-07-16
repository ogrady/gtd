package backend.playingfield;

public enum GroundType {
	GRASS, MUD, SAND, STONE, WATER, FIRE, AIR, SAND_WAY, GENERIC_OBSTACLE;
	
	public static GroundType getByName(String name) {
		name = name.toLowerCase();
		int i = 0;
		GroundType[] types = GroundType.values();
		while(i < types.length && !types[i].name().toLowerCase().equals(name)) {
			i++;
		}
		return i < types.length ? types[i] : null;
	}
}
