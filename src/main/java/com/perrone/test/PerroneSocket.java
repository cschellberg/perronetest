package com.perrone.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.perrone.test.util.SocketUtils;

/**
 * @author dschellb
 * 
 * This is the client which accepts an array of bytes according to the message format 
 * specified in the README document.  It receives an array of bytes from the server 
 * representing the sum of N matrixes.  Client than calls a SocketUtils function to
 * convert the byte array  to  a int array of 2 dimensions
 *
 */
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
			int bytesRead;
			byte[] responseBuffer = new byte[1000];
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			while ((bytesRead=is.read(responseBuffer)) != -1) {
				 buffer.write(responseBuffer, 0, bytesRead);
			}
			buffer.flush();
			int[][] retArray = SocketUtils.convertToIntArrays(buffer.toByteArray());
			return retArray;
		} catch (Exception ex) {
			System.out.println("Unable to send message to socket because" + ex.getMessage());
			return null;
		}
	}

}
