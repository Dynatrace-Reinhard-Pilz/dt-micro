package com.dynatrace.microservices.rest.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceInstance;

@XmlRootElement(name = "instances")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceInstanceCollection implements Iterable<ServiceInstance> {
	
	private static final Random RAND = new Random(System.currentTimeMillis());	

	@XmlElement(type = DefaultServiceInstance.class)
	private Collection<ServiceInstance> instances = new ArrayList<ServiceInstance>(0);
	
	public ServiceInstanceCollection() {
	}
	
	public ServiceInstanceCollection(Collection<? extends ServiceInstance> entries) {
		if (entries != null) {
			instances.addAll(entries);
		}
	}
	
	public Collection<ServiceInstance> getInstances() {
		return instances;
	}
	
	public void setInstances(Collection<ServiceInstance> instances) {
		this.instances = instances;
	}
	
	public void add(ServiceInstance instance) {
		if (instance == null) {
			return;
		}
		instances.add(instance);
	}
	
	public void addAll(Collection<? extends ServiceInstance> instances) {
		if (instances == null) {
			return;
		}
		this.instances.addAll(instances);
	}

	@Override
	public Iterator<ServiceInstance> iterator() {
		return new ArrayList<ServiceInstance>(instances).iterator();
	}
	
	public Collection<ServiceInstance> all() {
		return new ArrayList<ServiceInstance>(instances);
	}
	
	public ServiceInstance random() {
		if (instances.isEmpty()) {
			return ServiceInstance.NOT_FOUND;
		}
		
		int r = RAND.nextInt(instances.size());
		Iterator<ServiceInstance> it = instances.iterator();
		ServiceInstance result = null;
		for (int i = 0; i <= r; i++) {
			result = it.next();
		}
		return result;
	}
}
