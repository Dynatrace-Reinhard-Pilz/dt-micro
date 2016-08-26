package com.dynatrace.microservices.remoting.launch;

import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.rest.controller.LaunchConfig;

public class LocalRemoteLaunchService implements LaunchService {
	
	private final RemoteLaunchService service;
	private final ExceptionHandler exceptionHandler;
	
	public LocalRemoteLaunchService(RemoteLaunchService service, ExceptionHandler exceptionHandler) {
		this.service = service;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public ServiceInstance launch(LaunchConfig launchConfig) {
		try {
			return service.launch(launchConfig);
		} catch (RestClientException e) {
			exceptionHandler.handle(e);
			return null;
		}
	}
}
