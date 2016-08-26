package com.dynatrace.microservices;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = InstancesConfig.TAG)
@XmlAccessorType(XmlAccessType.FIELD)
public class InstancesConfig {
	
	public static final String TAG = "instances";
	
	private Collection<String> instances = new ArrayList<String>();
	
	public Collection<String> getInstances() {
		return instances;
	}
	
	public void setInstances(Collection<String> instances) {
		this.instances = instances;
	}
	
	public InstancesConfig add(String serviceId) {
		instances.add(serviceId);
		return this;
	}

}
