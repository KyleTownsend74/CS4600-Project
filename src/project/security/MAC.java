package project.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;

public class MAC {
    
    private static Key key = getMacKey();

    private static Key getMacKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            SecureRandom secRandom = new SecureRandom();
            keyGen.init(secRandom);
            Key key = keyGen.generateKey();
            
            return key;
        } catch(NoSuchAlgorithmException e) {
            System.out.println("Error generating MAC key: " + e.toString());
            return null;
        }
    }

    public static byte[] computeMac(String msg) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] bytes = msg.getBytes();
        
        return mac.doFinal(bytes);
    }
}
