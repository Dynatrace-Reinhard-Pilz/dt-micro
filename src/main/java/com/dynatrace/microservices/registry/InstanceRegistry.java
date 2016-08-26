package com.dynatrace.microservices.registry;

import java.util.Collection;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public interface InstanceRegistry<K> {

	boolean register(ServiceInstance instance);
	ServiceInstance unregister(ServiceInstance instance);
	ServiceInstance lookup(ServiceQuery query);
	Collection<ServiceInstance> getInstances();
	ServiceInstance unregister(String instanceId);
	ServiceInstance get(String instanceId);
	void dump(int indent);
}
