package com.dynatrace.microservices.registry;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class VersionedServiceInstanceRegistry {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(VersionedServiceInstanceRegistry.class.getName());

	private final RegistryByServiceId registry = new RegistryByServiceId();
	
	public boolean register(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		return registry.register(instance);
	}
	
	public ServiceInstance unregister(String instanceId) {
		Objects.requireNonNull(instanceId);
		return registry.unregister(instanceId);
	}
	
	public Collection<ServiceInstance> lookup(ServiceQuery query) {
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
