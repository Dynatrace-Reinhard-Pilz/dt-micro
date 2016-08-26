package com.dynatrace.microservices.remoting.registry;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.ServiceQuery;
import com.dynatrace.microservices.rest.registry.ServiceInstanceCollection;

public interface RegistryService {

	@PostMapping(path = "/lookup", consumes="application/json", produces="application/json")
	ServiceInstance lookup(ServiceQuery query);
	
	@PostMapping(path = "/register", consumes="application/json", produces="application/json")
	ServiceInstance register(ServiceInstance instance, boolean correct);
	
	@DeleteMapping(path = "/instances/{instanceId}")
	boolean unregister(String instanceId);
	
	@GetMapping(path = "/instances/{instanceId}")
	ServiceInstance getInstance(@PathVariable String instanceId);
	
	@GetMapping(path = "/instances", produces="application/json")
	ServiceInstanceCollection getInstances();
	
}
