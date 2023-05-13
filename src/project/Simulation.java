package project;

import project.sender.Sender;

public class Simulation {
    
    public static void main(String[] args) {
        Sender sender = new Sender();
        sender.generateSenderKeys();
    }
}
