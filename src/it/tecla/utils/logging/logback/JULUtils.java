package it.tecla.utils.logging.logback;

/**
 * Appender di logback per scrivere sul logger JDK
 * @author Nicolo' Chieffo
 *
 */
public class JULUtils {
	
	private JULUtils() {
		throw new UnsupportedOperationException("Utility class");
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
