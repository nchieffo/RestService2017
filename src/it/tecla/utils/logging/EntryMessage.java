package it.tecla.utils.logging;

import org.slf4j.Logger;

public class EntryMessage {

	private long startTime = System.currentTimeMillis();
	private long lastStepTime = startTime;
	private String methodName;
	private String methodSignature;
	private Object[] args;
	private Logger logger;
	
	protected EntryMessage() {
		
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
	
	public EntryMessage log() {
		logger.trace("{}", this);
		return this;
	}
	
	@Override
	public String toString() {
		return "Entering " + getMethodSigature();
	}
	
	public static EntryMessage create(Logger logger, String methodName, Object... args) {
		EntryMessage entryMessage = new EntryMessage();
		entryMessage.logger = logger;
		entryMessage.methodName = methodName;
		entryMessage.args = args;
		return entryMessage;
	}
	
	public ExitMessage getExitMessage() {
		return new ExitMessage(this);
	}
	
	public ExitMessage getExitMessage(Object retValue) {
		return new ExitMessage(this, retValue);
	}
	
	public StepMessage getStepMessage(String stepName) {
		return new StepMessage(this, stepName);
	}
}
