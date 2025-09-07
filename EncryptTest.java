/*
 * This file is part of [MessageClient]
 * Authors: Maxwell Weston and Evan Williams
 * 
 * NOTE - This is a test class!
 */
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Scanner;
import java.nio.ByteBuffer;

public class EncryptTest {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String passW;

        System.out.println("Enter password: ");
        passW = in.next();


        System.out.println("Pre-encrypt: " + passW);

        
        try{
            //Gen key pair and encrypt password
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
			KeyPair pair = keyGen.generateKeyPair();

            byte[] toSend = new String("LOGIN|" + "max|").getBytes();
            byte[] pubEncodedKey = pair.getPublic().getEncoded();
            byte[] keyLength = ByteBuffer.allocate(4).putInt(pubEncodedKey.length).array();

            byte[] fullLogin = new byte[toSend.length + pubEncodedKey.length + keyLength.length];

            int logLength = 0;

            for(int i = 0; i < fullLogin.length ; i++){
                
                if(i < toSend.length){
                    fullLogin[i] = toSend[i];
                } else if (i < keyLength.length + toSend.length) {
                    fullLogin[i] = keyLength[i - toSend.length];
                    logLength += fullLogin[i];
                } else if (i < fullLogin.length + 1){
                    fullLogin[i] = pubEncodedKey[i - toSend.length - keyLength.length];
                } else {
                    System.out.println("you did something wrong");
                }
                
            }

            for(int j = 0; j < fullLogin.length ; j++){
                System.out.println(fullLogin[j]);
            }

            System.out.println("Encoded key length: " + logLength);
            System.out.println("Encoded key  real length: " + pubEncodedKey.length);

            System.out.println("Public Key: " + pair.getPublic().getEncoded());

            byte[] encryptedPasW = Encrypt.encryptPass(passW, pair.getPublic());
            System.out.println(new String(encryptedPasW, "UTF8"));


            //use key pair to decrypt password
            System.out.println("Decrypted password: " + new String(Encrypt.decryptPass(encryptedPasW, pair.getPrivate()), "UTF8"));

            System.out.println("Pub key pre encode: " + pair.getPublic());

            System.out.println("Pub key post encode: " + Encrypt.decodeLogin(pubEncodedKey));
            
        } catch (Exception e) {
            System.out.println(e);

        }

        in.close();
    }
    
}
