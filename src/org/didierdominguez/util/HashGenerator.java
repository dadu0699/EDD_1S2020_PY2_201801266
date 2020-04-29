package org.didierdominguez.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class HashGenerator {
    private String secretKey;

    public HashGenerator() {
        secretKey = "EDD_1S2020_PY2";
    }

    public String encode(String password) {
        String encryptedPassword = "";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] keyPassword = md5.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            byte[] bytesKey = Arrays.copyOf(keyPassword, 24);
            SecretKey key = new SecretKeySpec(bytesKey, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = password.getBytes(StandardCharsets.UTF_8);
            byte[] buff = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buff);
            encryptedPassword = new String(base64Bytes);
        } catch (Exception exception) {
            System.out.println(exception);
        }

        return encryptedPassword;
    }

    public String decode(String password) {
        String decryptedPassword = "";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md5.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            byte[] bytesKey = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(bytesKey, "DESede");
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] message = Base64.decodeBase64(password.getBytes(StandardCharsets.UTF_8));
            byte[] plainText = decipher.doFinal(message);
            decryptedPassword = new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            System.out.println(exception);
        }

        return decryptedPassword;
    }
}
