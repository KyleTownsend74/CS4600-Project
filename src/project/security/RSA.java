package project.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // Generate keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    private static void writeKey(Key keyToWrite, String path) throws IOException {
        // Write key to file
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(keyToWrite.getEncoded());
        fos.close();
    }

    public static void writeKeyPair(String publicKeyPath, String privateKeyPath) throws NoSuchAlgorithmException, IOException {
        // Generate keys
        KeyPair pair = generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        writeKey(publicKey, publicKeyPath);
        writeKey(privateKey, privateKeyPath);
    }

    private static EncodedKeySpec readKey(String pathToKey) throws IOException {
        // Read key from file and create key instance
        File keyFile = new File(pathToKey);
        byte[] keyBytes = Files.readAllBytes(keyFile.toPath());

        return new X509EncodedKeySpec(keyBytes);
    }

    public static PublicKey readPublicKey(String pathToKey) throws 
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        EncodedKeySpec publicKeySpec = readKey(pathToKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(publicKeySpec);
    }

    public static PrivateKey readPrivateKey(String pathToKey) throws
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File keyFile = new File(pathToKey);
        byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(privateKeySpec);
    }

    public static String encrypt(String strToEncrypt, PublicKey publicKey) throws 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return Base64.getEncoder()
            .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    public static String decrypt(String strToDecrypt, PrivateKey privateKey) throws
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(Base64.getDecoder()
            .decode(strToDecrypt)));
    }
}
