package com.dynatrace.microservices;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {

	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(ContextClosedListener.class.getName());
	
	public void onApplicationEvent(ContextClosedEvent event) {
		try {
			ServiceApplication.registrationTask.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServiceApplication.getRegistryService().unregister(ServiceApplication.getInstance().getId());
		ServiceApplication.launchedServices.shutdown();
	}
}
