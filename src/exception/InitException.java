package exception;

import org.newdawn.slick.SlickException;

public class InitException extends SlickException {
	private static final long serialVersionUID = 1L;

	public InitException(String mes) {
		super(mes);
	}
}
