package com.dynatrace.microservices.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.logging.LogFactory;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.utils.Strings;

public abstract class AbstractInstanceRegistry<K, V extends InstanceRegistry<?>> implements InstanceRegistry<K> {

	protected final HashMap<K, V> entries = new HashMap<>();
	
	@Override
	public void register(ServiceInstance instance) {
		synchronized (entries) {
			K key = evaluateKey(instance);
			V value = entries.get(key);
			if (value == null) {
				value = newValue();
				entries.put(key, value);
			}
			value.register(instance);
		}
	}
	
	@Override
	public ServiceInstance lookup(ServiceQuery query) {
		Objects.requireNonNull(query);
		V value = null;
		synchronized (entries) {
			K key = evaluateKey(query);
			if (key == null) {
				if (entries.isEmpty()) {
					return null;
				}
				key = entries.keySet().iterator().next();
			}
			if (key != null) {
				value = entries.get(key);
			}
		}
		if (value == null) {
			return null;
		}
		return value.lookup(query);
	}
	
	@Override
	public Collection<ServiceInstance> getInstances() {
		ArrayList<ServiceInstance> instances = new ArrayList<ServiceInstance>();
		synchronized (entries) {
			for (V value : entries.values()) {
				if (value == null) {
					continue;
				}
				instances.addAll(value.getInstances());
			}
		}
		return instances;
	}
	
	@Override
	public ServiceInstance unregister(String instanceId) {
		synchronized (entries) {
			for (V value : entries.values()) {
				if (value == null) {
					continue;
				}
				ServiceInstance instance = value.unregister(instanceId);
				if (instance != null) {
					return instance;
				}
			}
		}
		return null;
	}
	
	@Override
	public ServiceInstance get(String instanceId) {
		synchronized (entries) {
			for (V value : entries.values()) {
				if (value == null) {
					continue;
				}
				ServiceInstance instance = value.get(instanceId);
				if (instance != null) {
					return instance;
				}
			}
		}
		return null;
	}
	
	private V newValue() {
		try {
			return valueType().newInstance();
		} catch (Throwable e) {
			throw new InternalError(e.getMessage());
		}
	}
	
	@Override
	public ServiceInstance unregister(ServiceInstance instance) {
		synchronized (entries) {
			K key = evaluateKey(instance);
			V value = entries.get(key);
			if (value == null) {
				return null;
			}
			return value.unregister(instance);
		}
	}
	
	@Override
	public void dump(int indent) {
		synchronized (entries) {
			Set<K> keys = entries.keySet();
			for (K key : keys) {
				LogFactory.getLog(getClass().getName()).info(Strings.indent(indent) + key);
				V value = entries.get(key);
				value.dump(indent + 2);
			}
		}
	}
	
	protected abstract Class<? extends V> valueType();
	protected abstract K evaluateKey(ServiceInstance instance);
	protected abstract K evaluateKey(ServiceQuery query);
	
	
}
