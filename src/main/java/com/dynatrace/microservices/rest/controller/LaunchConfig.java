package com.dynatrace.microservices.rest.controller;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.registry.DefaultLocation;

@XmlRootElement(name = LaunchConfig.TAG)
@XmlAccessorType(XmlAccessType.FIELD)
public class LaunchConfig {
	
	public static final String TAG = "launch-config";
	
	@XmlAttribute(name = "serviceId")
	private String serviceName = null;
	@XmlElement(type = DefaultLocation.class, name = "location")
	private Location location = null;
	@XmlAttribute(name = "registryInstanceId")
	private String registryInstanceId = null;
	
	public LaunchConfig() {
		
	}
	
	public LaunchConfig(String serviceId, Location location, String registryInstanceId) {
		Objects.requireNonNull(serviceId);
		Objects.requireNonNull(location);
		Objects.requireNonNull(registryInstanceId);
		this.serviceName = serviceId;
		this.registryInstanceId = registryInstanceId;
		this.location = location;
	}
	
	public static LaunchConfig get(String serviceId, String host, int port, String registryInstanceId) {
		return new LaunchConfig(
			serviceId,
			new DefaultLocation(host, port),
			registryInstanceId
		);
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public String getRegistryInstanceId() {
		return registryInstanceId;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setRegistryInstanceId(String registryInstanceId) {
		this.registryInstanceId = registryInstanceId;
	}
}
