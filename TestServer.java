import java.net.InetAddress;
import java.net.ServerSocket;

public class TestServer {
	public static void main(String[] args) {
		final int port = 56827;
		ServerSocket connect = null;
		try {
			connect = new ServerSocket(port);
			System.out.println(
					"Created server at IP: " + InetAddress.getLocalHost().getHostAddress() + " | Port: " + port);
			System.out.println("Attempting Connection...");
			
			connect.accept();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
		
	}
}
