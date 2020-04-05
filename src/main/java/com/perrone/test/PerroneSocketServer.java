package com.perrone.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.perrone.test.processors.Processor;
import com.perrone.test.processors.ProcessorFactory;

public class PerroneSocketServer implements Runnable {

	private ServerSocket serverSocket;

	private boolean active = true;
	
	private int port=Constants.DEFAULT_PORT;

	public PerroneSocketServer() throws IOException {
		super();
		serverSocket = new ServerSocket(port);
	}

	public PerroneSocketServer(int port) throws IOException {
		super();
		this.port=port;
		serverSocket = new ServerSocket(port);
	}

	public void waitForConnection() {
		do {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				int available = is.available();
				while (available == 0) {
					available = is.available();
					Thread.sleep(10l);
				}
				byte[] targetArray = new byte[available];
				is.read(targetArray);
				Processor matrixProcessor = ProcessorFactory.getProcessor(targetArray[0]);
				byte[] retValue = matrixProcessor.process(targetArray);

				OutputStream os = socket.getOutputStream();
				os.write(retValue);
				os.flush();
			} catch (Exception ex) {
				System.out.println("Unable to process socket request because " + ex.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException ex) {
						System.out.println("Cannot close socket because " + ex.getMessage());
					}
				}
			}
		} while (active);
	}

	public void close() throws IOException {
		active = false;
		serverSocket.close();
	}

	public void run() {
		waitForConnection();
	}

}
