package exception;
/**
 * Thrown when something went wrong within a Monster
 * @author Daniel
 */
public class MonsterException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * @param mes message
	 */
	public MonsterException(String mes) {
		super(mes);
	}
}
