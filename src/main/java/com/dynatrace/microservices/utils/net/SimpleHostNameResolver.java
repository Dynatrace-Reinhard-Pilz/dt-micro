package com.dynatrace.microservices.utils.net;

import javax.servlet.http.HttpServletRequest;

public final class SimpleHostNameResolver implements HostNameResolver {
	
	public static final HostNameResolver INSTANCE = new SimpleHostNameResolver();
	
	private SimpleHostNameResolver() {
	}

	@Override
	public String getHostName(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		return request.getRemoteAddr();
	}

}
