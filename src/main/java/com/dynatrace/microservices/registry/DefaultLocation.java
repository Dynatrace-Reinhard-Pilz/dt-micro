package com.dynatrace.microservices.registry;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.infrastructure.Location;

@XmlRootElement(name = DefaultLocation.TAG)
@XmlAccessorType(XmlAccessType.FIELD)
public final class DefaultLocation implements Location {
	
	public static final String TAG = "location";

	@XmlAttribute(name = "host")
	private String host = null;
	
	@XmlAttribute(name = "port")
	private int port = 0;
	
	public DefaultLocation() {
		
	}
	
	public DefaultLocation(String host, int port) {
		Objects.requireNonNull(host);
		this.host = host;
		this.port = port;
	}
	
	public DefaultLocation(Location location) {
		Objects.requireNonNull(location);
		this.host = location.getHost();
		this.port = location.getPort();
	}
	
	@Override
	public String getHost() {
		return host;
	}
	
	@Override
	public int getPort() {
		return port;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString() {
		return host + ":" + port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Location)
			return false;
		return equals((Location) obj);
	}
	
	public boolean equals(Location other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (host == null) {
			if (other.getHost() != null)
				return false;
		} else if (!host.equals(other.getHost()))
			return false;
		if (port != other.getPort())
			return false;
		return true;
	}	
	
}
