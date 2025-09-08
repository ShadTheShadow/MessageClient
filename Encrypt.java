/*
 * This file is part of [MessageClient]
 * Authors: Maxwell Weston and Evan Williams
 * 
 */
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
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

    public static byte[] encodeLogin(String username, PublicKey pubKey){
            byte[] toSend = new String("LOGIN|" + username + "|").getBytes();
            byte[] pubEncodedKey = pubKey.getEncoded();
            byte[] fullLogin = new byte[toSend.length + pubEncodedKey.length];


            for(int i = 0; i < fullLogin.length ; i++){
                
                if(i < toSend.length){
                    fullLogin[i] = toSend[i];
                } else if (i < fullLogin.length){
                    fullLogin[i] = pubEncodedKey[i - toSend.length];
                } else {
                    System.out.println("Key Encode Error!");
                }
                
            }

            return fullLogin;
    }

    public static PublicKey decodeLogin(byte[] encodedKey){
        try{
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        return keyFac.generatePublic(spec);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
