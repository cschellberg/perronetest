package com.perrone.test.util;

import java.util.Arrays;

import com.perrone.test.Constants;
import com.perrone.test.exceptions.InvalidDataFormat;

public class SocketUtils {
	public static byte[] toByteArray(int value) {
		return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}

	public static int fromByteArray(byte[] bytes) {
		return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8)
				| ((bytes[3] & 0xFF) << 0);
	}

	public static int[] convertToInts(byte[] inArray) {
		int[] retArray = new int[inArray.length / 4];
		for (int ii = 0; ii < retArray.length; ii++) {
			byte[] tmpArray = Arrays.copyOfRange(inArray, ii * 4, (ii + 1) * 4);
			int num = fromByteArray(tmpArray);
			retArray[ii] = num;
		}
		return retArray;
	}

	public static int[][] convertToIntArrays(byte[] inArray) {
		if (inArray == null || inArray.length == 0) {
			System.out.println("byte array is null or empty");
			return new int[][] {};
		}
		int numberOfRows = fromByteArray(Arrays.copyOfRange(inArray, Constants.ROW_OFFSET, Constants.COLUMN_OFFSET));
		int numberOfColumns = fromByteArray(Arrays.copyOfRange(inArray, Constants.COLUMN_OFFSET, Constants.BEGIN_DATA));
		int[][] retArray = new int[numberOfRows][numberOfColumns];
		for (int ii = 0; ii < numberOfRows; ii++) {
			for (int jj = 0; jj < numberOfColumns; jj++) {
				int offset = ii * numberOfColumns * 4 + jj * 4 + Constants.BEGIN_DATA;
				byte[] tmpArray = Arrays.copyOfRange(inArray, offset, offset + 4);
				int num = fromByteArray(tmpArray);
				retArray[ii][jj] = num;
			}
		}
		return retArray;
	}

	public static byte[] convertToBytes(byte processorType, int[][]... intArrays) {
		if (intArrays == null || intArrays.length == 0 || intArrays[0].length == 0) {
			return new byte[0];
		} else {
			int total = validateMatrixes(intArrays);
			byte[] retArray = new byte[total];
			retArray[Constants.PROCESS_TYPE_OFFSET] = processorType;
			SocketUtils.addIntegerAsBytes(retArray, Constants.N_OFFSET, intArrays.length);
			SocketUtils.addIntegerAsBytes(retArray, Constants.ROW_OFFSET, intArrays[0].length);
			SocketUtils.addIntegerAsBytes(retArray, Constants.COLUMN_OFFSET, intArrays[0][0].length);
			int cntr = Constants.BEGIN_DATA;
			for (int[][] intArray : intArrays) {
				for (int row = 0; row < intArray.length; row++) {
					for (int col = 0; col < intArray[0].length; col++) {
						int num = intArray[row][col];
						byte[] tmpArray = toByteArray(num);
						for (byte by : tmpArray) {
							retArray[cntr++] = by;
						}
					}
				}
			}
			return retArray;
		}

	}

	private static void addIntegerAsBytes(byte[] retArray, int offset, int value) {
		byte[] tmpArray = toByteArray(value);
		for (int ii = 0; ii < 4; ii++) {
			retArray[offset + ii] = tmpArray[ii];
		}

	}

	private static int validateMatrixes(int[][]... intArrays) {
		int columns = intArrays[0].length;
		int rows = intArrays[0][0].length;
		for (int[][] intArray : intArrays) {
			int tmpColumns = intArray.length;
			int tmpRows = intArray[0].length;
			if (tmpRows != rows || tmpColumns != columns) {
				throw new InvalidDataFormat("Matrixes must be of the same size");
			}
		}
		return 4 * rows * columns * intArrays.length + Constants.HEADER_BYTES;
	}

	public static byte[] convertToBytes(byte[] headerBytes, int[][] array) {
		int total = headerBytes.length + array.length * array[0].length * 4;
		byte[] retArray = new byte[total];
		copyToOffset(retArray, headerBytes, 0);
		int offset = Constants.BEGIN_DATA;
		for (int[] columns : array) {
			for (int value : columns) {
				byte[] tmpArray = toByteArray(value);
				copyToOffset(retArray, tmpArray, offset);
				offset += 4;
			}
		}
		return retArray;
	}

	public static void copyToOffset(byte[] dest, byte[] src, int offset) {
		for (int ii = 0; ii < src.length; ii++) {
			dest[offset + ii] = src[ii];
		}
	}

	public static byte[] convertToBytes(int[][][] matrixes) {
		byte[] retArray = new byte[matrixes.length * matrixes[0].length * matrixes[0][0].length * 4];
		int cntr = 0;
		for (int[][] intArray : matrixes) {
			for (int row = 0; row < intArray.length; row++) {
				for (int col = 0; col < intArray[row].length; col++) {
					int num = intArray[row][col];
					byte[] tmpArray = toByteArray(num);
					for (byte by : tmpArray) {
						retArray[cntr++] = by;
					}
				}
			}
		}
		return retArray;
	}

}
