package exception;

public class PlayingFieldException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PlayingFieldException(String mes) {
		super(mes);
	}
}
