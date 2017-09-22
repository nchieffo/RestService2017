package it.tecla.utils.logging;

public class LoggerExitMessage {

	private LoggerEntryMessage loggerEntryMessage;
	private Object retValue;
	
	protected LoggerExitMessage(LoggerEntryMessage loggerEntryMessage) {
		this(loggerEntryMessage, "<void>");
	}
	
	public LoggerExitMessage(LoggerEntryMessage loggerEntryMessage, Object retValue) {
		this.loggerEntryMessage = loggerEntryMessage;
		this.retValue = retValue;
	}
	
	@Override
	public String toString() {
		return "EXITING " + loggerEntryMessage.getMethodSigature() + ": " + retValue;
	}
	
	public void log() {
		loggerEntryMessage.resetSteps();
		loggerEntryMessage.getStepMessage("FULL METHOD").log();
		loggerEntryMessage.getLogger().trace("{}", this);
	}
}
