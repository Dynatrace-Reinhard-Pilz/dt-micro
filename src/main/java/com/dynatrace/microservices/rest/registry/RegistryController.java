package com.dynatrace.microservices.rest.registry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.ServiceProperties;
import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceQuery;
import com.dynatrace.microservices.rest.Success;
import com.dynatrace.microservices.utils.net.SimpleHostNameResolver;

@RestController("registry")
@ConditionalOnExpression("'${micro.service}'.equals('registry')")
public class RegistryController {
	
	@SuppressWarnings("unused")
	private @Autowired ServiceProperties props;
	private @Autowired HttpServletRequest request;
	
	@PostMapping(path = "/${micro.service}/lookup", consumes="application/json", produces="application/json")
	public ServiceInstance lookup(@RequestBody DefaultServiceQuery query) {
		return ServiceApplication.getServiceRegistry().lookup(query);
	}
	
	@PostMapping(path = "/${micro.service}/register", consumes="application/json", produces="application/json")
	public Success register(@RequestBody DefaultServiceInstance instance) {
		Location location = instance.getLocation();
		instance.setLocation(
			new DefaultLocation(
				SimpleHostNameResolver.INSTANCE.getHostName(request),
				location.getPort()
			)
		);
		ServiceApplication.getServiceRegistry().register(instance);
		return Success.TRUE;
	}
	
	@DeleteMapping(path = "/${micro.service}/instances/{instanceId}")
	public Success unregister(@PathVariable String instanceId) {
		ServiceApplication.getServiceRegistry().unregister(instanceId);
		return Success.TRUE;
	}
	
	@GetMapping(path = "/${micro.service}/instances/{instanceId}")
	public ServiceInstance getInstance(@PathVariable String instanceId) {
		return ServiceApplication.getServiceRegistry().get(instanceId);
	}
	
	@GetMapping(path = "/${micro.service}/instances", produces="application/json")
	public ServiceInstanceCollection getInstances() {
		ServiceInstanceCollection instances = new ServiceInstanceCollection();
		instances.addAll(ServiceApplication.getServiceRegistry().getInstances());
		return instances;
	}
	

}