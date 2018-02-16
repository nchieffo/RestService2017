package it.tecla.utils.logging.logback;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class JULAppender<E extends ILoggingEvent> extends AppenderBase<E> {
	
    protected PatternLayoutEncoder encoder;
	
	public PatternLayoutEncoder getEncoder() {
		return encoder;
	}
	
	public void setEncoder(PatternLayoutEncoder encoder) {
		this.encoder = encoder;
	}

	@Override
	protected void append(E event) {
		
		java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(event.getLoggerName());
		java.util.logging.Level julLevel = JULUtils.toJULLevel(event.getLevel());
		
		if (julLogger.isLoggable(julLevel)) {
			julLogger.log(julLevel, encoder.getLayout().doLayout(event));
		}
	}
}
