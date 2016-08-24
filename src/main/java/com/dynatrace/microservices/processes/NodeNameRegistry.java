package com.dynatrace.microservices.processes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class NodeNameRegistry {

	private static final Map<String, AtomicInteger> map = new HashMap<String, AtomicInteger>();
	
	public static int get(String serviceId) {
		Objects.requireNonNull(serviceId);
		synchronized (map) {
			AtomicInteger cnt = map.get(serviceId);
			if (cnt == null) {
				cnt = new AtomicInteger(0);
				map.put(serviceId, cnt);
			}
			return cnt.incrementAndGet();
		}
	}
}
