import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Sender {

    private static SecretKeySpec secretKey;
    private static IvParameterSpec ivSpec;

    private static void setIv(final String myIv) {
        try {
            ivSpec = new IvParameterSpec(myIv.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    private static void setKey(final String myKey) {
        MessageDigest sha = null;
        byte[] key = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(final String strToEncrypt, final String secret, final String iv) {
        try {
            setKey(secret);
            setIv(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return Base64.getEncoder()
                .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }

        return null;
    }

    public static String decrypt(final String strToDecrypt, final String secret, final String iv) {
        try {
            setKey(secret);
            setIv(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return new String(cipher.doFinal(Base64.getDecoder()
                .decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }

        return null;
    }

    public static void main(String[] args) {
        String plaintext = "";

        try {
            plaintext = new String(Files.readAllBytes(Paths.get("input.txt")), "UTF-8");
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        final String secretKey = "ssshhhhhhhhhhh!!!!";
        final String iv = "1234567890123456";

        String encryptedString = encrypt(plaintext, secretKey, iv) ;
        String decryptedString = decrypt(encryptedString, secretKey, iv) ;

        System.out.println(plaintext);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }
}