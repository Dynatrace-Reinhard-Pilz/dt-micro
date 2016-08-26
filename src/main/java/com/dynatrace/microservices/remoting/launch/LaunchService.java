package com.dynatrace.microservices.remoting.launch;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.rest.controller.LaunchConfig;

public interface LaunchService {

	ServiceInstance launch(LaunchConfig launchConfig);
	
}
