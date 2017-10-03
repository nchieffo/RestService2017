package it.tecla.utils.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class OperationResult {

	private boolean success;
	private String statusCode;
	private String message;
	private long startTime = System.currentTimeMillis();
	private long endTime;
	private String duration;

	public boolean isSuccess() {
		return success;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	
	public String getMessage() {
		return message;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public String getDuration() {
		return duration;
	}

	public OperationResult init(boolean success, String statusCode, String message) {
		this.success = success;
		this.statusCode = statusCode;
		this.message = message;
		this.endTime = System.currentTimeMillis();
		 
		duration = DurationFormatUtils.formatDurationISO(endTime - startTime);
		return this;
	}
	
	public OperationResult success(String statusCode, String message) {
		return init(true, statusCode, message);
	}
	
	public OperationResult failure(String statusCode, String message) {
		return init(false, statusCode, message);
	}
	
	public OperationResult failure(OperationException ex) {
		return init(false, ex.getStatusCode(), ex.getMessage());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
