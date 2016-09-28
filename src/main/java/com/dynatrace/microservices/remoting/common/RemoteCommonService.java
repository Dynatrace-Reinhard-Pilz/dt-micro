package com.dynatrace.microservices.remoting.common;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class RemoteCommonService {
	
	private static final RestTemplate REST = new RestTemplate();
	
	private final ServiceInstance serviceInstance;
	
	public RemoteCommonService(ServiceInstance serviceInstance) {
		Objects.requireNonNull(serviceInstance);
		this.serviceInstance = serviceInstance;
	}

	public boolean shutdown() throws RestClientException {
		ResponseEntity<String> response = REST.getForEntity(serviceInstance.createURL("shutdown", false).toString(), String.class);
		return "true".equals(response.getBody());
	}
	
	public String getQuote() throws RestClientException {
		ResponseEntity<String> response = REST.getForEntity(serviceInstance.createURL("nplusone", false).toString(), String.class);
		return response.getBody();
	}
}
