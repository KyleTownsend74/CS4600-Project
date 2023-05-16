package project.receiver;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import project.security.RSA;

public class Receiver {
    
    public void generateReceiverKeys() throws NoSuchAlgorithmException, IOException {
        String publicKeyPath = "sender/receiver_public.key";
        String privateKeyPath = "receiver/receiver_private.key";
        RSA.writeKeyPair(publicKeyPath, privateKeyPath);
    }
}
