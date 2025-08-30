import java.net.Socket; // for client side interactions
import java.net.SocketAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket; // for server side interactions

public class ClientEngine {
	
	
	
	public static void main(String[] args) {
		//System.out.print("Sending to: " + args[0] + " | Message: " + args[1] + "\n");
		String address;
		String endAddress = "192.168.0.194";
		final int PORT = 56827;
		try {
		address = InetAddress.getLocalHost().getHostAddress();
		System.out.println("Local Addy: " + address);
		} catch(Exception ex) {
			System.out.println("Unable to determine address!");
			return;
		}
		
		Socket evan = connectSocket(endAddress, PORT);
		
		try {
		PrintWriter output = new PrintWriter(evan.getOutputStream(), true);
		
		output.println("balls");
		
		
		while(evan != null) {
			output.println("balls");
		}
		
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public static Socket connectSocket(String endAddress, int port) {
		SocketAddress evanIp = new InetSocketAddress(endAddress,port);
		Socket evan = new Socket();
		try {
			System.out.println("Connecting to: " + evanIp);
		evan.connect(evanIp);
		System.out.println("Connected to " + evanIp);
		} catch (Exception ex) {
			System.out.println("Failed to connect!");
			return null;
		}
		return evan;
	}
}
