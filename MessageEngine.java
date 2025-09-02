import java.net.Socket; // for client side interactions
import java.util.concurrent.ConcurrentHashMap;
import java.net.ServerSocket; // for server side interactions
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.io.FileWriter;

public class MessageEngine {

	private static int port = 55935;

	private static ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

	public static void main(String[] args) {


		try (ServerSocket serverSocket = new ServerSocket(port);) {

			System.out.println("Server is listening on port " + serverSocket.getLocalPort());

			serverSocket.setReuseAddress(true);

			while (true) {

				Socket clientSocket = serverSocket.accept();
				InetAddress clientAddress = clientSocket.getInetAddress();


				String clientIP = clientAddress.getHostAddress();

				System.out.println("Client connected from " + clientIP);

				new Thread(new ClientHandler(clientSocket)).start();

			}
		}catch(Exception e){
			System.out.println(e);
		}
	}



	public static class ClientHandler implements Runnable {
		private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;


		public ClientHandler(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			try{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);



				out.println("Enter username: ");

				username = in.readLine();

				clients.put(username, out);

				out.println("Welcome " + username + "! Type username|message to text somebody!");

				
				while(socket.isConnected()){
					String message = in.readLine();

					String[] split = message.split("|");

					PrintWriter destOut = clients.get(split[0]);

					destOut.println(message);
				}



			}catch(Exception e){
				System.out.println(e);
;			}
		}
	}

}