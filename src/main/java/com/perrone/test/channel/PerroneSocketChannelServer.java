package com.perrone.test.channel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.perrone.test.Constants;
import com.perrone.test.processors.Processor;
import com.perrone.test.processors.ProcessorFactory;
import com.perrone.test.util.SocketUtils;

/**
 * @author dschellb
 * 
 *         This the socket server that listens on the specified port. It
 *         receives a message packet (See README.md file for message structure).
 *         It uses the first byte to determine what processor to call and than
 *         delegates to the processor to calculate the matrix sum which it
 *         returns as an array of bytes with the same message format.
 *
 */
public class PerroneSocketChannelServer implements Runnable {

	private ServerSocket serverSocket;

	private boolean active = true;

	private int port = Constants.DEFAULT_PORT_CHANNEL;

	private int poolSize = 10;

	private ServerSocketChannel serverSocketChannel = null;

	public PerroneSocketChannelServer() throws IOException {
		super();
		initChannelPool();
	}

	public PerroneSocketChannelServer(int port) throws IOException {
		super();
		this.port = port;
		initChannelPool();
	}

	private void initChannelPool() throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
	}

	public void waitForConnection() {
		for (int ii = 0; ii < poolSize; ii++) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					SocketChannel socketChannel = null;
					do {

						try {
							socketChannel = serverSocketChannel.accept();
							ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
							int bytesRead = 0;
							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							while ((bytesRead = socketChannel.read(byteBuffer)) != -1) {
								byteArrayOutputStream.write(byteBuffer.array());
								if ( SocketUtils.endOfMessage(byteBuffer)) {
									break;
								}
								if (!byteBuffer.hasRemaining()) {
									byteBuffer.clear();
								}
							}
							byteArrayOutputStream.flush();
							byte[] inputArray = byteArrayOutputStream.toByteArray();
							Processor matrixProcessor = ProcessorFactory.getProcessor(inputArray[0]);
							byte[] retValue = matrixProcessor.process(inputArray);
							ByteBuffer returnBuffer = ByteBuffer.allocate(retValue.length);
							returnBuffer.put(retValue);
							returnBuffer.position(0);
							socketChannel.write(returnBuffer);
						} catch (Exception ex) {
							System.out.println("Unable to process socket request because " + ex.getMessage());
						} finally {
							if (socketChannel != null) {
								try {
									socketChannel.close();
								} catch (IOException ex) {
									System.out.println("Cannot close socket because " + ex.getMessage());
								}
							}
						}
					} while (active);

				}
			});
			thread.start();
		}
	}

	public void close() throws IOException {
		active = false;
		serverSocket.close();
	}

	public void run() {
		waitForConnection();
	}

}
