package com.dynatrace.microservices.processes;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InputArguments {

	private final String[] args;
	
	public InputArguments() {
		List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
		this.args = args.toArray(new String[args.size()]);
	}
	
	public Collection<String> toJvmArg() {
		if (args == null) {
			return Collections.emptyList();
		}
		if (args.length == 0) {
			return Collections.emptyList();
		}
		ArrayList<String> result = new ArrayList<String>();
		for (String arg : args) {
			if (arg.startsWith("-Dcom.sun.management.jmxremote.port")) {
				continue;
			}
			if (arg.startsWith("-agentlib")) {
				continue;
			}
			result.add(arg);
		}
		return result;
	}
}
