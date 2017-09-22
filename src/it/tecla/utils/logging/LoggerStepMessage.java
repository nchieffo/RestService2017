package it.tecla.utils.logging;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class LoggerStepMessage {

	private LoggerEntryMessage loggerEntryMessage;
	private String stepName;
	
	protected LoggerStepMessage(LoggerEntryMessage loggerEntryMessage, String stepName) {
		this.loggerEntryMessage = loggerEntryMessage;
		this.stepName = stepName;
	}
	
	@Override
	public String toString() {
		long durationTime = System.currentTimeMillis() - loggerEntryMessage.getLastStepTime();
		String duration = DurationFormatUtils.formatDuration(durationTime, "s's' S'ms'");
		return "Step " + loggerEntryMessage.getMethodSigature() + ": " + stepName + " " + duration;
	}
	
	public void log() {
		loggerEntryMessage.getLogger().trace("{}", this);
	}
}
