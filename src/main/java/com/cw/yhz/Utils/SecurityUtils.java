package com.cw.yhz.Utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * 加密处理 帮助类
 */
public class SecurityUtils {

    private static final byte[] passwd = "chint-intf-mes-123456".getBytes();

    private static SecretKey getCipher()throws Exception{
        DESKeySpec keySpec = new DESKeySpec(passwd);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return secretKey;
    }

    /**
     * 加密
     * @param str
     * @return
     * @throws Exception
     */
    public  static String  encrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("des");
        cipher.init(Cipher.ENCRYPT_MODE, getCipher(),  new SecureRandom());
        byte[] cipherData = cipher.doFinal(str.getBytes());
        return  new BASE64Encoder().encode(cipherData);
    }

    /**
     * 解密
     * @param encryptStr
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr) throws Exception {
        Cipher cipher = Cipher.getInstance("des");
        cipher.init(Cipher.DECRYPT_MODE, getCipher(), new SecureRandom());
        byte [] strByteArr =  new BASE64Decoder().decodeBuffer(encryptStr);
        byte[] plainData = cipher.doFinal(strByteArr);
        return new String(plainData);
    }

    public static void main(String[] args) throws Exception {
        String str= encrypt("1231231233213213");
        System.out.println(str);
        String s= decrypt("HTzPolqWZfVt0iCAiuAGH57in07oOrWX");
        System.out.printf(s);
    }
}
