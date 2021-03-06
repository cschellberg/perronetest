package com.perrone.test;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author dschellb
 * Main class to start the socket server.  If no parameter is specified, server listens on
 * port 6666.  This default can be overridden by adding a command line parameter
 *
 */
public class SocketRunner {

	public static void main(String[] args) {
		PerroneSocketServer perroneSocketServer = null;
		int port = Constants.DEFAULT_PORT;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception ex) {
				System.out.println("Unable to convert " + args[0] + " to an integer. Exiting...");
				System.exit(Constants.INVALID_PORT_SPECIFIED);
			}
		}
		try {
			perroneSocketServer = new PerroneSocketServer(port);
			Thread thread = new Thread(perroneSocketServer);
			thread.start();

		} catch (IOException ex) {
			System.out.println("Unable to start socket server because " + ex.getMessage());
		}

		Scanner scanner = new Scanner(System.in);
		String command = "";
		while (!command.equals("q")) {
			System.out.print("Type q to terminate server");
			command = scanner.next();
		}
		try {
			if (perroneSocketServer != null) {
				perroneSocketServer.close();
			}
		} catch (IOException ex) {
			System.out.println("Unable to stop socket server because " + ex.getMessage());

		}
		scanner.close();
	}

}
