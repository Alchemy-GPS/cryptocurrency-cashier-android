package com.achpay.wallet.utils;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AES {
    //AESCrypt-ObjC uses CBC and PKCS7Padding
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";

    private static final String iv = "0000000000000000";

    private AES() {
    }

    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    private static SecretKeySpec generateKey(final String password) {
        SecretKeySpec secretKeySpec = null;
        try {
            //处理秘钥长度
            int len = password.getBytes("UTF-8").length; // length of the keyprovided
            byte[] key = new byte[32]; //256 bit key space
            if (len > key.length) len = key.length;
            System.arraycopy(password.getBytes("UTF-8"), 0, key, 0, len);

            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return secretKeySpec;
    }

    /**
     * Encrypt and encode message using 256-bit AES with key generated from defaultpassword.
     *
     * @param message  the thing you want to encrypt assumed String UTF-8
     * @return Base64 encoded CipherText
     */
    public static String encrypt(String message) {
        return encrypt(NativeMethods.getPWD(), message);
    }


    /**
     * Encrypt and encode message using 256-bit AES with key generated from password.
     *
     * @param password used to generated key
     * @param message  the thing you want to encrypt assumed String UTF-8
     * @return Base64 encoded CipherText
     */
    public static String encrypt(final String password, String message) {
        String encoded = null;
        try {
            final SecretKeySpec key = generateKey(password);

            //处理秘钥偏移量
            int ivlen = iv.getBytes("UTF-8").length;
            byte[] destIV = new byte[16]; //128 bit IV
            if(ivlen > destIV.length) ivlen = destIV.length;
            System.arraycopy(iv.getBytes("UTF-8"), 0, destIV, 0, ivlen);

            byte[] cipherText = encrypt(key, destIV, message.getBytes(CHARSET));

            encoded = Base64.encodeToString(cipherText, Base64.DEFAULT);

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encoded;
    }


    /**
     * More flexible AES encrypt that doesn't encode
     *
     * @param key     AES key typically 128, 192 or 256 bit
     * @param iv      Initiation Vector
     * @param message in bytes (assumed it's already been decoded)
     * @return Encrypted cipher text (not encoded)
     */
    public static byte[] encrypt(final SecretKeySpec key, final byte[] iv, final byte[] message){
        byte[] cipherText = null;

        try {
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            cipherText = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from defaultpassword
     *
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     */
    public static String decrypt(String base64EncodedCipherText){
        return decrypt(NativeMethods.getPWD(), base64EncodedCipherText);
    }

    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from password
     *
     * @param password                used to generated key
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     */
    public static String decrypt(final String password, String base64EncodedCipherText){
        String message = null;
        try {
            final SecretKeySpec key = generateKey(password);

            byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.DEFAULT);

            //处理秘钥偏移量
            int ivlen = iv.getBytes("UTF-8").length;
            byte[] destIV = new byte[16]; //128 bit IV
            if(ivlen > destIV.length) ivlen = destIV.length;
            System.arraycopy(iv.getBytes("UTF-8"), 0, destIV, 0, ivlen);

            byte[] decryptedBytes = decrypt(key, destIV, decodedCipherText);

            message = new String(decryptedBytes, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return message;
    }


    /**
     * More flexible AES decrypt that doesn't encode
     *
     * @param key               AES key typically 128, 192 or 256 bit
     * @param iv                Initiation Vector
     * @param decodedCipherText in bytes (assumed it's already been decoded)
     * @return Decrypted message cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    public static byte[] decrypt(final SecretKeySpec key, final byte[] iv, final byte[] decodedCipherText)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal(decodedCipherText);
    }

    /**
     * Converts byte array to hexidecimal useful for logging and fault finding
     *
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
