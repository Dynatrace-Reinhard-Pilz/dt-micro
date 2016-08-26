package com.dynatrace.microservices.registry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.Service;
import com.dynatrace.microservices.infrastructure.ServiceInstance;

@XmlRootElement(name = DefaultServiceInstance.TAG)
@XmlAccessorType(XmlAccessType.FIELD)
public final class DefaultServiceInstance implements ServiceInstance {
	
	public static final String TAG = "instance";

	@XmlAttribute(name = "id")
	private String id = null;
	@XmlElement(name = DefaultService.TAG, type = DefaultService.class)
	private Service service = null;
	@XmlElement(name = DefaultLocation.TAG, type = DefaultLocation.class)
	private Location location = null;
	
	public DefaultServiceInstance() {
		
	}
	
	public DefaultServiceInstance(String id, Service service, Location location) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(service);
		Objects.requireNonNull(location);
		this.id = id;
		this.service = new DefaultService(service);
		this.location = new DefaultLocation(location);
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public Service getService() {
		return service;
	}
	
	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public URL createURL(String operation) {
		return createURL(operation, true);
	}
	
	@Override
	public URL createURL(String operation, boolean includeServiceName) {
		Objects.requireNonNull(operation);
		try {
			if (includeServiceName) {
				return new URL("http", location.getHost(), location.getPort(), "/" + service.getServiceId() + "/" + operation);
			}
			return new URL("http", location.getHost(), location.getPort(), "/" + operation);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setService(DefaultService service) {
		this.service = service;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return id + "[" + service + "@" + location + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof ServiceInstance)
			return false;
		return equals((ServiceInstance) obj);
	}
	
	public boolean equals(ServiceInstance other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		if (location == null) {
			if (other.getLocation() != null)
				return false;
		} else if (!location.equals(other.getLocation()))
			return false;
		if (service == null) {
			if (other.getService() != null)
				return false;
		} else if (!service.equals(other.getService()))
			return false;
		return true;
	}
	
}
