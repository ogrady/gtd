package backend.playingfield;

public enum CellAttribute {
	UNBUILDABLE, UNWALKABLE;
	
	public static CellAttribute getByName(String name) {
		name = name.toLowerCase();
		int i = 0;
		CellAttribute[] types = CellAttribute.values();
		while(i < types.length && !types[i].name().toLowerCase().equals(name)) {
			i++;
		}
		return i < types.length ? types[i] : null;
	}
}
