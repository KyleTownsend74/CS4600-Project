package project.sender;

import java.io.FileOutputStream;
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

public class Sender {

    // Constant IV to use for AES encryption
    public static final String AES_IV = "1234567890123456";

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

    // Read sender's message from file (hardcoded file path at 'sender/Message.txt')
    public String getMessage() throws UnsupportedEncodingException, IOException {
        String msg = new String(Files.readAllBytes(Paths.get("sender/Message.txt")), "UTF-8");
        
        System.out.println("--- SENDER ---");
        System.out.println("Original Plaintext Message:\n");
        System.out.println(msg);
        System.out.println("\n--------------\n");

        return msg;
    }

    // Write data to file from byte array (hardcoded file path at 'receiver/Transmitted_Data')
    public void writeMessage(byte[] msgBytes) throws IOException {
        FileOutputStream fos = new FileOutputStream("receiver/Transmitted_Data");
        fos.write(msgBytes);
        fos.close();

        System.out.println("--- SENDER ---");
        System.out.println("Sent encrypted message...");
        System.out.println("--------------\n");
    }

    // Send message to receiver (using file system to simulate sending process)
    // The message is encrypted using AES. The AES key used is encrypted using RSA.
    // The encrypted key is appended to the encrypted message, and then the MAC is
    // calculated and appended to the data to be sent.
    public void sendMessage(String msg) throws
            UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, IOException {
        // Encrypt the message using AES
        String aesKey = "abcdefghABCDEFGH";
        String encryptedMsg = AES.encrypt(msg, aesKey, AES_IV);

        // Encrypt the AES key with receiver's RSA public key
        String encryptedKey = RSA.encrypt(aesKey, receiverPublic);

        // Append encrypted AES key to encrypted message
        String dataToSend = encryptedMsg + Simulation.END_MESSAGE
                            + encryptedKey + Simulation.END_KEY;

        // Compute MAC on encrypted data
        byte[] mac = MAC.computeMac(dataToSend);

        // Append MAC to data and prepare data to be written as bytes to a file
        byte[] dataBytes = dataToSend.getBytes("UTF-8");
        byte[] finalData = Arrays.copyOf(dataBytes, dataBytes.length + mac.length);
        System.arraycopy(mac, 0, finalData, dataBytes.length, mac.length);

        // Write data to file
        writeMessage(finalData);
    }
}