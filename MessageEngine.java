import java.net.Socket; // for client side interactions
import java.net.ServerSocket; // for server side interactions

public class MessageEngine {
	
	public static void main(String[] args) {
		System.out.print("Sending to: " + args[0] + " | Message: " + args[1] + "\n");
	}
}
