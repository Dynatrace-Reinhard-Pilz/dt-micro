package com.dynatrace.microservices.remoting.common;

import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.remoting.ExceptionHandler;

public class LocalRemoteCommonService implements CommonService {

	private final RemoteCommonService service;
	private final ExceptionHandler exceptionHandler;
	
	public LocalRemoteCommonService(RemoteCommonService service, ExceptionHandler exceptionHandler) {
		this.service = service;
		this.exceptionHandler = exceptionHandler;
	}
	
	@Override
	public boolean shutdown() {
		try {
			return service.shutdown();
		} catch (RestClientException e) {
			exceptionHandler.handle(e);
			return false;
		}
	}
}
