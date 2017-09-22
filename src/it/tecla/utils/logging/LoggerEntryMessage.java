package it.tecla.utils.logging;

import org.slf4j.Logger;

public class LoggerEntryMessage {

	private long startTime = System.currentTimeMillis();
	private long lastStepTime = startTime;
	private String methodName;
	private String methodSignature;
	private Object[] args;
	private Logger logger;
	
	protected LoggerEntryMessage() {
		
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public long getLastStepTime() {
		return lastStepTime;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public String getMethodSigature() {
		if (methodSignature == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(methodName);
			sb.append("(");
			if (args != null) {
				for (Object arg : args) {
					sb.append(String.format("%s", arg));
				}
			}
			sb.append(")");
			this.methodSignature = sb.toString();
		}
		return methodSignature;
	}
	
	public void resetSteps() {
		this.lastStepTime = startTime;
	}
	
	public LoggerEntryMessage log() {
		logger.trace("{}", this);
		return this;
	}
	
	@Override
	public String toString() {
		return "ENTERING " + getMethodSigature();
	}
	
	public static LoggerEntryMessage create(Logger logger, Object... args) {
		String methodName;
		if (logger.isTraceEnabled()) {
			methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		} else {
			methodName = "<unknownMethod>";
		}
		return createWithMethodName(logger, methodName, args);
	}
	
	public static LoggerEntryMessage createWithMethodName(Logger logger, String methodName, Object... args) {
		LoggerEntryMessage loggerEntryMessage = new LoggerEntryMessage();
		loggerEntryMessage.logger = logger;
		loggerEntryMessage.methodName = methodName;
		loggerEntryMessage.args = args;
		return loggerEntryMessage;
	}
	
	public LoggerExitMessage getExitMessage() {
		return new LoggerExitMessage(this);
	}
	
	public LoggerExitMessage getExitMessage(Object retValue) {
		return new LoggerExitMessage(this, retValue);
	}
	
	public LoggerStepMessage getStepMessage(String stepName) {
		return new LoggerStepMessage(this, stepName);
	}
}
