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
    
    public static final String AES_IV = "1234567890123456";
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

        // Encrypt the sender's message using AES
        String aesKey = "ssshhhhhhhhhhh!!!!";
        String aesIv = AES_IV;
        String encryptedMsg;

        try {
            encryptedMsg = AES.encrypt(msg, aesKey, aesIv);
        } catch(Exception e) {
            System.out.println("Error encrypting with AES: " + e.toString());
            return;
        }

        // Encrypt the AES key with receiver's RSA public key
        String encryptedKey;

        try {
            encryptedKey = RSA.encrypt(aesKey, sender.getReceiverPublic());
        } catch(Exception e) {
            System.out.println("Error reading key: " + e.toString());
            return;
        }

        // Append encrypted AES key to encrypted message
        String dataToSend = encryptedMsg + END_MESSAGE
                            + encryptedKey + END_KEY;

        // Send data (with MAC appended in the called method)
        try {
            sender.sendMessage(dataToSend);
        } catch(Exception e) {
            System.out.println("Error sending data: " + e.toString());
            return;
        }

        try {
            System.out.println(receiver.readTransmittedMessage(receiver.getReceiverPrivate()));
            // receiver.readTransmittedMessage(receiverPrivate);
        } catch(Exception e) {
            System.out.println("Error receiving transmitted message: " + e.toString());
            return;
        }
    }
}
