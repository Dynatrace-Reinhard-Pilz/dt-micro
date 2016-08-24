package com.dynatrace.microservices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.dynatrace.microservices.processes.ServiceLauncher;

@Component
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {

	private static final Log LOGGER = LogFactory.getLog(ContextClosedListener.class.getName());
	
	public void onApplicationEvent(ContextClosedEvent event) {
		LOGGER.trace(this.getClass().getSimpleName() + " has been invoked");
		ServiceLauncher.shutdownAll();
	}
}
