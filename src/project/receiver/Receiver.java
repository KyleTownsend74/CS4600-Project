package project.receiver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import project.Simulation;
import project.security.RSA;

public class Receiver {
    
    public void generateReceiverKeys() throws NoSuchAlgorithmException, IOException {
        String publicKeyPath = "sender/receiver_public.key";
        String privateKeyPath = "receiver/receiver_private.key";
        RSA.writeKeyPair(publicKeyPath, privateKeyPath);
    }

    public String readTransmittedMessage() throws UnsupportedEncodingException, IOException {
        String data = new String(Files.readAllBytes(Paths.get("receiver/Transmitted_Data.txt")), "UTF-8");
        int msgEndIndex = data.indexOf(Simulation.END_MESSAGE);
        String msg = data.substring(0, msgEndIndex);
        int keyEndIndex = data.indexOf(Simulation.END_KEY);
        String aesKey = data.substring(msgEndIndex + Simulation.END_MESSAGE.length(), keyEndIndex);

        return aesKey;
    }
}
