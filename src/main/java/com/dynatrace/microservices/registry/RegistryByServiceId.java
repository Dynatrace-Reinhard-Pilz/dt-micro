package com.dynatrace.microservices.registry;

import java.util.Objects;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class RegistryByServiceId extends AbstractInstanceRegistry<String, RegistryByVersion> {

	@Override
	public Class<RegistryByVersion> valueType() {
		return RegistryByVersion.class;
	}

	@Override
	public String evaluateKey(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		return instance.getService().getServiceId();
	}

	@Override
	protected String evaluateKey(ServiceQuery query) {
		Objects.requireNonNull(query);
		return query.getServiceId();
	}

}
