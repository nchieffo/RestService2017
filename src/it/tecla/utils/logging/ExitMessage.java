package it.tecla.utils.logging;

public class ExitMessage {

	private EntryMessage entryMessage;
	private Object retValue;
	
	protected ExitMessage(EntryMessage entryMessage) {
		this(entryMessage, "<void>");
	}
	
	public ExitMessage(EntryMessage entryMessage, Object retValue) {
		this.entryMessage = entryMessage;
		this.retValue = retValue;
	}
	
	@Override
	public String toString() {
		return "Exiting " + entryMessage.getMethodSigature() + ": " + retValue;
	}
	
	public void log() {
		entryMessage.resetSteps();
		entryMessage.getStepMessage("complete method duration").log();
		entryMessage.getLogger().trace("{}", this);
	}
}
