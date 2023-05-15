package project.sender;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import project.security.AES;
import project.security.MAC;
import project.security.RSA;

public class Sender {

    public void generateSenderKeys() {
        try {
            String publicKeyPath = "receiver/sender_public.key";
            String privateKeyPath = "sender/sender_private.key";
            RSA.writeKeyPair(publicKeyPath, privateKeyPath);   
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Error writing RSA key pair: " + e.toString());
        }
    }

    public String getMessage() throws UnsupportedEncodingException, IOException {
        return new String(Files.readAllBytes(Paths.get("sender/Message.txt")), "UTF-8");
    }

    public static void main(String[] args) {
        // try {
        //     byte[] result1 = MAC.computeMac("Hello world!");
        //     System.out.println("Mac result:");
        //     System.out.println(new String(result1));

        //     byte[] result2 = MAC.computeMac("Hello world!");
        //     System.out.println("Mac result:");
        //     System.out.println(new String(result2));

        //     System.out.println(Arrays.equals(result1, result2));
        // } catch(Exception e) {
        //     System.out.println("Error computing MAC: " + e.toString());
        // }

        // String plaintext = "";

        // try {
        //     plaintext = new String(Files.readAllBytes(Paths.get("input.txt")), "UTF-8");
        // } catch (Exception e) {
        //     System.out.println("Error reading plaintext file: " + e.toString());
        // }

        // String secretKey = "ssshhhhhhhhhhh!!!!";
        // String iv = "1234567890123456";

        // try {
        //     String encryptedString = AES.encrypt(plaintext, secretKey, iv) ;
        //     String decryptedString = AES.decrypt(encryptedString, secretKey, iv);
    
        //     System.out.println(plaintext);
        //     System.out.println(encryptedString);
        //     System.out.println(decryptedString);
        // } catch(Exception e) {
        //     System.out.println("Error encrypting with AES: " + e.toString());
        // }

        // try {
        //     String publicKeyPath = "receiver/sender_public.key";
        //     String privateKeyPath = "sender/sender_private.key";
        //     RSA.writeKeyPair(publicKeyPath, privateKeyPath);   
        // } catch (NoSuchAlgorithmException | IOException e) {
        //     System.out.println("Error writing RSA key pair: " + e.toString());
        // }
    }
}