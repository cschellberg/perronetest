package com.perrone.test.processors;

import java.util.Arrays;

import com.perrone.test.Constants;
import com.perrone.test.util.SocketUtils;

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
