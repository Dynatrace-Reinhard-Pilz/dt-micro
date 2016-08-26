package com.dynatrace.microservices.remoting.registry;

import java.util.Objects;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.ServiceQuery;
import com.dynatrace.microservices.rest.registry.ServiceInstanceCollection;

public class SynchronizedRegistryService implements RegistryService {
	
	private final Object lock = new Object();
	private RegistryService service = null;
	
	public SynchronizedRegistryService() {
	}

	public SynchronizedRegistryService(RegistryService service) {
		Objects.requireNonNull(service);
	}
	
	public void set(RegistryService service) {
		Objects.requireNonNull(service);
		synchronized (lock) {
			this.service = service;
		}
	}
	
	@Override
	public ServiceInstance lookup(ServiceQuery query) {
		Objects.requireNonNull(query);
		synchronized (lock) {
			if (service == null) {
				return ServiceInstance.NOT_FOUND;
			}
			return service.lookup(query);
		}
	}

	@Override
	public boolean unregister(String instanceId) {
		Objects.requireNonNull(instanceId);
		synchronized (lock) {
			if (service == null) {
				return false;
			}
			return service.unregister(instanceId);
		}
	}

	@Override
	public ServiceInstance register(ServiceInstance instance, boolean correct) {
		Objects.requireNonNull(instance);
		synchronized (lock) {
			if (service == null) {
				return instance;
			}
			return service.register(instance, correct);
		}
	}

	@Override
	public ServiceInstance getInstance(String instanceId) {
		Objects.requireNonNull(instanceId);
		synchronized (lock) {
			if (service == null) {
				return null;
			}
			return service.getInstance(instanceId);
		}
	}

	@Override
	public ServiceInstanceCollection getInstances() {
		synchronized (lock) {
			if (service == null) {
				return null;
			}
			return service.getInstances();
		}
	}

}
