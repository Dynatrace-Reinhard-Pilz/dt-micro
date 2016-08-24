package com.dynatrace.microservices.registry;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class ServiceInstanceRegistry {
	
	private static final Log LOGGER = LogFactory.getLog(ServiceInstanceRegistry.class.getName());

	private final RegistryByServiceId registry = new RegistryByServiceId();
	
	public void register(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		registry.register(instance);
		LOGGER.info(instance + " registered");
//		registry.dump(0);
	}
	
	public ServiceInstance unregister(String instanceId) {
		Objects.requireNonNull(instanceId);
		return registry.unregister(instanceId);
	}
	
	public ServiceInstance lookup(ServiceQuery query) {
		Objects.requireNonNull(query);
		return registry.lookup(query);
	}
	
	public Collection<ServiceInstance> getInstances() {
		return registry.getInstances();
	}
	
	public ServiceInstance get(String instanceId) {
		return registry.get(instanceId);
	}
	
}
