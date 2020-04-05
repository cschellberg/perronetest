package com.perrone.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.perrone.test.util.SocketUtils;

public class PerroneSocketTest {

	private static PerroneSocketServer perroneSocketServer;

	@BeforeClass
	public static void init() {
		try {
			perroneSocketServer = new PerroneSocketServer();
			Thread thread = new Thread(perroneSocketServer);
			thread.start();

		} catch (IOException ex) {
			fail("Unable to start the socker server because " + ex.getMessage());
		}
	}

	// @AfterClass
	public static void destroy() {
		try {
			perroneSocketServer.close();
		} catch (IOException ex) {
			System.out.println("Unable to close socket server because " + ex.getMessage());
		}
	}

	@Test
	public void socketMatrixTest() {
		PerroneSocket perroneSocket = new PerroneSocket();
		int[][] testArray1 = new int[][] { { 200, 150, 100, 50, 8 }, { 200, 150, 100, 50, 8 } };
		int[][] testArray2 = new int[][] { { 5, 1, 3, 50, 18 }, { 5, 1, 3, 50, 18 } };
		int[][] matrixSum = perroneSocket.connectToServerSocket(Constants.MATRIX_PROCESSOR,
				SocketUtils.convertToBytes(Constants.MATRIX_PROCESSOR, testArray1, testArray1));
		validateMatrixSum(matrixSum, testArray1);
		matrixSum = perroneSocket.connectToServerSocket(Constants.MATRIX_PROCESSOR,
				SocketUtils.convertToBytes(Constants.MATRIX_PROCESSOR, testArray2, testArray2));
		validateMatrixSum(matrixSum, testArray2);
		matrixSum = perroneSocket.connectToServerSocket(Constants.MATRIX_PROCESSOR,
				SocketUtils.convertToBytes(Constants.MATRIX_PROCESSOR, new int[][] {}));
		matrixSum = perroneSocket.connectToServerSocket(Constants.MATRIX_PROCESSOR, null);

	}

	private void validateMatrixSum(int[][] matrixSum, int[][] testArray1) {
		for (int ii = 0; ii < testArray1.length; ii++) {
			for (int jj = 0; jj < testArray1[ii].length; jj++) {
				int expectedValue = testArray1[ii][jj] * 2;
				int actualValue = matrixSum[ii][jj];
				assertEquals(expectedValue, actualValue);
			}
		}

	}

	@Test
	public void loadTest() throws InterruptedException, ExecutionException, TimeoutException {
		int numberOfCalls = 500;
		ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfCalls);
		Collection<Callable<int[][]>> runnables = new ArrayList<Callable<int[][]>>();
		for (int ii = 0; ii < numberOfCalls; ii++) {
			runnables.add(new Callable<int[][]>() {
				@Override
				public int[][] call() throws Exception {
					PerroneSocket perroneSocket = new PerroneSocket();
					int[][] retValue = perroneSocket.connectToServerSocket(Constants.MATRIX_PROCESSOR,
							SocketUtils.convertToBytes(Constants.MATRIX_PROCESSOR,
									new int[][] { { 200, 150, 100, 50, 8 }, { 200, 150, 100, 50, 8 } }));
					return retValue;
				}

			});
		}
		List<Future<int[][]>> futures = forkJoinPool.invokeAll(runnables);
		int errors = 0;
		int cntr = 0;
		for (Future<int[][]> future : futures) {
			int[][] retValue = future.get(30l, TimeUnit.SECONDS);
			if (retValue.length == 0 || retValue[0].length == 0) {
				errors++;
			} else {
				cntr++;
			}
		}
		assertEquals("There should be no errors", 0, errors);
		assertEquals(numberOfCalls, cntr);
		System.out.println("Successfully finished load test");

	}

}
