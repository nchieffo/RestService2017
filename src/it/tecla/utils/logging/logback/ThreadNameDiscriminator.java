package it.tecla.utils.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

public class ThreadNameDiscriminator extends AbstractDiscriminator<ILoggingEvent> {

	private String key = "threadName";
	
	@Override
	public String getDiscriminatingValue(ILoggingEvent e) {
		return e.getThreadName();
	}

	@Override
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

}
