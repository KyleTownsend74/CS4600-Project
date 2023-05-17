package project;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import project.receiver.Receiver;
import project.security.AES;
import project.security.RSA;
import project.sender.Sender;

public class Simulation {
    
    // Constants to identify where the end of the message or key is in the transmitted data file
    public static final String END_MESSAGE = "---END MESSAGE---";
    public static final String END_KEY = "---END KEY---";

    public static void main(String[] args) {
        // Create instances for the two communicating parties
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        // Generate each party's RSA key pairs
        try {
            KeyPair senderPair = RSA.generateKeyPair();
            receiver.setSenderPublic(senderPair.getPublic());
            sender.setSenderPrivate(senderPair.getPrivate());
            KeyPair receiverPair = RSA.generateKeyPair();
            sender.setReceiverPublic(receiverPair.getPublic());
            receiver.setReceiverPrivate(receiverPair.getPrivate());
        } catch(Exception e) {
            System.out.println("Error generating RSA key pairs: " + e.toString());
            return;
        }

        // Get the plaintext message from the sender (located in the "sender" folder)
        String msg = "";
        try {
            msg = sender.getMessage();
        } catch(IOException e) {
            System.out.println("Error reading sender's plaintext message: " + e.toString());
            return;
        }

        // Send data (AES encryption, RSA encryption, and MAC handled inside called method)
        try {
            sender.sendMessage(msg);
        } catch(Exception e) {
            System.out.println("Error sending data: " + e.toString());
            return;
        }

        try {
            System.out.println(receiver.readTransmittedMessage());
            // receiver.readTransmittedMessage(receiverPrivate);
        } catch(Exception e) {
            System.out.println("Error receiving transmitted message: " + e.toString());
            return;
        }
    }
}
