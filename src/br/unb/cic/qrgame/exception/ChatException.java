package br.unb.cic.qrgame.exception;

public class ChatException extends Exception {
	private static final long serialVersionUID = 1L;

	public ChatException(String message) {
		super(message);
	}

	public ChatException(String message, Exception error) {
		super(message, error);
	}
}
