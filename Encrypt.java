import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import javax.crypto.Cipher;

public class Encrypt {

    /**
     * Encrypts data in the form of strings to encrypted byte arrays
     * 
     * @param plainStr String to encrypt with Public Key
     * @param publickey Public Key to encrypt with
     * @return byte array containing encrypted string
     */
    public static byte[] encryptPass(String plainStr, PublicKey publickey) {

		try {

            //Create and initialize cypher with public key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publickey);

            //add password to cypher
            byte[] input = plainStr.getBytes();
            cipher.update(input);

            //encrypt password
            byte[] encryptedPassword = cipher.doFinal();

            return encryptedPassword;

		} catch (Exception e) {
			System.out.println(e);
            return null;
		}

	}

    /**
     * Decrypts an encrypted byte array with a private key
     * 
     * @param encryptedStr Byte array of the encrypted string
     * @param privateKey Private key to decrypt with
     * @return Byte array of decrypted string
     */
    public static byte[] decryptPass(byte[] encryptedStr, PrivateKey privateKey){
        try{

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decypherPass = cipher.doFinal(encryptedStr);

            return decypherPass;

        } catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    /**
     * Main function, used for testing of the methods.
     * 
     * @param args unused
     */
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

            byte[] encryptedPasW = encryptPass(passW, pair.getPublic());
            System.out.println(new String(encryptedPasW, "UTF8"));


            //use key pair to decrypt password
            System.out.println("Decrypted password: " + new String(decryptPass(encryptedPasW, pair.getPrivate()), "UTF8"));
        } catch (Exception e) {
            System.out.println(e);

        }

        in.close();

    }
}
