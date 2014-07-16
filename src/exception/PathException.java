package exception;

/**
 * Thrown when a Path can't be calculated
 * @author Daniel
 */
public class PathException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public PathException(String mes)
	{
		super(mes);
	}
}
