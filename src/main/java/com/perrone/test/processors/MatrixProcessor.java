package com.perrone.test.processors;

import java.util.Arrays;

import com.perrone.test.Constants;
import com.perrone.test.util.SocketUtils;

/**
 * @author dschellb
 * Class to sum N matrixes
 * Please see README file for actual structure of message
 * All matrixes are of the same dimensions including the sum of N matrixes
 * Class iterates by row and column
 * For a given row, column it iterates through all matrixes for that particular row column and calculates
 * the sum for that particular cell.  This process produces a dual dimension array of ints that
 * represent the sums of N matrixes.  It is then converted to an output byte array by adding
 * the header bytes(13 bytes that define processorType, number of matrixes, number of rows in 
 * each matrix and number of columns for each matrix.  It then converts the int array to bytes 
 * and copies it to position 13 of the return array.
 *
 */
public class MatrixProcessor implements Processor {

	@Override
	public byte[] process(byte[] array) {
		int numberOfMatrixes = SocketUtils
				.fromByteArray(Arrays.copyOfRange(array, Constants.N_OFFSET, Constants.COLUMN_OFFSET));
		int numberOfColumns = SocketUtils
				.fromByteArray(Arrays.copyOfRange(array, Constants.ROW_OFFSET, Constants.COLUMN_OFFSET));
		int numberOfRows = SocketUtils
				.fromByteArray(Arrays.copyOfRange(array, Constants.COLUMN_OFFSET, Constants.BEGIN_DATA));
		byte[] dataArray = Arrays.copyOfRange(array, Constants.BEGIN_DATA, array.length);
		byte[] headerBytes = Arrays.copyOfRange(array, Constants.PROCESS_TYPE_OFFSET, Constants.BEGIN_DATA);
		int[][] tmpArray = new int[numberOfRows][numberOfColumns];
		int offsetDelta = numberOfColumns * numberOfRows * 4;
		for (int ii = 0; ii < numberOfRows; ii++) {
			for (int jj = 0; jj < numberOfColumns; jj++) {
				int offset = ii * numberOfColumns * 4 + jj * 4;
				for (int kk = 0; kk < numberOfMatrixes; kk++) {
					byte[] tmpArr = Arrays.copyOfRange(dataArray, offset, offset + 4);
					int value = SocketUtils.fromByteArray(tmpArr);
					tmpArray[ii][jj] += value;
					offset += offsetDelta;
				}
			}
		}
		return SocketUtils.convertToBytes(headerBytes, tmpArray);
	}

}
