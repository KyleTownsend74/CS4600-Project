package project.receiver;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import project.security.RSA;

public class Receiver {
    
    public void generateReceiverKeys() {
        try {
            String publicKeyPath = "sender/receiver_public.key";
            String privateKeyPath = "receiver/receiver_private.key";
            RSA.writeKeyPair(publicKeyPath, privateKeyPath);   
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Error writing RSA key pair: " + e.toString());
        }
    }
}
