package it.tecla.utils.logging;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class StepMessage {

	private EntryMessage entryMessage;
	private String stepName;
	
	protected StepMessage(EntryMessage entryMessage, String stepName) {
		this.entryMessage = entryMessage;
		this.stepName = stepName;
	}
	
	@Override
	public String toString() {
		long durationTime = System.currentTimeMillis() - entryMessage.getLastStepTime();
		String duration = DurationFormatUtils.formatDuration(durationTime, "s's' S'ms'");
		return "Step " + entryMessage.getMethodSigature() + ": " + stepName + " " + duration;
	}
	
	public void log() {
		entryMessage.getLogger().trace("{}", this);
	}
}
