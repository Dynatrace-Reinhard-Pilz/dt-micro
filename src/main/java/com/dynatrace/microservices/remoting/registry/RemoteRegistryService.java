package com.dynatrace.microservices.remoting.registry;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceQuery;
import com.dynatrace.microservices.rest.Success;

public class RemoteRegistryService {
	
	private static final RestTemplate REST = new RestTemplate();
	
	private final ServiceInstance serviceInstance;
	
	public RemoteRegistryService(ServiceInstance serviceInstance) {
		Objects.requireNonNull(serviceInstance);
		this.serviceInstance = serviceInstance;
	}

	public ServiceInstance lookup(DefaultServiceQuery query) throws RestClientException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<DefaultServiceQuery> entity = new HttpEntity<DefaultServiceQuery>(query, headers);
		ResponseEntity<DefaultServiceInstance> responseInstance = REST.postForEntity(
			serviceInstance.createURL("lookup").toString(),
			entity,
			DefaultServiceInstance.class
		);
		return responseInstance.getBody();
	}
	
	public boolean register(DefaultServiceInstance instance) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<DefaultServiceInstance> entity = new HttpEntity<DefaultServiceInstance>(instance, headers);
		String url = serviceInstance.createURL("register").toString();
		ResponseEntity<Success> responseInstance = REST.postForEntity(
			url,
			entity,
			Success.class
		);
		return Success.TRUE.equals(responseInstance.getBody());
	}
}
