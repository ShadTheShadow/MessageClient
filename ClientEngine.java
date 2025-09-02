
/*
 * Created by Maxwell Weston and Evan Williams
 */
import java.net.Socket; // for client side interactions
import java.net.SocketAddress;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.crypto.*;
import java.security.*;

/**
 * 
 */
public class ClientEngine {

	/**
	 * 
	 * @param endAddress
	 * @param PORT
	 * @param message
	 */
	public static void chatEngine(String endAddress, int PORT, String message, String username, String destUser) {
		Socket server = connectSocket(endAddress, PORT);
		if (server == null) {
			System.out.println("Failed to send message! Could the port be blocked?");
			return;
		}

		boolean connected = true;
		String receivedMessage;

		PrintWriter output = null;
		BufferedReader input = null;

		try {
			output = new PrintWriter(server.getOutputStream(), true);
			input = new BufferedReader(
					new InputStreamReader(server.getInputStream()));

			// login whoami process
			output.println(username);
			System.out.println(input.readLine());

		} catch (Exception e) {
			System.out.println(e);
		}

		// main message loop
		while (connected) {

			try {

				if (message.length() > 0) {
					output.println(destUser + "|" + message);
				}

				receivedMessage = input.readLine();
				if (receivedMessage != null) {
					System.out.println("Recieved: " + receivedMessage);
				}

			} catch (Exception ex) {
				connected = false;
				System.out.println("Exception occured, disconnected!");
			}
		}

		try {
			output.close();
			input.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO
	 * 
	 * @param endAddress
	 * @param port
	 * @return endpoint
	 */
	public static Socket connectSocket(String endAddress, int port) {
		SocketAddress serverIp = new InetSocketAddress(endAddress, port);
		Socket server = new Socket();
		for (int i = 1; i <= 5; ++i) {
			try {
				System.out.println("Connecting to: " + serverIp);
				server.connect(serverIp);
				System.out.println("Connected to " + serverIp);
			} catch (Exception ex) {
				System.out.println("Failed to connect! Waiting 1 second. Tried: " + i);

				// Wait for 1 second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				continue; // Loops again if no connection is made
			}
			return server; // returns the connection if it is made
		}
		return null; // returns null if connection failed
	}

	public static boolean userAuth(String username, String password, String destination) {
		// TODO authenticate with EXISTING key

		return true;
	}

	public static boolean verifyCreds(String credential) {
		// TODO make more robust
		if (credential.length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * TODO WIP, for encryption
	 * 
	 * @param username
	 * @param password
	 */
	public static void genAuthKeys(String username, String password) {

		try {
			// Declare key generator and generate key
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
			keyGen.initialize(4096);
			KeyPair pair = keyGen.generateKeyPair();

			PublicKey publicKey = pair.getPublic();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String localAddress;
		String endAddress = "kronkserver.tplinkdns.com";
		String message = "";
		boolean valid;
		Scanner in = new Scanner(System.in);
		final int PORT = 55935;

		// fetch local and remote addresses
		try {
			endAddress = InetAddress.getByName(endAddress).getHostAddress().toString();
			localAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Local Address: " + localAddress);
			System.out.println("End address: " + endAddress);
		} catch (Exception ex) {
			System.out.println("Unable to determine address!");
			in.close();
			return;
		}

		String username = "";

		do {
			String password = "";

			do {

				System.out.println("Enter your username: ");

				valid = true;

				if (!valid) {
					System.out.println("Invalid username, enter again:");

				}
				username = in.next();
				valid = verifyCreds(username);
			} while (!valid);

			System.out.println("Enter your password: ");

			do {
				if (!valid) {
					System.out.println("Invalid password, enter again:");
				}
				password = in.next();
				valid = verifyCreds(password);
			} while (!valid);

			valid = true; // TEMP -> userAuth(username, password);
		} while (!valid);

		System.out.println("Signed in as user " + username);

		boolean exit = false;

		String destUser;

		while (!exit) {

			System.out.println("Please specify destination username: (i.e. john)");
			destUser = in.next();
			in.nextLine(); // flush input

			System.out.println("Please enter a message or type :exit to close:");
			message = in.nextLine();
			if (message.equals(":exit")) {
				exit = true;
				System.out.println("Exiting!");
			} else {
				System.out.println("Message to send: " + message); // Echos message to be sent

				// TESTING
				
				// TESTING

				chatEngine(endAddress, PORT, message, username, destUser);
			}

		}

		in.close();

	}
}
