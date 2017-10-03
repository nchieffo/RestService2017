package it.tecla.utils.model;

public class OperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String statusCode;
	private String message;
	
	public OperationException(String message, String statusCode) {
		super(statusCode + " " + message);
		this.message = message;
		this.statusCode = statusCode;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
}
