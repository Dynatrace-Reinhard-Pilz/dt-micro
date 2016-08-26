package com.dynatrace.microservices.rest.registry;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.ServiceProperties;
import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.registry.ServiceQuery;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.registry.RegistryService;
import com.dynatrace.microservices.utils.net.SimpleHostNameResolver;

@RestController("registry")
@ConditionalOnExpression("'${micro.service}'.equals('registry')")
public class RegistryController implements RegistryService, ExceptionHandler {
	
	@SuppressWarnings("unused")
	private @Autowired ServiceProperties props;
	private @Autowired HttpServletRequest request;

	@Override
	public ServiceInstance lookup(@RequestBody ServiceQuery query) {
		return ServiceApplication.getServiceRegistry().lookup(query);
	}
	
	@Override
	public ServiceInstance register(@RequestBody ServiceInstance instance, boolean correct) {
		Objects.requireNonNull(instance);
		Location location = instance.getLocation();
		Objects.requireNonNull(location);
		instance.setLocation(
			new DefaultLocation(
				SimpleHostNameResolver.INSTANCE.getHostName(request),
				location.getPort()
			)
		);
		if (ServiceApplication.getServiceRegistry().register(instance)) {
			// forward the registration of the service to the controller
			ServiceApplication.getRegistryService().register(instance, false);
		}
		return instance;
	}
	
	@Override
	public boolean unregister(@PathVariable String instanceId) {
		return ServiceApplication.getServiceRegistry().unregister(instanceId) != null;
	}
	
	@Override
	public ServiceInstance getInstance(@PathVariable String instanceId) {
		return ServiceApplication.getServiceRegistry().get(instanceId);
	}
	
	@Override
	public ServiceInstanceCollection getInstances() {
		ServiceInstanceCollection instances = new ServiceInstanceCollection();
		instances.addAll(ServiceApplication.getServiceRegistry().getInstances());
		return instances;
	}

	@Override
	public void handle(RestClientException e) {
		e.printStackTrace(System.err);
	}

}