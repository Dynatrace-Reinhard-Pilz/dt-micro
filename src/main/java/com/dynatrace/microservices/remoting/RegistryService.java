package com.dynatrace.microservices.remoting;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceQuery;

public interface RegistryService {

	ServiceInstance lookup(DefaultServiceQuery query);
	boolean register(DefaultServiceInstance instance);
	
}
