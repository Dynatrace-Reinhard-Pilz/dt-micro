package com.dynatrace.microservices;

import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.registry.DefaultLocation;

public class RegistryConfig {
	
	public static Location parse(String property) {
		if (property == null) {
			return new DefaultLocation("localhost", 0);
		}
		int idx = property.indexOf(':');
		if (idx == -1) {
			return new DefaultLocation(property, 0);
		}
		String host = property.substring(0, idx);
		int port = Integer.parseInt(property.substring(idx + 1));
		return new DefaultLocation(host, port);
	}

}
