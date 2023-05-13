package project.sender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import project.security.AES;
import project.security.RSA;

public class Sender {

    public static void main(String[] args) {
        String plaintext = "";

        try {
            plaintext = new String(Files.readAllBytes(Paths.get("input.txt")), "UTF-8");
        } catch (Exception e) {
            System.out.println("Error reading plaintext file: " + e.toString());
        }

        String secretKey = "ssshhhhhhhhhhh!!!!";
        String iv = "1234567890123456";

        try {
            String encryptedString = AES.encrypt(plaintext, secretKey, iv) ;
            String decryptedString = AES.decrypt(encryptedString, secretKey, iv);
    
            System.out.println(plaintext);
            System.out.println(encryptedString);
            System.out.println(decryptedString);
        } catch(Exception e) {
            System.out.println("Error encrypting with AES: " + e.toString());
        }

        try {
            String publicKeyPath = "receiver/sender_public.key";
            String privateKeyPath = "sender/sender_private.key";
            RSA.writeKeyPair(publicKeyPath, privateKeyPath);   
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Error writing RSA key pair: " + e.toString());
        }
    }
}