package project.sender;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import project.security.AES;
import project.security.MAC;
import project.security.RSA;

public class Sender {

    private PublicKey receiverPublic = null;
    private PrivateKey senderPrivate = null;

    public void setReceiverPublic(PublicKey publicKey) {
        receiverPublic = publicKey;
    }

    public void setSenderPrivate(PrivateKey privateKey) {
        senderPrivate = privateKey;
    }

    public PublicKey getReceiverPublic() {
        return receiverPublic;
    }

    public PrivateKey getSenderPrivate() {
        return senderPrivate;
    }

    public void generateSenderKeys() throws NoSuchAlgorithmException, IOException {        
        String publicKeyPath = "receiver/sender_public.key";
        String privateKeyPath = "sender/sender_private.key";
        RSA.writeKeyPair(publicKeyPath, privateKeyPath);   
    }

    public String getMessage() throws UnsupportedEncodingException, IOException {
        return new String(Files.readAllBytes(Paths.get("sender/Message.txt")), "UTF-8");
    }

    public void sendMessage(String msg) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        FileOutputStream fos = new FileOutputStream("receiver/Transmitted_Data");
        byte[] mac = MAC.computeMac(msg);
        byte[] msgBytes = msg.getBytes("UTF-8");
        byte[] both = Arrays.copyOf(msgBytes, msgBytes.length + mac.length);
        System.arraycopy(mac, 0, both, msgBytes.length, mac.length);
        fos.write(both);
        fos.close();
    }
}