package it.tecla.utils.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.annotations.ApiModelProperty;

/**
 * Rappresentazione del risultato di un'operazione
 * @author Nicolo' Chieffo
 *
 */
public class OperationResult {

	@ApiModelProperty(example="true")
	private boolean success;
	@ApiModelProperty(example="OK")
	private String statusCode;
	private String details;
	private Date startedAt = new Date(System.currentTimeMillis());
	private Date endedAt;
	@ApiModelProperty(example="150 ms")
	private String duration;

	public boolean isSuccess() {
		return success;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	
	public String getDetails() {
		return details;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public String getDuration() {
		return duration;
	}

	@SuppressWarnings("hiding")
	public OperationResult init(boolean success, String statusCode, String details) {
		this.success = success;
		this.statusCode = statusCode;
		this.details = details;
		this.endedAt = new Date(System.currentTimeMillis());
		 
		duration = (endedAt.getTime() - startedAt.getTime()) + " ms";
		return this;
	}

	@SuppressWarnings("hiding")
	public OperationResult success(String statusCode, String details) {
		return init(true, statusCode, details);
	}

	@SuppressWarnings("hiding")
	public OperationResult failure(String statusCode, String details) {
		return init(false, statusCode, details);
	}
	
	public OperationResult failure(OperationException ex) {
		return init(false, ex.getStatusCode(), ex.getDetails());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
