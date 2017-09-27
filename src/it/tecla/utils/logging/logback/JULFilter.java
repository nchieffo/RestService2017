package it.tecla.utils.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class JULFilter extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		
		java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(event.getLoggerName());
		java.util.logging.Level julLevel = toJULLevel(event.getLevel());
		
		boolean isLoggable = julLogger.isLoggable(julLevel);
//		System.out.println(julLogger + " " + julLevel + " " + isLoggable);
		
		if (isLoggable) {
			return FilterReply.ACCEPT;
		}
		
		return FilterReply.NEUTRAL;
		
	}
	
	public static java.util.logging.Level toJULLevel(ch.qos.logback.classic.Level logbackLevel) {
		
		java.util.logging.Level julLevel;
		
		if (logbackLevel.toInt() < ch.qos.logback.classic.Level.TRACE_INT) {
			julLevel = java.util.logging.Level.ALL;
		} else if (logbackLevel.toInt() < ch.qos.logback.classic.Level.DEBUG_INT) {
			julLevel = java.util.logging.Level.FINEST;
		} else if (logbackLevel.toInt() < ch.qos.logback.classic.Level.INFO_INT) {
			julLevel = java.util.logging.Level.FINE;
		} else if (logbackLevel.toInt() < ch.qos.logback.classic.Level.WARN_INT) {
			julLevel = java.util.logging.Level.INFO;
		} else if (logbackLevel.toInt() < ch.qos.logback.classic.Level.ERROR_INT) {
			julLevel = java.util.logging.Level.WARNING;
		} else if (logbackLevel.toInt() < ch.qos.logback.classic.Level.OFF_INT) {
			julLevel = java.util.logging.Level.SEVERE;
		} else {
			julLevel = java.util.logging.Level.OFF;
		}
		
		return julLevel;
	}

}
