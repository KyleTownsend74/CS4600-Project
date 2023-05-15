package project;

import java.io.IOException;

import project.receiver.Receiver;
import project.sender.Sender;

public class Simulation {
    
    public static void main(String[] args) {
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        sender.generateSenderKeys();
        receiver.generateReceiverKeys();

        String msg = "";
        try {
            msg = sender.getMessage();
        } catch(IOException e) {
            System.out.println("Error reading sender's plaintext message: " + e.toString());
            return;
        }
        System.out.println(msg);
    }
}
