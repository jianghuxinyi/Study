package com.linanqing.passwordmanager.utils;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtils {
    private static String test = "test";
    private static String publicKeyM = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVxAu1mzwk2rLZTAVOToOrawlhBl5VDCVB5gVt\n" +
            "xOdq6SAErePopaxI38KQ1Cvx8TzT6epq/ZA6ePU8MaQUERQd7TqdJ6y/M7Ag3O0FZdhwdugBHz/G\n" +
            "O49QyyrUIbEbJ3FfC4Hehs//TYtJSzd4r/e6JtRdEWMC7LSEDpmAAx3GbwIDAQAB\n";

    private static String privateKeyM="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANXEC7WbPCTastlMBU5Og6trCWEG\n" +
            "XlUMJUHmBW3E52rpIASt4+ilrEjfwpDUK/HxPNPp6mr9kDp49TwxpBQRFB3tOp0nrL8zsCDc7QVl\n" +
            "2HB26AEfP8Y7j1DLKtQhsRsncV8Lgd6Gz/9Ni0lLN3iv97om1F0RYwLstIQOmYADHcZvAgMBAAEC\n" +
            "gYAn2VD5fMO8dwxqF5LRoy9GcECiVqaVIht76l5OtSsSsfynAqd+wguY1iWWDAdZJchaFLv/T99o\n" +
            "Fw5YN0TH/NsL1eX3gbaP7WXgwT5ez9BKdFP2LzJZ+pgIJMEUbfD97bcTQog+CyHN8LYwzmsKfiWi\n" +
            "O7wlnlOYfqiqWPAg1xz70QJBAO7JFjAsYBRIwlCGUrOlHQzFl00HImWqsmnQEV4Arnlzc+v1FysR\n" +
            "Rc/fQCVxMpxQwXn3dQ3pzE8gdDrvND4Q5+cCQQDlLTUVbENLyg+3JUGZ7uIUhuZkTdz1WGsRQxlz\n" +
            "zVgmhYPThE0U4WGzu0z21v9J8USLHkwvyZ6AjZuUf6iNADw5AkEAg6XR2dVdU1GZ8BNeXTTXA0ec\n" +
            "1xbr2+l0W+oe6RivGL6SVrDViSUIvEZ1cy8pnAzZ3oiTvIv93FIkQqnGv8FKWQJAV7Z8ua1M+GXm\n" +
            "q+cDe6H1P3v+E+fFKNXlbJ7sz+iI032IXd0mD1bPqRWHuHXDEY7Y+BHpgOS+2F7aa/SMEyNC6QJA\n" +
            "B3WvyU+CAGAT7NqBQJ0Q3DtAv5jiRaFu7n7SFCSMW9+4Zwlzn79q6d+r5QJCiXBCkXzJF6GeYIrv\n" +
            "oVRJhj/61A==\n";
    public static void rsaTest() {
        int keyLength = 1024;
        //生成密钥对
        KeyPair keyPair = null;
        try {
            keyPair = RSAUtils.generateRSAKeyPair(keyLength);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //获取公钥
        byte[] publicKey = RSAUtils.getPublicKey(keyPair);
        //获取私钥
        byte[] privateKey = RSAUtils.getPrivateKey(keyPair);

        //公钥用base64编码
        String encodePublic = Base64.encodeToString(publicKey,Base64.DEFAULT);
        Log.d("TAG", "base64编码的公钥：" + encodePublic);

        //私钥用base64编码
        String encodePrivate = Base64.encodeToString(privateKey,Base64.DEFAULT);
        Log.d("TAG", "base64编码的私钥：" + privateKey);
        try {
            //str.getBytes(StandardCharsets.UTF_8);
            //new String(bytes, StandardCharsets.UTF_8)
            String enStr = encryptByPublicKey(test);
            Log.d("TAG", "加密：" + enStr);
            Log.d("TAG", "解密：" + decryptByPrivateKey(enStr));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**RSA算法*/
    public static final String RSA = "RSA";
    /**加密方式，android的*/
//  public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**加密方式，标准jdk的*/
    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    /** 使用公钥加密 */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return cp.doFinal(data);
    }

    public static String encryptByPublicKey(String str){
        try {
            byte[] enByte = encryptByPublicKey(str.getBytes(StandardCharsets.UTF_8),Base64.decode(publicKeyM,Base64.DEFAULT));
            String enStr = Base64.encodeToString(enByte,Base64.DEFAULT);
            return enStr;
        } catch (Exception e) {
            return "";
        }
    }

    /** 使用私钥解密 */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }

    public static String decryptByPrivateKey(String str){
        try {
            byte[] deStr = Base64.decode(str,Base64.DEFAULT);
            byte[] deByte = decryptByPrivateKey(deStr,Base64.decode(privateKeyM,Base64.DEFAULT));
            return new String(deByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }


    /** 生成密钥对，即公钥和私钥。key长度是512-2048，一般为1024 */
    public static KeyPair generateRSAKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
        kpg.initialize(keyLength);
        return kpg.genKeyPair();
    }

    /** 获取公钥，打印为48-12613448136942-12272-122-913111503-126115048-12...等等一长串用-拼接的数字 */
    public static byte[] getPublicKey(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return rsaPublicKey.getEncoded();
    }

    /** 获取私钥，同上 */
    public static byte[] getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return rsaPrivateKey.getEncoded();
    }

}
