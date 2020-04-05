package com.perrone.test.processors;

/**
 * @author dschellb
 * Processor interface that processes an array of bytes for some function
 *
 */
public interface Processor {

	/**
	 * @param array
	 * @return
	 */
	public byte[] process(byte[] array);

}