package com.perrone.test.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.perrone.test.Constants;

public class SocketUtilsTest {

	@Test
	public void testIntMartixConversion() {
		int[][] expectedArray = new int[][] { { 1, 2, 3 }, { 1, 2, 3 } };
		int expectedTotal = 12;
		byte[] testArray = SocketUtils.convertToBytes(Constants.MATRIX_PROCESSOR, expectedArray);
		int[] convertedArray = SocketUtils
				.convertToInts(Arrays.copyOfRange(testArray, Constants.BEGIN_DATA, testArray.length));
		int actualTotal = 0;
		for (int num : convertedArray) {
			actualTotal += num;
		}
		assertEquals(expectedTotal, actualTotal);
	}

}
