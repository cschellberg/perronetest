package com.perrone.test.processors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.perrone.test.Constants;
import com.perrone.test.exceptions.UnsupportedProcessorType;

/**
 * @author dschellb Factory for creating a Processor based on the processor type
 *
 */
public class ProcessorFactory {

	private static Map<Byte, Processor> processorCache = new ConcurrentHashMap<>();

	/**
	 * @param processorType
	 * @return Processor
	 */
	public static Processor getProcessor(byte processorType) {
		return getProcessorFromCache(processorType);
	}

	private static Processor getProcessorFromCache(byte processorType) {
		Byte processorTypeObject = Byte.valueOf(processorType);
		return processorCache.computeIfAbsent(processorTypeObject, pt -> {
			switch (pt) {
			case Constants.MATRIX_PROCESSOR:
				return new MatrixProcessor();
			default:
				throw new UnsupportedProcessorType("Could not find processor type " + processorType);
			}
		});

	}

}
