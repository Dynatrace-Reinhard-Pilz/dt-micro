package com.dynatrace.microservices.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.infrastructure.Version;
import com.dynatrace.microservices.registry.DefaultServiceQuery;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.operation.LocalRemoteGenericService;
import com.dynatrace.microservices.remoting.operation.RemoteGenericService;

@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Operation implements ExceptionHandler {

	@XmlElement(type=Operation.class, name = "operation", required = false)
    private Collection<Operation> operations = new ArrayList<Operation>();
	
	@XmlAttribute(name = "serviceId", required = false)
	private String serviceId = null;
	
	public Operation() {
		
	}
	
	public Operation(String serviceId) {
		Objects.requireNonNull(serviceId);
		this.serviceId = serviceId;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	
	public Collection<Operation> getOperations() {
		return operations;
	}
	
	public void add(Operation operation) {
		if (operation == null) {
			return;
		}
		operations.add(operation);
	}
	
	public OperationResponse execute() {
		OperationResponse response = new OperationResponse();
		for (Operation operation : operations) {
			String serviceId = operation.getServiceId();
			if (serviceId == null) {
				executeLocal(operation);
			} else {
				DefaultServiceQuery query = new DefaultServiceQuery(serviceId, Version.DEFAULT);
				ServiceInstance serviceInstance = ServiceApplication.getRegistryService().lookup(query);
				RemoteGenericService remoteOperationService = new RemoteGenericService(serviceInstance);
				LocalRemoteGenericService operationService = new LocalRemoteGenericService(remoteOperationService, this);
				response.add(operationService.process(operation));
			}
		}
		return response;
	}
	
	private void executeLocal(Operation operation) {
		
	}

	@Override
	public void handle(RestClientException e) {
		e.printStackTrace(System.err);
	}

}
