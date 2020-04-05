package com.perrone.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.perrone.test.util.SocketUtils;

public class PerroneSocket {
	
	private int port=Constants.DEFAULT_PORT;
	
	public PerroneSocket() {
		super();
	}

	public PerroneSocket(int port) {
		super();
		this.port=port;
	}

	public int[][] connectToServerSocket(byte processorType, byte[] input) {
		if (input == null || input.length == 0) {
			return new int[][] {};
		}
		try (Socket socket = new Socket("localhost", port)) {
			OutputStream os = socket.getOutputStream();
			os.write(input);
			os.flush();
			InputStream is = socket.getInputStream();
			byte[] response = new byte[10000];
			is.read(response);
			int[][] retArray = SocketUtils.convertToIntArrays(response);
			return retArray;
		} catch (Exception ex) {
			System.out.println("Unable to send message to socket because" + ex.getMessage());
			return null;
		}
	}

}
