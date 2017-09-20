package it.tecla.utils.logging;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;

public class LoggerHandler {
	
	private static final String DURATION_FORMAT = "s's' S'ms'";
	
	private Logger logger;
	private String methodSignature;
	private String returnFormat;
	private long startTime;
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public String getMethodSignature() {
		return methodSignature;
	}
	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}
	public String getReturnFormat() {
		return returnFormat;
	}
	public void setReturnFormat(String returnFormat) {
		this.returnFormat = returnFormat;
	}
	
	public void logEntering() {
		logger.trace("Entering " + methodSignature);
	}
	
	public void logExiting(Object retValue) {
		String methodDuration = DurationFormatUtils.formatDuration(System.currentTimeMillis()-startTime, DURATION_FORMAT);
		logger.trace("Duration {}: {}", methodSignature, methodDuration);
		logger.trace("Exiting {}: " + returnFormat, methodSignature, retValue);
	}
}
