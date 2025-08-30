import java.net.Socket; // for client side interactions
import java.net.SocketAddress;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * TODO write header
 */
public class ClientEngine {

	/**
	 * TODO write header
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

	/**
	 * TODO write header
	 */
	public static void main(String[] args) {
		String localAddress;
		String endAddress = "kronkserver.tplinkdns.com";
		final int PORT = 55935;
		try {
			endAddress = InetAddress.getByName(endAddress).getHostAddress().toString();
			localAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Local Address: " + localAddress);
		} catch (Exception ex) {
			System.out.println("Unable to determine address!");
			return;
		}
		System.out.println("End address: " + endAddress); // Test to see end add
		Socket server = connectSocket(endAddress, PORT);

		if (server == null) {
			System.out.println("No connection! Could the port be blocked?");
			return;
		}

		try {
			PrintWriter output = new PrintWriter(server.getOutputStream(), true);

			output.println("balls");

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
