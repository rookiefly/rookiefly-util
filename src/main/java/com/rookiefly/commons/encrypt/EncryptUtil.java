package com.rookiefly.commons.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * 加密工具类
 *
 * @author rookiefly
 */
public class EncryptUtil {

    private static final int ENCRYPT_BYTE_LEN = 100;

    private static final String DEFAULT_ENCODING = "utf-8";

    public static final String AES_KEY = "r4vfa6d7573347e3ab3r3s73hg0fe771d";

    public static List<String> rsaEncryptByPublicKey(String src, String keyPath) throws Exception {
        String pubKey = FileUtils.readFileToString(new File(keyPath), DEFAULT_ENCODING);

        byte[] pubByte = Hex.decodeHex(pubKey.trim().toCharArray());

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        List<String> resultList = new ArrayList<String>();
        byte[] srcByte = src.getBytes(DEFAULT_ENCODING);

        int count = (srcByte.length % ENCRYPT_BYTE_LEN == 0) ? (srcByte.length / ENCRYPT_BYTE_LEN) : (srcByte.length / ENCRYPT_BYTE_LEN + 1);

        for (int i = 0; i < count; i++) {
            byte[] pBytes = new byte[ENCRYPT_BYTE_LEN];

            if (i == (count - 1)) {
                System.arraycopy(srcByte, i * ENCRYPT_BYTE_LEN, pBytes, 0, (srcByte.length - i * ENCRYPT_BYTE_LEN));
            } else {
                System.arraycopy(srcByte, i * ENCRYPT_BYTE_LEN, pBytes, 0, ENCRYPT_BYTE_LEN);
            }

            byte[] cBytes = cipher.doFinal(pBytes);
            resultList.add(base64Encode(cBytes));
        }

        return resultList;
    }

    public static String rsaDecryptByPrivateKey(List<String> cipherTexts, String encoding, String keyPath) throws Exception {
        if ((cipherTexts == null) || (cipherTexts.size() == 0)) {
            return null;
        }

        String priv = FileUtils.readFileToString(new File(keyPath), DEFAULT_ENCODING);
        byte[] priByte = Hex.decodeHex(priv.trim().toCharArray());
        PKCS8EncodedKeySpec nkeySpec = new PKCS8EncodedKeySpec(priByte);

        KeyFactory fac = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA");

        PrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(nkeySpec);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        List<byte[]> list = new ArrayList<byte[]>();
        int total = 0;
        for (String s : cipherTexts) {
            byte[] pBytes = cipher.doFinal(base64Decode(s));
            list.add(pBytes);
            total += pBytes.length;
        }

        byte[] rBytes = new byte[total];

        int pos = 0;
        for (byte[] bs : list) {
            System.arraycopy(bs, 0, rBytes, pos, bs.length);
            pos += bs.length;
        }

        return new String(rBytes, DEFAULT_ENCODING).trim();
    }

    public static String encryptForMD5(String text)
            throws NoSuchAlgorithmException {
        if (StringUtils.isBlank(text))
            return "";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5bytes = md5.digest(text.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < md5bytes.length; i++) {
            String temp = Integer.toHexString(md5bytes[i] & 0x000000ff);
            if (temp.length() < 2)
                sb.append("0");
            sb.append(temp);
        }
        return sb.toString();

    }

    private static String bytesToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        String stmp = null;
        for (int n = 0; n < byteArray.length; n++) {
            stmp = (java.lang.Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1)
                sb.append("0").append(stmp);
            else
                sb.append(stmp);
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] stringToBytes(String text) throws Exception {
        byte[] b = text.getBytes();
        if (0 != b.length % 2)
            throw new Exception();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static String encryptForAES(String content, String key)
            throws Exception {
        byte[] bytesContent = content.getBytes("UTF-8");
        byte[] keyBytes = stringToBytes(key);
        return encryptForAES(bytesContent, keyBytes);
    }

    public static String encryptForAES(byte[] bytesContent, byte[] key)
            throws Exception {
        Cipher cp = Cipher.getInstance("AES");
        SecretKey encryptKey = new SecretKeySpec(key, "AES");
        cp.init(Cipher.ENCRYPT_MODE, encryptKey);
        byte[] result = cp.doFinal(bytesContent);
        return bytesToHexString(result);
    }

    public static byte[] encryptByteForAES(byte[] bytesContent, byte[] key) throws Exception {
        Cipher cp = Cipher.getInstance("AES");
        SecretKey encryptKey = new SecretKeySpec(key, "AES");
        cp.init(Cipher.ENCRYPT_MODE, encryptKey);
        byte[] result = cp.doFinal(bytesContent);
        return result;
    }

    public static String decryptForAES(String encryptContent, String key)
            throws Exception {

        if (StringUtils.isBlank(encryptContent)) {
            return StringUtils.EMPTY;
        }

        byte[] bytes = stringToBytes(encryptContent);
        byte[] keyBytes = stringToBytes(key);
        return decryptForAES(bytes, keyBytes);
    }

    private static String decryptForAES(byte[] content, byte[] key)
            throws Exception {
        String result = null;
        try {
            Cipher cp = Cipher.getInstance("AES");
            SecretKey decryptKey = new SecretKeySpec(key, "AES");
            cp.init(Cipher.DECRYPT_MODE, decryptKey);
            byte[] cipherByte = cp.doFinal(content);
            result = new String(cipherByte, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public static String base64Encode(byte[] binaryData) {
        return binaryData != null && binaryData.length != 0 ? Base64.encodeBase64String(binaryData) : null;
    }

    public static byte[] base64Decode(String base64String) {
        return base64String != null && base64String.length() != 0 ? Base64.decodeBase64(base64String) : null;
    }
}
