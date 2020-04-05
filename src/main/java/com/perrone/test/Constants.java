package com.perrone.test;

public class Constants {

	public final static byte MATRIX_PROCESSOR = 1;
	// These are the amount of bytes need to describe the matrix
	public static final int HEADER_BYTES = 13;
	public static final int PROCESS_TYPE_OFFSET = 0;
	public static final int N_OFFSET = 1;
	public static final int COLUMN_OFFSET = 9;// integer 4 bytes
	public static final int ROW_OFFSET = 5;// integer 4 bytes
	public static final int BEGIN_DATA = 13;// integer 4 bytes
	public static final int DEFAULT_PORT=66;
	public static final int INVALID_PORT_SPECIFIED = 1;
}
