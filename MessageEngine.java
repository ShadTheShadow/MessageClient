import java.net.Socket; // for client side interactions
import java.net.ServerSocket; // for server side interactions

import java.net.InetAddress;

public class MessageEngine {
	
	public static void main(String[] args) {

		try {

		} catch (Exception e){
			e.printStackTrace();
		}


		int port = 56827;

		try (ServerSocket serverSocket = new ServerSocket(port);){

			System.out.println("Server is listening on port " + serverSocket.getLocalPort());

			serverSocket.setReuseAddress(true);
	
			while (true){

				Socket clientSocket = serverSocket.accept();
				InetAddress clientAddress = clientSocket.getInetAddress();
				System.out.println("Client connected from " + clientAddress.getHostAddress());

				clientSocket.close();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
