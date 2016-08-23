package com.dynatrace.microservices.rest.common;

import java.util.UUID;

public class Status {

	private final String id = UUID.randomUUID().toString();
	private final String serviceId;
	
	public Status(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getId() {
		return id;
	}
	
	public String getServiceId() {
		return serviceId;
	}
}
