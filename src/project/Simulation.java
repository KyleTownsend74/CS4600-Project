package project;

import project.receiver.Receiver;
import project.sender.Sender;

public class Simulation {
    
    public static void main(String[] args) {
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        sender.generateSenderKeys();
        receiver.generateReceiverKeys();
    }
}
