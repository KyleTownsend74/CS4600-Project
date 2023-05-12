package project.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // Generate keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    private static void writeKey(Key keyToWrite, String path) throws IOException {
        // Write key to file
        FileOutputStream fos = new FileOutputStream(path);
        // System.out.println(publicKey);
        fos.write(keyToWrite.getEncoded());
        fos.close();
    }

    public static void writeKeyPair(String publicKeyPath, String privateKeyPath) throws NoSuchAlgorithmException, IOException {
        // Generate keys
        KeyPair pair = generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        System.out.println(privateKey);
        System.out.println(publicKey);

        writeKey(publicKey, publicKeyPath);
        writeKey(privateKey, privateKeyPath);
    }

    public static PublicKey readKey(String pathToKey) throws 
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Read key from file and create key instance
        File publicKeyFile = new File(pathToKey);
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey newPublicKey = keyFactory.generatePublic(publicKeySpec);
        
        return newPublicKey;
    }
}
