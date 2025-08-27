import java.net.Socket; // for client side interactions
import java.net.SocketAddress;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket; // for server side interactions

public class ClientEngine {
	
	
	
	public static void main(String[] args) {
		//System.out.print("Sending to: " + args[0] + " | Message: " + args[1] + "\n");
		String address;
		String endAddress = "192.168.0.194";
		final int PORT = 50100;
		try {
		address = InetAddress.getLocalHost().getHostAddress();
		System.out.println("Local Addy: " + address);
		} catch(Exception ex) {
			System.out.println("Unable to determine address!");
			return;
		}
		
		connectSocket(endAddress, PORT);
	}
	
	public static void connectSocket(String endAddress, int port) {
		SocketAddress evanIp = new InetSocketAddress(endAddress,port);
		Socket evan = new Socket();
		try {
			System.out.println("Connecting to: " + evanIp);
		evan.connect(evanIp);
		System.out.println("Connected to" + evanIp);
		} catch (Exception ex) {
			System.out.println("Failed to connect!");
		}
	}
}
