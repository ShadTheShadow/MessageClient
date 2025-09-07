
/*
 * Created by Maxwell Weston and Evan Williams
 */
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;

public class ClientEngine {

	public static final String SERVER = "kronkserver.tplinkdns.com";
	public static final int PORT = 55935;

	public static void main(String[] args) throws IOException {

		System.out.println("Connecting to server...");

		Scanner scanner = new Scanner(System.in);

		Selector selector = Selector.open();

		SocketChannel client = connectServer(scanner);

		// If connection fails and user chooses to exit, exit the program
		if (client == null) {

			System.out.println("Exiting!");
			return;

		}

		scanner.nextLine(); // flush to prep for further input

		client.configureBlocking(false);
		client.register(selector, SelectionKey.OP_READ);

		// Register with username (client side)
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		client.write(ByteBuffer.wrap(("LOGIN|" + username).getBytes()));

		// Separate thread for sending
		new Thread(() -> {
			try {
				while (true) {
					String input = scanner.nextLine();
					client.write(ByteBuffer.wrap(input.getBytes())); // Format 'MSG|username|message'
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		// Main loop for receiving
		while (true) {
			selector.select();
			for (SelectionKey key : selector.selectedKeys()) {
				if (key.isReadable()) {
					SocketChannel ch = (SocketChannel) key.channel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					int read = ch.read(buffer);
					if (read > 0) {
						buffer.flip();
						System.out.println("Received: " + new String(buffer.array(), 0, buffer.limit()));
					}
				}
			}
			selector.selectedKeys().clear();
		}

	}

	/**
	 * Handles socket connection to server IP and port, allowing user control.
	 * 
	 * @param input Scanner variable specifying system.in
	 * @return connected socket of the server, or null if no connection was made.
	 */
	public static SocketChannel connectServer(Scanner input) {

		boolean connected = false;

		SocketChannel attemptChannel = null;

		while (!connected) {

			System.out.println("Connecting...");

			try {

				attemptChannel = SocketChannel.open(new InetSocketAddress(SERVER, PORT));
				connected = true;

			} catch (Exception e) {

				System.out.println("Connection failed! Retry? (y/n)");

				if (input.next().equals("n")) {

					break;

				} else {

					System.out.println("Retrying connection...");

				}

			}
		}

		return attemptChannel;
	}



	/**
	 * 
	 * @return
	 */
	public static KeyPair genAuthKeys() {

		try {
			// Declare key generator and generate key
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
         keyGen.initialize(4096);
         return keyGen.generateKeyPair();


		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

}
