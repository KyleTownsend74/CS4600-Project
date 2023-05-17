package project.receiver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import project.Simulation;
import project.security.AES;
import project.security.MAC;
import project.security.RSA;
import project.sender.Sender;

public class Receiver {
    
    private PublicKey senderPublic = null;
    private PrivateKey receiverPrivate = null;

    public void setSenderPublic(PublicKey publicKey) {
        senderPublic = publicKey;
    }

    public void setReceiverPrivate(PrivateKey privateKey) {
        receiverPrivate = privateKey;
    }

    public PublicKey getSenderPublic() {
        return senderPublic;
    }

    public PrivateKey getReceiverPrivate() {
        return receiverPrivate;
    }

    public void generateReceiverKeys() throws NoSuchAlgorithmException, IOException {
        String publicKeyPath = "sender/receiver_public.key";
        String privateKeyPath = "receiver/receiver_private.key";
        RSA.writeKeyPair(publicKeyPath, privateKeyPath);
    }

    public String readTransmittedMessage() throws
            UnsupportedEncodingException, IOException, NoSuchAlgorithmException, 
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {
        byte[] dataBytes = Files.readAllBytes(Paths.get("receiver/Transmitted_Data"));
        String data = new String(dataBytes, "UTF-8");
        int msgEndIndex = data.indexOf(Simulation.END_MESSAGE);
        String msg = data.substring(0, msgEndIndex);
        int keyEndIndex = data.indexOf(Simulation.END_KEY);
        String aesKey = data.substring(msgEndIndex + Simulation.END_MESSAGE.length(), keyEndIndex);
        String receivedMac = data.substring(keyEndIndex + Simulation.END_KEY.length(), data.length());
        byte[] mac = MAC.computeMac(data.substring(0, data.indexOf(receivedMac)));
        byte[] receivedMacBytes = Arrays.copyOfRange(dataBytes, 
            keyEndIndex + Simulation.END_KEY.length(), dataBytes.length);

        // MAC authentication failed, return null to represent failure
        if(!Arrays.equals(mac, receivedMacBytes)) {
            return null;
        }

        // MAC authentication successful, get AES key and decrypt message
        String decryptedAesKey = RSA.decrypt(aesKey, receiverPrivate);
        String decryptedMsg = AES.decrypt(msg, decryptedAesKey, Sender.AES_IV);

        return decryptedMsg;
    }
}
