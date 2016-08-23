package com.dynatrace.microservices.remoting;

import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceQuery;

public class LocalRemoteRegistryService implements RegistryService {
	
	private final RemoteRegistryService service;
	private final ExceptionHandler exceptionHandler;
	
	public LocalRemoteRegistryService(RemoteRegistryService service, ExceptionHandler exceptionHandler) {
		this.service = service;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public ServiceInstance lookup(DefaultServiceQuery query) {
		try {
			return service.lookup(query);
		} catch (RestClientException e) {
			exceptionHandler.handle(e);
		}
		return null;
	}

	@Override
	public boolean register(DefaultServiceInstance instance) {
		try {
			return service.register(instance);
		} catch (RestClientException e) {
			exceptionHandler.handle(e);
		}
		return false;
	}

}
