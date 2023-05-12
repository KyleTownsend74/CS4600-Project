package project.sender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import project.security.RSA;

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

        // try {
        //     plaintext = new String(Files.readAllBytes(Paths.get("input.txt")), "UTF-8");
        // } catch (Exception e) {
        //     System.out.println("An error occurred.");
        //     e.printStackTrace();
        // }

        // final String secretKey = "ssshhhhhhhhhhh!!!!";
        // final String iv = "1234567890123456";

        // String encryptedString = encrypt(plaintext, secretKey, iv) ;
        // String decryptedString = decrypt(encryptedString, secretKey, iv) ;

        // System.out.println(plaintext);
        // System.out.println(encryptedString);
        // System.out.println(decryptedString);

        try {
            String publicKeyPath = "receiver/sender_public.key";
            String privateKeyPath = "sender/sender_private.key";
            RSA.writeKeyPair(publicKeyPath, privateKeyPath);   
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Error writing RSA key pair: " + e.toString());
        }
        
        // try {
        //     // Write key to file
        //     FileOutputStream publicFos = new FileOutputStream("test.txt");
        //     // System.out.println(publicKey);
        //     byte[] bytes = {66, 72, 89};
        //     publicFos.write(bytes);
        //     publicFos.close();
        //     System.out.println("Hello???");
        // } catch(IOException e) {
        //     e.printStackTrace();
        // }

        // try {
        //     // Generate keys
        //     KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        //     generator.initialize(2048);
        //     KeyPair pair = generator.generateKeyPair();
    
        //     PrivateKey privateKey = pair.getPrivate();
        //     PublicKey publicKey = pair.getPublic();

        //     // Write key to file
        //     FileOutputStream publicFos = new FileOutputStream("../receiver/sender_public.key");
        //     // System.out.println(publicKey);
        //     publicFos.write(publicKey.getEncoded());
        //     publicFos.close();

        //     // Write key to file
        //     FileOutputStream privateFos = new FileOutputStream("sender_private.key");
        //     // System.out.println(privateKey);
        //     privateFos.write(privateKey.getEncoded());
        //     privateFos.close();

        //     // Read key from file and create key instance
        //     File publicKeyFile = new File("../receiver/sender_public.key");
        //     byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        //     KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //     EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        //     PublicKey newPublicKey = keyFactory.generatePublic(publicKeySpec);
        //     System.out.println(newPublicKey);
        // } catch(NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
        //     System.out.println("Error while generating RSA Keys: " + e.toString());
        // }

        // try {
        //     String str = "Hello world!\nSome other line!";
        //     BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        //     writer.write(str);
            
        //     writer.close();
        // } catch(Exception e) {
        //     e.printStackTrace();
        // }
    }
}