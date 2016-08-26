package com.dynatrace.microservices.remoting.launch;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.rest.controller.LaunchConfig;

public class RemoteLaunchService {

	private static final RestTemplate REST = new RestTemplate();
	
	private final ServiceInstance serviceInstance;
	
	public RemoteLaunchService(ServiceInstance serviceInstance) {
		Objects.requireNonNull(serviceInstance);
		this.serviceInstance = serviceInstance;
	}

	public ServiceInstance launch(LaunchConfig launchConfig) throws RestClientException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LaunchConfig> entity = new HttpEntity<LaunchConfig>(launchConfig, headers);
		ResponseEntity<? extends ServiceInstance> responseInstance = REST.postForEntity(
			serviceInstance.createURL("launch", false).toString(),
			entity,
			DefaultServiceInstance.class
		);
		return responseInstance.getBody();
	}	
}
