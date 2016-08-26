package com.dynatrace.microservices.infrastructure;

import java.net.URL;

import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.utils.Strings;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = DefaultServiceInstance.class)
public interface ServiceInstance {
	
	public static final ServiceInstance UNDEFINED = new DefaultServiceInstance(
		"undefined",
		Service.UNDEFINED,
		Location.UNDEFINED
	);

	public static final ServiceInstance NOT_FOUND = new DefaultServiceInstance(
		"notfound",
		Service.UNDEFINED,
		Location.UNDEFINED
	);
	
	public static boolean equals(ServiceInstance a, ServiceInstance b) {
		if (a == b) {
			return true;
		}
		if ((a == null) && (b != null)) {
			return false;
		}
		if ((a != null) && (b == null)) {
			return false;
		}
		if (!Strings.equals(a.getId(), b.getId())) {
			return false;
		}
		if (!Service.equals(a.getService(), b.getService())) {
			return false;
		}
		if (!Location.equals(a.getLocation(), b.getLocation())) {
			return false;
		}
		return true;
	}
	
	String getId();
	Location getLocation();
	void setLocation(Location location);
	Service getService();
	URL createURL(String operation, boolean includeServiceName);
	URL createURL(String operation);
	
}
