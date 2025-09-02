
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


	public static void main(String[] args) throws IOException{



		Selector selector = Selector.open();
        SocketChannel client = SocketChannel.open(new InetSocketAddress(SERVER, PORT));
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);




		Scanner scanner = new Scanner(System.in);

        // Register with username (client side)
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        client.write(ByteBuffer.wrap(("LOGIN|" + username).getBytes()));


		// Separate thread for sending
        new Thread(() -> {
            try {
                while (true) {
                    String input = scanner.nextLine();
                    client.write(ByteBuffer.wrap(input.getBytes())); //Format 'MSG|username|message'
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

}
