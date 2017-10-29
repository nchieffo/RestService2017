package it.tecla.utils.model;

/**
 * Eccezione contenente un codice di errore e un messaggio, utile per essere poi riconosciuta esternamente
 * @author Nicolo' Chieffo
 *
 */
public class OperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String statusCode;
	private String details;
	
	public OperationException(String details, String statusCode) {
		super(statusCode + " " + details);
		this.details = details;
		this.statusCode = statusCode;
	}
	
	public String getDetails() {
		return details;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
}
