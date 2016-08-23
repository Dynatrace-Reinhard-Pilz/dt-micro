package com.dynatrace.microservices.registry;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.infrastructure.Service;
import com.dynatrace.microservices.infrastructure.Version;

@XmlRootElement(name = "service")
@XmlAccessorType(XmlAccessType.FIELD)
public final class DefaultService implements Service {

	@XmlAttribute(name = "serviceId")
	private String serviceId = null;
	@XmlElement
	private Version version = null;
	
	public DefaultService() {
		
	}
	
	public DefaultService(String serviceId, Version version) {
		Objects.requireNonNull(serviceId);
		Objects.requireNonNull(version);
		this.serviceId = serviceId;
		this.version = version;
	}
	
	public DefaultService(Service service) {
		Objects.requireNonNull(service);
		this.serviceId = service.getServiceId();
		Objects.requireNonNull(serviceId);
		this.version = service.getVersion();
		Objects.requireNonNull(version);
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Override
	public String getServiceId() {
		return serviceId;
	}
	
	@Override
	public Version getVersion() {
		return version;
	}
	
	public void setVersion(Version version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return serviceId + "_" + version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Service)
			return false;
		return equals((Service) obj);

	}
	
	public boolean equals(Service other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (serviceId == null) {
			if (other.getServiceId() != null)
				return false;
		} else if (!serviceId.equals(other.getServiceId()))
			return false;
		if (version == null) {
			if (other.getVersion() != null)
				return false;
		} else if (!version.equals(other.getVersion()))
			return false;
		return true;
	}
	
	
}
