package com.dynatrace.microservices.registry;

import java.util.Collection;
import java.util.Objects;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class ServiceInstanceRegistry {

	private final RegistryByServiceId registry = new RegistryByServiceId();
	
	public void register(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		registry.register(instance);
		System.out.println(instance + " registered");
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
