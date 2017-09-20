package it.tecla.utils.model;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class OperationResult {
	
	private String message;
	private String code;
	private boolean success;
	private long startTime = System.currentTimeMillis();
	private long endTime;
	private String duration;
	
	public String getMessage() {
		return message;
	}
	
	public String getCode() {
		return code;
	}

	public boolean isSuccess() {
		return success;
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

	public OperationResult init(boolean success, String code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
		this.endTime = System.currentTimeMillis();
		 
		duration = DurationFormatUtils.formatDurationISO(endTime - startTime);
		return this;
	}
	
	public OperationResult success(String code, String message) {
		return init(true, code, message);
	}
	
	public OperationResult failure(String code, String message) {
		return init(false, code, message);
	}

}
