package com.dynatrace.microservices.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.utils.Strings;

public class ServiceInstances extends AbstractInstanceRegistry<String, ServiceInstances.ServiceInstanceWrapper> {
	
	private static final Log LOGGER = LogFactory.getLog(ServiceInstances.class.getName());

	@Override
	public Class<ServiceInstances.ServiceInstanceWrapper> valueType() {
		return ServiceInstances.ServiceInstanceWrapper.class;
	}
	
	@Override
	public String evaluateKey(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		return instance.getId();
	}

	@Override
	protected String evaluateKey(ServiceQuery query) {
		return null;
	}
	
	protected static final class ServiceInstanceWrapper implements InstanceRegistry<DefaultServiceInstance> {

		private ServiceInstance instance = null;
		
		public ServiceInstanceWrapper() {
			
		}
		
		public ServiceInstanceWrapper(ServiceInstance instance) {
			Objects.requireNonNull(instance);
			this.instance = instance;
		}
		
		public void setInstance(ServiceInstance instance) {
			this.instance = instance;
		}
		
		public ServiceInstance getInstance() {
			return instance;
		}

		@Override
		public boolean register(ServiceInstance instance) {
			if (!ServiceInstance.equals(this.instance, instance)) {
				setInstance(instance);
				return true;
			}
			return false;
		}

		@Override
		public ServiceInstance unregister(ServiceInstance instance) {
			Objects.requireNonNull(instance);
			if (this.instance == null) {
				return null;
			}
			if (this.instance.getId().equals(instance.getId())) {
				setInstance(null);
				return instance;
			}
			return null;
		}
		
		@Override
		public ServiceInstance get(String instanceId) {
			Objects.requireNonNull(instanceId);
			if (instance == null) {
				return null;
			}
			if (this.instance.getId().equals(instance.getId())) {
				return instance;
			}
			return null;
		}
		
		@Override
		public ServiceInstance unregister(String instanceId) {
			Objects.requireNonNull(instanceId);
			if (instance == null) {
				return null;
			}
			if (instanceId.equals(instance.getId())) {
				ServiceInstance result = instance;
				instance = null;
				return result;
			}
			return null;
		}
		
		@Override
		public String toString() {
			return instance.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((instance == null) ? 0 : instance.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ServiceInstanceWrapper other = (ServiceInstanceWrapper) obj;
			if (instance == null) {
				if (other.instance != null)
					return false;
			} else if (!instance.equals(other.instance))
				return false;
			return true;
		}

		@Override
		public Collection<ServiceInstance> lookup(ServiceQuery query) {
			ArrayList<ServiceInstance> result = new ArrayList<ServiceInstance>();
			result.add(instance);
			return result;
		}

		@Override
		public Collection<ServiceInstance> getInstances() {
			if (instance == null) {
				return Collections.emptyList();
			}
			ArrayList<ServiceInstance> list = new ArrayList<ServiceInstance>(1);
			list.add(instance);
			return list;
		}

		@Override
		public void dump(int indent) {
			if (instance != null) {
				LOGGER.info(Strings.indent(indent) + instance.toString());
			}
		}
		
	}

}
