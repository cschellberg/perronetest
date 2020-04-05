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
	
	@Test 
	public void testIntByteConversions() {
		int expectedValue=234;
		byte[] output=SocketUtils.toByteArray(expectedValue);
		int actualValue=SocketUtils.fromByteArray(output);
		assertEquals(expectedValue, actualValue);
		expectedValue=-234;
		output=SocketUtils.toByteArray(expectedValue);
		actualValue=SocketUtils.fromByteArray(output);
		assertEquals(expectedValue, actualValue);
	}
	
	@Test
	public void testConvertToInts() {
		byte[] input=new byte[] {0,0,0,1,0,0,0,2,0,0,0,3,0,0,0,4,1,1,1,1};
		int [] output= SocketUtils.convertToInts(input);
		assertEquals(5,output.length);
		//first 4 numbers are sequence 1,2,3,4 last one is not
		for ( int ii=0;ii<output.length -1;ii++) {
			assertEquals(ii+1,output[ii]);
		}
		assertEquals(16843009,output[output.length-1]);
	}

}
