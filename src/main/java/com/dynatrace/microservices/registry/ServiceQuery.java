package com.dynatrace.microservices.registry;

import com.dynatrace.microservices.infrastructure.Version;

public interface ServiceQuery {

	String getServiceId();
	Version getVersion();

}
