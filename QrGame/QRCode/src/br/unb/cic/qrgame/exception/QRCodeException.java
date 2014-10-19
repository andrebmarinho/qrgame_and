package br.unb.cic.qrgame.exception;

public class QRCodeException extends Exception {
	private static final long serialVersionUID = 1L;

	public QRCodeException(String message) {
		super(message);
	}

	public QRCodeException(String message, Exception error) {
		super(message, error);
	}
}
