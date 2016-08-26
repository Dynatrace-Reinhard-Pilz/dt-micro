package com.dynatrace.microservices.infrastructure;

import com.dynatrace.microservices.registry.DefaultService;
import com.dynatrace.microservices.registry.ServiceQuery;
import com.dynatrace.microservices.utils.Strings;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = DefaultService.class)
public interface Service extends ServiceQuery {

	public static final Service UNDEFINED = new DefaultService("undefined", Version.DEFAULT);
	
	public static boolean equals(Service a, Service b) {
		if (a == b) {
			return true;
		}
		if ((a == null) && (b != null)) {
			return false;
		}
		if ((a != null) && (b == null)) {
			return false;
		}
		if (!Strings.equals(a.getServiceId(), b.getServiceId())) {
			return false;
		}
		if (!Version.equals(a.getVersion(), b.getVersion())) {
			return false;
		}
		return true;
	}
		
}
