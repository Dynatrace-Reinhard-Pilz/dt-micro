package com.dynatrace.microservices.rest.registry;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.DefaultServiceInstance;

@XmlRootElement(name = "instances")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceInstanceCollection {

	@XmlElement(type = DefaultServiceInstance.class)
	private Collection<ServiceInstance> instances = new ArrayList<ServiceInstance>(0);
	
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
}
