package br.unb.cic.qrgame.exception;

public class LoginException extends Exception {
	private static final long serialVersionUID = 1L;

	public LoginException(String message) {
		super(message);
	}

	public LoginException(String message, Exception error) {
		super(message, error);
	}
}
