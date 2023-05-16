package project;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import project.receiver.Receiver;
import project.security.AES;
import project.security.RSA;
import project.sender.Sender;

public class Simulation {
    
    public static void main(String[] args) {
        // Create instances for the two communicating parties
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        // Generate each party's RSA key pairs, and store them in the correct folders
        // Assumes sender has access to "sender" folder, and receiver has access to 
        // "receiver" folder in project's root directory
        try {
            sender.generateSenderKeys();
            receiver.generateReceiverKeys();
        } catch(Exception e) {
            System.out.println("Error writing RSA key pair: " + e.toString());
        }

        // Get the plaintext message from the sender (located in the "sender" folder)
        String msg = "";
        try {
            msg = sender.getMessage();
        } catch(IOException e) {
            System.out.println("Error reading sender's plaintext message: " + e.toString());
            return;
        }
        // System.out.println(msg);

        PublicKey publicKey;
        PrivateKey privateKey;

        try {
            publicKey = RSA.readPublicKey("sender/receiver_public.key");
            String encryptedMsg = RSA.encrypt(msg, publicKey);

            // privateKey = RSA.readPrivateKey("sender/receiver_public.key");
            // String decryptedMsg = RSA.decrypt(msg, privateKey);

            System.out.println(msg);
            System.out.println(encryptedMsg);
            // System.out.println(decryptedMsg);
        } catch(Exception e) {
            System.out.println("Error reading key: " + e.toString());
            return;
        }

        // // Encrypt the sender's message using AES
        // String secretKey = "ssshhhhhhhhhhh!!!!";
        // String iv = "1234567890123456";
        // String encryptedMsg;

        // try {
        //     encryptedMsg = AES.encrypt(msg, secretKey, iv);
        // } catch(Exception e) {
        //     System.out.println("Error encrypting with AES: " + e.toString());
        //     return;
        // }
    }
}
