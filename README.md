## Socket Matrixes Adder

### Assumptions

* Matrixes are dual dimensional arrays
* All matrixes have the same dimensions
* Null input will return an empty matrix
* The sum of N matrixes will return a single matrix with the same dimensions of the matrixes passed in.

### Message Structure

* Byte 0: This represents the processorType.  This will allow addition functionality to be built into the socket server.  Currently, there is only one processor, to sum matrixes.
* Byte 1-5: An integer representing the number of matrixes to be added
* Byte 5-9: An integer representing the number of rows in the matrix
* Byte 9-13: An integer representing the number of columns in the matrix
* Byte 13-:  The matrix data

### Overview

Both client and server sockets are agnostic regarding message format.  There only functions is to pass message packets back and forth.  The client receives a message as a byte array.  The server receives the message as a byte array and delegates the processing of that message to the MatrixProcessor which implements the Processor interface

### Usage

The class with the main method is the SocketRunner class. It takes an optional parameter to specify the port that the server will listen on.  If no port is passed in server defaults to port 6666.

java -jar dschellberg-0.0.1-SNAPSHOT.jar

### Addendum

There is also an implementation use SocketChannels.  This should give greater throughput because there are ten channels processing requests.  When using the SocketChannel implementation and END_OF_MESSAGE must be added to the end of each message.  SocketRunner uses the standard socket implementation which does not require this.

### Potential Optimization

Client should have a blocking queue that feeds the SocketChannel client.  This channel should be non-blocking and never close until application shutdown.  This client will continually write to a server-side SocketChannel that always remains open as well.
