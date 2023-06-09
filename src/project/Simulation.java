package project;

import java.io.IOException;
import java.security.KeyPair;

import project.receiver.Receiver;
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

        // --- REQUIREMENT 1 ---
        // Generate each party's RSA key pairs and give to each party accordingly
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
        // --- END REQUIREMENT 1 ---

        // --- REQUIREMENTS 2, 3, AND 4 ---
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
        // --- END REQUIREMENTS 2, 3, AND 4 ---

        // --- REQUIREMENT 5 ---
        // Read transmitted data (MAC verification, RSA decryption, and AES decryption handled
        // inside called method)
        try {
            if(receiver.readTransmittedMessage()) {
                // The transmitted data was successfully retrieved by the receiver, so display
                // the decrypted message
                receiver.displayReceivedMessage();
            }
            else {
                throw new Exception("Receiver method to read transmitted data returned false");
            }
        } catch(Exception e) {
            System.out.println("Error receiving transmitted message: " + e.toString());
            return;
        }
        // --- END REQUIREMENT 5 ---
    }
}
