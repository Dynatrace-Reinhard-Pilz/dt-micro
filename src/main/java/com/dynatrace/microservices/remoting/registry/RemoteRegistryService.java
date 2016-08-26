package com.dynatrace.microservices.remoting.registry;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.registry.ServiceQuery;
import com.dynatrace.microservices.rest.registry.ServiceInstanceCollection;

public class RemoteRegistryService {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(RemoteRegistryService.class.getName());
	
	private static final RestTemplate REST = new RestTemplate();
	
	private final ServiceInstance serviceInstance;
	
	public RemoteRegistryService(ServiceInstance serviceInstance) {
		Objects.requireNonNull(serviceInstance);
		this.serviceInstance = serviceInstance;
	}
	
	public ServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	public ServiceInstance lookup(ServiceQuery query) throws RestClientException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ServiceQuery> entity = new HttpEntity<ServiceQuery>(query, headers);
		ResponseEntity<DefaultServiceInstance> responseInstance = REST.postForEntity(
			serviceInstance.createURL("lookup", false).toString(),
			entity,
			DefaultServiceInstance.class
		);
		return responseInstance.getBody();
	}
	
	public ServiceInstance register(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		Location location = instance.getLocation();
		Objects.requireNonNull(location);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ServiceInstance> entity = new HttpEntity<ServiceInstance>(instance, headers);
		String url = serviceInstance.createURL("register", false).toString();
		ResponseEntity<? extends ServiceInstance> responseInstance = REST.postForEntity(
			url,
			entity,
			DefaultServiceInstance.class
		);
		return responseInstance.getBody();
	}
	
	public boolean unregister(String instanceId) {
		String url = serviceInstance.createURL("unregister/{instanceId}", false).toString();
		ResponseEntity<Boolean> exchange = REST.exchange(url, HttpMethod.DELETE, null, Boolean.TYPE, instanceId);
		return exchange.getBody();
	}
	
	public ServiceInstance getInstance(String instanceId) {
		String url = serviceInstance.createURL("instances/{instanceId}", false).toString();
		return REST.getForObject(url, ServiceInstance.class, instanceId);
	}
	
	public ServiceInstanceCollection getInstances() {
		String url = serviceInstance.createURL("instances", false).toString();
		return REST.getForObject(url, ServiceInstanceCollection.class);
	}

	public ServiceInstance register(ServiceInstance instance, boolean correct) {
		return register(instance);
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<ServiceInstance> entity = new HttpEntity<ServiceInstance>(instance, headers);
//		String url = serviceInstance.createURL("register", false).toString();
//		ResponseEntity<? extends ServiceInstance> responseInstance = REST.postForEntity(
//			url,
//			entity,
//			DefaultServiceInstance.class
//		);
//		REST.postForObject(url, request, responseType, uriVariables)
//		return responseInstance.getBody();
	}
}
