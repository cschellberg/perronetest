package com.perrone.test.channel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.perrone.test.Constants;
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
public class PerroneChannelSocket {
	
	private int port=Constants.DEFAULT_PORT_CHANNEL;
	
	public PerroneChannelSocket() {
		super();
	}

	public PerroneChannelSocket(int port) {
		super();
		this.port=port;
	}

	public int[][] connectToServerSocket(byte processorType, byte[] input) {
		if (input == null || input.length == 0) {
			return new int[][] {};
		}
		try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(port))) {
            ByteBuffer byteBuffer=ByteBuffer.allocate(input.length+Constants.END_OF_MESSAGE.length);
            byteBuffer.put(input);
            byteBuffer.put(Constants.END_OF_MESSAGE);
            byteBuffer.position(0);
			int bytesWritten=socketChannel.write(byteBuffer);
			int bytesRead=0;
			ByteBuffer responseBuffer = ByteBuffer.allocate(1000);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			while ((bytesRead=socketChannel.read(responseBuffer)) != -1) {
				byteArrayOutputStream.write(responseBuffer.array());
				if ( SocketUtils.endOfMessage(byteBuffer)) {
					break;
				}
				if (!responseBuffer.hasRemaining()) {
					responseBuffer.clear();
				}
			}
			byteArrayOutputStream.flush();
			int[][] retArray = SocketUtils.convertToIntArrays(byteArrayOutputStream.toByteArray());
			return retArray;
		} catch (Exception ex) {
			System.out.println("Unable to send message to socket because" + ex.getMessage());
			return null;
		}
	}

}
