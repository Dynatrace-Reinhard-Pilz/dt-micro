package com.dynatrace.microservices.processes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class CommandLine {

	private final Collection<String> parts = new ArrayList<String>();
	private final Map<String, String> properties = new HashMap<String, String>();
	
	public CommandLine() {
		String commandLine = System.getProperty("sun.java.command");
		StringTokenizer strTok = new StringTokenizer(commandLine, " ");
		while (strTok.hasMoreTokens()) {
			String part = strTok.nextToken().trim();
			if (!part.isEmpty()) {
				parts.add(part);
			}
		}
	}
	
	private boolean contains(String s) {
		for (String part : parts) {
			if (part.startsWith(s)) {
				return true;
			}
		}
		return false;
	}
	
	public void setProperty(String key, String value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		properties.put(key, value);
		if (!contains("--" + key + "=")) {
			parts.add("--" + key + "=" + value);
		}
	}
	
	public String first() {
		if (parts.isEmpty()) {
			return null;
		}
		return parts.iterator().next();
	}
	
	public Collection<String> toJvmArg() {
		if (parts.isEmpty()) {
			return Collections.emptyList();
		}
		ArrayList<String> result = new ArrayList<String>();
		if (first().endsWith(".jar")) {
			result.add("-jar");
		}
		for (String part : parts) {
			result.add(propify(part));
		}
		return result;
	}
	
	private String propify(String part) {
		Objects.requireNonNull(part);
		if (!part.startsWith("--")) {
			return part;
		}
		int idx = part.indexOf('=');
		String key = part.substring(2, idx);
		String property = properties.get(key);
		if (property == null) {
			return part;
		}
		return "--" + key + "=" + property;
	}
}
