package com.perrone.test.processors;

import com.perrone.test.Constants;
import com.perrone.test.exceptions.UnsupportedProcessorType;

/**
 * @author dschellb
 * Factory for creating a Processor based on the processor type
 *
 */
public class ProcessorFactory {

	/**
	 * @param processorType
	 * @return Processor
	 */
	public static Processor getProcessor(byte processorType) {
		switch (processorType) {
		case Constants.MATRIX_PROCESSOR:
			return new MatrixProcessor();
		default:
			throw new UnsupportedProcessorType("Could not find processor type " + processorType);
		}
	}

}
