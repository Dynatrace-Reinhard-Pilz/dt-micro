package com.dynatrace.microservices.remoting.registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.ServiceQuery;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.rest.registry.ServiceInstanceCollection;

public class LocalRemoteRegistryService implements RegistryService, ExceptionHandler {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(LocalRemoteRegistryService.class.getName());
	
	private final RemoteRegistryService service;
	
	public LocalRemoteRegistryService(RemoteRegistryService service) {
		this.service = service;
	}

	@Override
	public ServiceInstanceCollection lookup(ServiceQuery query) {
		try {
			return service.lookup(query);
		} catch (RestClientException e) {
			handle(e);
		}
		return null;
	}
	
	@Override
	public ServiceInstance register(ServiceInstance instance, boolean correct) {
		try {
			return service.register(instance, correct);
		} catch (RestClientException e) {
			handle(e);
		}
		return null;
	}

	@Override
	public boolean unregister(String instanceId) {
		try {
			return service.unregister(instanceId);
		} catch (RestClientException e) {
			handle(e);
		}
		return false;
	}

	@Override
	public ServiceInstance getInstance(String instanceId) {
		try {
			return service.getInstance(instanceId);
		} catch (RestClientException e) {
			handle(e);
		}
		return null;
	}

	@Override
	public ServiceInstanceCollection getInstances() {
		try {
			return service.getInstances();
		} catch (RestClientException e) {
			handle(e);
		}
		return null;
	}

	@Override
	public void handle(RestClientException e) {
		e.printStackTrace(System.err);
	}

}
