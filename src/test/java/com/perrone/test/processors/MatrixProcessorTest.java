package com.perrone.test.processors;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.perrone.test.Constants;
import com.perrone.test.util.SocketUtils;

public class MatrixProcessorTest {

	private MatrixProcessor matrixProcessor = new MatrixProcessor();

	@Test
	public void matrixProcessorTest() {
		int[] expectedArray = { 3, 6, 9, 12, 6, 9, 12, 15, 9, 12, 15, 18 };
		int[][][] matrixes = new int[][][] { { { 1, 2, 3, 4 }, { 2, 3, 4, 5 }, { 3, 4, 5, 6 } },
				{ { 1, 2, 3, 4 }, { 2, 3, 4, 5 }, { 3, 4, 5, 6 } },
				{ { 1, 2, 3, 4 }, { 2, 3, 4, 5 }, { 3, 4, 5, 6 } } };
		byte[] dataInput = SocketUtils.convertToBytes(matrixes);
		byte[] input = new byte[Constants.BEGIN_DATA + dataInput.length];
		input[0] = 1;
		SocketUtils.copyToOffset(input, SocketUtils.toByteArray(matrixes.length), Constants.N_OFFSET);
		SocketUtils.copyToOffset(input, SocketUtils.toByteArray(matrixes[0].length), Constants.COLUMN_OFFSET);
		SocketUtils.copyToOffset(input, SocketUtils.toByteArray(matrixes[0][0].length), Constants.ROW_OFFSET);
		SocketUtils.copyToOffset(input, dataInput, Constants.BEGIN_DATA);
		byte[] output = matrixProcessor.process(input);
		byte[] dataOuput = Arrays.copyOfRange(output, Constants.BEGIN_DATA, output.length);
		int[] outputInts = SocketUtils.convertToInts(dataOuput);
		for (int ii = 0; ii < outputInts.length; ii++) {
			assertEquals(expectedArray[ii], outputInts[ii]);

		}
		System.out.println("Matrix processor test was successful");
	}

}
