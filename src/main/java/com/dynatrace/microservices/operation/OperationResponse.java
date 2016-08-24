package com.dynatrace.microservices.operation;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operation-response")
@XmlAccessorType(XmlAccessType.FIELD)
public class OperationResponse {

	@XmlAttribute(name = "success")
	private boolean success = true;
	
	public boolean isSuccess() {
		return success;
	}
	
	public OperationResponse() {
	}
	
	public OperationResponse(boolean success) {
		this.success = success;
	}
	
	@XmlElementRef(type = OperationResponse.class)
	private Collection<OperationResponse> children = new ArrayList<OperationResponse>();
	
	public Collection<OperationResponse> getChildren() {
		return children;
	}
	
	public void add(OperationResponse response) {
		if (children == null) {
			children = new ArrayList<OperationResponse>();
		}
		children.add(response);
	}
}
