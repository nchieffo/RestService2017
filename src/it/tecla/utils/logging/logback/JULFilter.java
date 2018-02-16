package it.tecla.utils.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Filtro di logback per rimbalzare la decisione sul logger JDK
 * @author Nicolo' Chieffo
 *
 */
@Deprecated
public class JULFilter extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		
		java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(event.getLoggerName());
		java.util.logging.Level julLevel = JULUtils.toJULLevel(event.getLevel());
		
		boolean isLoggable = julLogger.isLoggable(julLevel);
		
		if (isLoggable) {
			return FilterReply.ACCEPT;
		}
		
		return FilterReply.NEUTRAL;
		
	}

}
