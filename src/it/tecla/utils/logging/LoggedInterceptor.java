package it.tecla.utils.logging;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
@Logged
public class LoggedInterceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggedInterceptor.class);

	@AroundInvoke
	public Object log(InvocationContext ctx) throws Exception {
		
		LoggedInterceptorHelper loggedInterceptorHelper = null;
		
		try {
			loggedInterceptorHelper = LoggedInterceptorHelperFactory.create(ctx.getTarget().getClass(), ctx.getMethod(), ctx.getParameters());
			loggedInterceptorHelper.logEntering();
		} catch (Throwable t) {
			LOGGER.warn("Error while creating/logging @Logged", t);
		}
		
		// real method invocation
		Object result = ctx.proceed();
		
		if (loggedInterceptorHelper != null) {
			try {
				loggedInterceptorHelper.logExiting(result);
			} catch (Throwable t) {
				LOGGER.warn("Error while logging @Logged", t);
			}
		}
		
		return result;
	}
	
}
