package com.dynatrace.microservices.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class ServiceInstanceRegistry {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(ServiceInstanceRegistry.class.getName());
	
	private static final HashMap<String, Collection<ServiceInstance>> instances =
			new HashMap<String, Collection<ServiceInstance>>();
	
	public boolean register(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		String serviceId = instance.getService().getServiceId();
		Objects.requireNonNull(serviceId);
		String instanceId = instance.getId();
		Objects.requireNonNull(instanceId);
		synchronized (instances) {
			Collection<ServiceInstance> serviceInstances = instances.get(serviceId);
			if (serviceInstances == null) {
				serviceInstances = new ArrayList<ServiceInstance>();
				instances.put(serviceId, serviceInstances);
			}
			for (Iterator<ServiceInstance> it = serviceInstances.iterator(); it.hasNext(); ) {
				ServiceInstance storedInstance = it.next();
				if (storedInstance == null) {
					it.remove();
					continue;
				}
				if (instanceId.equals(storedInstance.getId())) {
					it.remove();
				}
			}
			serviceInstances.add(instance);
		}
		return true;
	}
	
	public ServiceInstance unregister(String instanceId) {
		Objects.requireNonNull(instanceId);
		synchronized (instances) {
			for (Entry<String, Collection<ServiceInstance>> entry : instances.entrySet()) {
				Collection<ServiceInstance> serviceInstances = entry.getValue();
				if (serviceInstances == null) {
					continue;
				}
				for (Iterator<ServiceInstance> it = serviceInstances.iterator(); it.hasNext(); ) {
					ServiceInstance instance = it.next();
					if (instance == null) {
						continue;
					}
					if (instanceId.equals(instance.getId())) {
						it.remove();
						return instance;
					}
				}
			}
		}
		return null;
	}
	
	public Collection<ServiceInstance> lookup(ServiceQuery query) {
		Objects.requireNonNull(query);
		String serviceId = query.getServiceId();
		Objects.requireNonNull(serviceId);
		synchronized (instances) {
			Collection<ServiceInstance> serviceInstances = instances.get(serviceId);
			if (serviceInstances == null) {
				return Collections.emptyList();
			}
			return serviceInstances;
		}
	}
	
	public Collection<ServiceInstance> getInstances() {
		ArrayList<ServiceInstance> all = new ArrayList<ServiceInstance>();
		synchronized (instances) {
			for (Collection<ServiceInstance> values : instances.values()) {
				all.addAll(values);
			}
		}
		return all;
	}
	
	public ServiceInstance get(String instanceId) {
		Objects.requireNonNull(instanceId);
		synchronized (instances) {
			for (Entry<String, Collection<ServiceInstance>> entry : instances.entrySet()) {
				Collection<ServiceInstance> serviceInstances = entry.getValue();
				if (serviceInstances == null) {
					continue;
				}
				for (ServiceInstance instance : serviceInstances) {
					if (instance == null) {
						continue;
					}
					if (instanceId.equals(instance.getId())) {
						return instance;
					}
				}
			}
		}
		return null;
	}
	
}
