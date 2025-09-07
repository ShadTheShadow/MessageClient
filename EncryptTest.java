/*
 * This file is part of [MessageClient]
 * Authors: Maxwell Weston and Evan Williams
 * 
 * NOTE - This is a test class!
 */
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Scanner;

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

            byte[] encryptedPasW = Encrypt.encryptPass(passW, pair.getPublic());
            System.out.println(new String(encryptedPasW, "UTF8"));


            //use key pair to decrypt password
            System.out.println("Decrypted password: " + new String(Encrypt.decryptPass(encryptedPasW, pair.getPrivate()), "UTF8"));
        } catch (Exception e) {
            System.out.println(e);

        }

        in.close();
    }
    
}
