package exception;
/**
 * Thrown when something went wrong with the MapParser
 * @author Daniel
 */
public class MapParserException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * @param mes message
	 */
	public MapParserException(String mes) {
		super(mes);
	}

}
