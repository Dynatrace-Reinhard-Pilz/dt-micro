package com.dynatrace.microservices.utils.net;

import java.io.IOException;
import java.net.ServerSocket;

public final class PortUtil {
	
	private PortUtil() {
	}

	public static int getFreePort() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		int newServerPort = serverSocket.getLocalPort();
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newServerPort;
	}

}
