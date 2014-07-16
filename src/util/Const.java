package util;

public class Const {
	private static final String PATH_IMG = "img/";
	public static final String PATH_MONSTER = PATH_IMG + "monster/";
	public static final String PATH_TOWER = PATH_IMG + "tower/";
	public static final String PATH_INTERFACE = PATH_IMG + "interface/";

	// game states
	public static final int STATE_PLAY = 1;
	public static final int STATE_START_SCREEN = 0;

	// structures
	public static final String PATH_STRONGHOLD = PATH_IMG + "stronghold.png";
	public static final String PATH_SPAWNER = PATH_IMG + "spawner.png";

	// towers
	public static final String PATH_GUNTOWER = PATH_TOWER
			+ "directed_tower.png";
	public static final String PATH_SLOWTOWER = PATH_TOWER + "slow_tower.png";
	public static final String PATH_SPLASHTOWER = PATH_TOWER
			+ "splash_tower.png";
	public static final String PATH_SITE = PATH_TOWER + "site.png";

	// bullets
	public static final String PATH_BULLET = PATH_IMG + "bullet.gif";
	public static final String PATH_FROSTBULLET = PATH_IMG + "frostbullet.png";

	// monsters
	public static final String PATH_BLOB = PATH_MONSTER + "blob/";

	// interface
	public static final String PATH_SELL_ICON = PATH_INTERFACE
			+ "sell_icon.png";
	public static final String PATH_UPGRADE_ICON = PATH_INTERFACE
			+ "upgrade_icon.png";
	public static final String PATH_MONEY_ICON = PATH_INTERFACE
			+ "money_icon.png";
	public static final String PATH_HEALTH_ICON = PATH_INTERFACE
			+ "health_icon.png";

	// local
	public static final String L_NO_MONEY = "noMoney";
	public static final String L_NOT_IMPLEMENTED = "notImplemented";
}
