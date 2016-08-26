package com.dynatrace.microservices;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class RegistrationTask extends TimerTask implements Closeable {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(RegistrationTask.class.getName());

	private final Timer timer = new Timer(RegistrationTask.class.getSimpleName(), true);
	
	public RegistrationTask() {
		timer.schedule(this, 0, 5000);
	}

	@Override
	public void run() {
		ServiceApplication.setInstance(
			ServiceApplication.getRegistryService().register(ServiceApplication.getInstance(), true)
		);
	}

	@Override
	public void close() throws IOException {
		timer.cancel();
	}
	
}
