package exception;

public class CommonException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static final String INSUFFICIENT_RIGHTS = "insufficient rights";
	public static final String NEGATIVE_ARGUMENTS = "no negative arguments";
	public static final String MISSING_ARGUMENTS = "missing arguments";
	public static final String ILLEGAL_ARGUMENTS = "wrong value";

	/**
	 * Constructor
	 * 
	 * @param mes
	 *            message
	 */
	public CommonException(final String msg) {
		super(msg);
	}
}
