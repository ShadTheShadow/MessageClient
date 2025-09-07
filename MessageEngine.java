import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class MessageEngine {

	private static final int PORT = 55935;

	private static HashMap<String, SocketChannel> clients = new HashMap<>();
	public static HashMap<String, PublicKey> publicKeys = new HashMap<>();

	public static void main(String[] args) throws IOException{


		Selector selector = Selector.open();
		ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(PORT));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);

		System.out.println("Server is listening on " + server.getLocalAddress());

		//Main dummy pipe loop
		while(true){
			selector.select();

			//Iterates through available selectors
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {

                SelectionKey key = it.next();
                it.remove();

				//Checks the statuses of the keys (new connection / writable)
				if (key.isAcceptable()){

					SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("New connection: " + client.getRemoteAddress());

				}else if (key.isReadable()){

                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    int bytesRead = client.read(buffer);
                    if (bytesRead == -1) {
                        // client disconnected
                        clients.values().remove(client);
                        client.close();
                        continue;
					}

					
					buffer.flip();






					byte[] data = buffer.array();
					
					String msg = new String(buffer.array(), 0, buffer.limit()).trim();


					if (msg.startsWith("LOGIN|")){

						try{


							//reads 'LOGIN|username|publicKey' format

							
							String[] split = msg.split("\\|");

							String username = split[1];

							//int startIndex = split[0].length() + username.length() + 2; //+2 for pipe operators
							
							//byte[] publicKeyArray = Arrays.copyOfRange(data, startIndex, data.length);
							

							PublicKey publicKey = Encrypt.decodeLogin(data);

							System.out.println("PUBLIC KEY: " + publicKey.toString());

							clients.put(username, client);
							publicKeys.put(username, publicKey);


							System.out.println("Registered " + username);
						}


						catch (Exception e){
							System.out.println("Login process failed");
						}

					}else if (msg.startsWith("MSG|")){
						//reads 'MSG|username|message' format
						String[] split = msg.split("\\|");

						String recipient = split[1];
						String message = split[2];

						SocketChannel dest = clients.get(recipient);
						if (dest != null && dest.isOpen()){
							dest.write(ByteBuffer.wrap(message.getBytes()));
							System.out.println("Forwarded message to " + recipient);
						}
					}

				}

			}

		}
	}
}

	

