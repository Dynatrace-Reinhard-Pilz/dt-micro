package com.dynatrace.microservices.registry;

import java.net.URL;
import java.util.Objects;

import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.Service;
import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class SynchronizedServiceInstance implements ServiceInstance {

	private final Object lock = new Object();
	private ServiceInstance instance = null;
	
	public SynchronizedServiceInstance(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		this.instance = instance;
	}
	
	public void set(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		if (instance == this) {
			return;
		}
		synchronized (lock) {
			this.instance = instance;
		}
	}
	
	@Override
	public String getId() {
		synchronized (lock) {
			return instance.getId();
		}
	}
	
	@Override
	public Location getLocation() {
		synchronized (lock) {
			return instance.getLocation();
		}
	}
	
	@Override
	public Service getService() {
		synchronized (lock) {
			return instance.getService();
		}
	}
	
	@Override
	public URL createURL(String operation, boolean includeServiceName) {
		synchronized (lock) {
			return instance.createURL(operation, includeServiceName);
		}
	}
	
	@Override
	public URL createURL(String operation) {
		synchronized (lock) {
			return instance.createURL(operation);
		}
	}
	
	@Override
	public String toString() {
		synchronized (lock) {
			return instance.toString();
		}
	}

	@Override
	public void setLocation(Location location) {
		synchronized (lock) {
			instance.setLocation(location);
		}
	}
}
