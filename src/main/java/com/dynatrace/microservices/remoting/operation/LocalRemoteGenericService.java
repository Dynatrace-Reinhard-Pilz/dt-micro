package com.dynatrace.microservices.remoting.operation;

import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.operation.Operation;
import com.dynatrace.microservices.operation.OperationResponse;
import com.dynatrace.microservices.remoting.ExceptionHandler;

public class LocalRemoteGenericService implements GenericService {
	
	private final RemoteGenericService service;
	private final ExceptionHandler exceptionHandler;
	
	public LocalRemoteGenericService(RemoteGenericService service, ExceptionHandler exceptionHandler) {
		this.service = service;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public OperationResponse process(Operation operation) {
		try {
			return service.process(operation);
		} catch (RestClientException e) {
			exceptionHandler.handle(e);
			return new OperationResponse(false);
		}
	}

}
