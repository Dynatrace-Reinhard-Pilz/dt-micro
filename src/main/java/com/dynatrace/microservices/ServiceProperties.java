package com.dynatrace.microservices;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("micro")
public class ServiceProperties {

	private String service = "registry";

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}