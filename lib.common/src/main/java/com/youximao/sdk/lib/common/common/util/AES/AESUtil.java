package com.youximao.sdk.lib.common.common.util.AES;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * Set-up MD5 as the default hashing algorithm
     */
    private static final HashType DEFAULT_HASH_TYPE = HashType.MD5;

    /**
     * 加密
     *
     * @param value
     * @param key
     * @return
     * @throws CryptoException
     */
    public static String encryptAES(String value, String key) throws CryptoException {
        try {
            byte[] privateKey = Base64.decodeBase64(key.getBytes("utf8"));
            SecretKeySpec skeySpec = new SecretKeySpec(privateKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return Codec.byteToHexString(cipher.doFinal(value.getBytes()));
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }

    /**
     * 解密
     *
     * @param value
     * @param key
     * @return
     * @throws CryptoException
     */

    public static String decryptAES(String value, String key) throws CryptoException {
        try {
            byte[] privateKey = Base64.decodeBase64(key.getBytes("utf8"));
            SecretKeySpec skeySpec = new SecretKeySpec(privateKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return new String(cipher.doFinal(Codec.hexStringToByte(value)));
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }


    /**
     * Define a hash type enumeration for strong-typing
     */
    public enum HashType {
        MD5("MD5"),
        SHA1("SHA-1"),
        SHA256("SHA-256"),
        SHA512("SHA-512");
        private String algorithm;

        HashType(String algorithm) {
            this.algorithm = algorithm;
        }

        @Override
        public String toString() {
            return this.algorithm;
        }
    }


}