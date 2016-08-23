package com.dynatrace.microservices.infrastructure;

import java.net.URL;

public interface ServiceInstance {

	String getId();
	Location getLocation();
	Service getService();
	URL createURL(String operation);
	
}
