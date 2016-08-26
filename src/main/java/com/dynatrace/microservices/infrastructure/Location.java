package com.dynatrace.microservices.infrastructure;

import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.utils.Strings;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = DefaultLocation.class)
public interface Location {
	
	public static final Location UNDEFINED = new DefaultLocation("undefined", 0);
	
	public static boolean equals(Location a, Location b) {
		if (a == b) {
			return true;
		}
		if ((a == null) && (b != null)) {
			return false;
		}
		if ((a != null) && (b == null)) {
			return false;
		}
		if (!Strings.equals(a.getHost(), b.getHost())) {
			return false;
		}
		return a.getPort() == b.getPort();
	}

	String getHost();
	int getPort();
}
