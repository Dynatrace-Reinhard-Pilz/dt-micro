package com.dynatrace.microservices.registry;

import com.dynatrace.microservices.infrastructure.Version;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = DefaultServiceQuery.class)
public interface ServiceQuery {

	String getServiceId();
	Version getVersion();

}
