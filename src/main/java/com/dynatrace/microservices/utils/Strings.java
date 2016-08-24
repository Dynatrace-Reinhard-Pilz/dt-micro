package com.dynatrace.microservices.utils;

public final class Strings {
	
	private Strings() {
		
	}

	public static String indent(int indent) {
		if (indent <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
