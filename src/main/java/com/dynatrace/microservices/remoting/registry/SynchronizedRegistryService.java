package com.dynatrace.microservices.remoting.registry;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.registry.ServiceInstanceRegistry;
import com.dynatrace.microservices.registry.ServiceQuery;
import com.dynatrace.microservices.rest.registry.ServiceInstanceCollection;

public class SynchronizedRegistryService implements RegistryService {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(SynchronizedRegistryService.class.getName());
	
	private final Object lock = new Object();
	private RegistryService service = null;
	private final ServiceInstanceRegistry serviceRegistry = new ServiceInstanceRegistry();
	
	public SynchronizedRegistryService() {
	}

	public SynchronizedRegistryService(RegistryService service) {
		Objects.requireNonNull(service);
	}
	
	public void set(RegistryService service) {
		Objects.requireNonNull(service);
		synchronized (lock) {
			this.service = service;
		}
	}
	
	@Override
	public ServiceInstanceCollection lookup(ServiceQuery query) {
		Objects.requireNonNull(query);
		synchronized (lock) {
			if (service == null) {
				return new ServiceInstanceCollection();
			}
			Collection<ServiceInstance> serviceInstances = serviceRegistry.lookup(query);
			if ((serviceInstances != null) && !serviceInstances.isEmpty()) {
				return new ServiceInstanceCollection(serviceInstances);
			}
			ServiceInstanceCollection serviceInstanceCollection = lookupRemote(query);
			if (serviceInstanceCollection != null) {
				for (ServiceInstance serviceInstance : serviceInstanceCollection) {
					serviceRegistry.register(serviceInstance);
				}
			}
			return serviceInstanceCollection;
		}
	}
	
	private static class Ref<T> {
		public T value = null;
	}
	
	private ServiceInstanceCollection lookupRemote(ServiceQuery query) {
		Objects.requireNonNull(query);
		final Ref<ServiceInstanceCollection> ref = new Ref<ServiceInstanceCollection>();
		CountDownLatch latch = new CountDownLatch(1);
		Thread thread = new Thread() {
			@Override
			public void run() {
				synchronized (ref) {
					ref.value = service.lookup(query);
				}
				latch.countDown();
			}
		};
		thread.setDaemon(true);
		thread.start();
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// ignore
		}
		synchronized (ref) {
			return ref.value;
		}
	}

	@Override
	public boolean unregister(String instanceId) {
		Objects.requireNonNull(instanceId);
		synchronized (lock) {
			if (service == null) {
				return false;
			}
			return service.unregister(instanceId);
		}
	}

	@Override
	public ServiceInstance register(ServiceInstance instance, boolean correct) {
		Objects.requireNonNull(instance);
		synchronized (lock) {
			if (service == null) {
				return instance;
			}
			return service.register(instance, correct);
		}
	}

	@Override
	public ServiceInstance getInstance(String instanceId) {
		Objects.requireNonNull(instanceId);
		synchronized (lock) {
			if (service == null) {
				return null;
			}
			return service.getInstance(instanceId);
		}
	}

	@Override
	public ServiceInstanceCollection getInstances() {
		synchronized (lock) {
			if (service == null) {
				return null;
			}
			return service.getInstances();
		}
	}

}
