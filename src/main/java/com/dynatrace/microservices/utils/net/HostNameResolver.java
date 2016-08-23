package com.dynatrace.microservices.utils.net;

import javax.servlet.http.HttpServletRequest;

public interface HostNameResolver {

	String getHostName(HttpServletRequest request);
}
