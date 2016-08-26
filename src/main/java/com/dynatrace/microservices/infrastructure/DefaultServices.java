package com.dynatrace.microservices.infrastructure;

public enum DefaultServices {
	
	controller, registry, gateway;

	public boolean matches(String serviceId) {
		if (serviceId == null) {
			return false;
		}
		return name().equals(serviceId);
	}
	
	public boolean matches(Service service) {
		if (service == null) {
			return false;
		}
		return matches(service.getServiceId());
	}
	
	public boolean matches(ServiceInstance instance) {
		if (instance == null) {
			return false;
		}
		return matches(instance.getService());
	}
}
