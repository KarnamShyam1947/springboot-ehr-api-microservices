package com.ehr.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Patient implements Serializable {
    int id;
    String name;

    public Patient(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
}

public class BlockchainHashUtils {

    public static String hashObject(Object obj, String algorithm) throws NoSuchAlgorithmException, IOException {
        
        byte[] objectBytes = convertObjectToBytes(obj);

        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = messageDigest.digest(objectBytes);
        
        return bytesToHex(hashBytes);
    }

    
    public static boolean verifyDataIntegrity(String algorithm, Object obj, String expectedHash) {
        try {
            String generatedHash = hashObject(obj, algorithm);    
            return generatedHash.equals(expectedHash);
        } catch (Exception e) {    
            e.printStackTrace();
            return false;
        }
    }

    
    private static byte[] convertObjectToBytes(Object obj) throws IOException {
        
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(obj);
        }
        return byteStream.toByteArray();
    }

    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    
}
