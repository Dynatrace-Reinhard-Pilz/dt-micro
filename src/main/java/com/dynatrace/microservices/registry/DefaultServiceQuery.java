package com.dynatrace.microservices.registry;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.infrastructure.Version;

@XmlRootElement(name = "service-query")
@XmlAccessorType(XmlAccessType.FIELD)
public class DefaultServiceQuery implements ServiceQuery {
	
	@XmlAttribute(name = "serviceId")
	private String serviceId = null;
	@XmlElement
	private Version version = null;
	
	public DefaultServiceQuery() {
		
	}
	
	public DefaultServiceQuery(String serviceId, Version version) {
		Objects.requireNonNull(serviceId);
		this.serviceId = serviceId;
		this.version = version;
	}
	
	@Override
	public String getServiceId() {
		return serviceId;
	}

	@Override
	public Version getVersion() {
		return version;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public void setVersion(Version version) {
		this.version = version;
	}

}
