package com.dynatrace.microservices.registry;

import java.util.Objects;

import com.dynatrace.microservices.infrastructure.Service;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.infrastructure.Version;

public class RegistryByVersion extends AbstractInstanceRegistry<Version, ServiceInstances> {

	@Override
	public Class<ServiceInstances> valueType() {
		return ServiceInstances.class;
	}

	@Override
	public Version evaluateKey(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		Service service = instance.getService();
		Objects.requireNonNull(service);
		return service.getVersion();
	}

	@Override
	protected Version evaluateKey(ServiceQuery query) {
		Objects.requireNonNull(query);
		return query.getVersion();
	}

}
