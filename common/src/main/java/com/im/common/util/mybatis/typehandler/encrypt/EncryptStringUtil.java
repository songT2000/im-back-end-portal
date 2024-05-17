package com.im.common.util.mybatis.typehandler.encrypt;

import cn.hutool.crypto.SecureUtil;

/**
 * 数据库字段加密解密工具类
 *
 * @author Barry
 * @date 2019/3/5
 */
public class EncryptStringUtil {
    private EncryptStringUtil() {
    }

    /**
     * 数据库字段加密密钥
     * todo 后续必须将该值配置化，需要考虑泄密
     **/
    private static final byte[] ENCRYPT_KEY = "j9VrjyTe=Cm3*z8~".getBytes();
    private static final cn.hutool.crypto.symmetric.AES AES = SecureUtil.aes(ENCRYPT_KEY);

    /**
     * 明文加密成密文
     *
     * @param plainStr 明文
     * @return 密文
     */
    public static String encrypt(String plainStr) {
        return AES.encryptBase64(plainStr);
    }

    /**
     * 明文加密成密文
     *
     * @param plainStr 明文
     * @return 密文
     */
    public static String encrypt(EncryptString plainStr) {
        return AES.encryptBase64(plainStr.toString());
    }

    /**
     * 明文加密成密文
     *
     * @param plainStr 明文
     * @return 密文
     */
    public static EncryptString encryptToObj(String plainStr) {
        return new EncryptString(AES.encryptBase64(plainStr));
    }

    /**
     * 明文加密成密文
     *
     * @param plainStr 明文
     * @return 密文
     */
    public static EncryptString encryptToObj(EncryptString plainStr) {
        return new EncryptString(AES.encryptBase64(plainStr.toString()));
    }

    /**
     * 密文解密成明文
     *
     * @param encryptedStr 密文
     * @return 明文
     */
    public static String decrypt(String encryptedStr) {
        return AES.decryptStr(encryptedStr);
    }

    /**
     * 密文解密成明文
     *
     * @param encryptedStr 密文
     * @return 明文
     */
    public static String decrypt(EncryptString encryptedStr) {
        return AES.decryptStr(encryptedStr.toString());
    }

    /**
     * 密文解密成明文
     *
     * @param encryptedStr 密文
     * @return 明文
     */
    public static EncryptString decryptToObj(String encryptedStr) {
        return new EncryptString(AES.decryptStr(encryptedStr));
    }

    /**
     * 密文解密成明文
     *
     * @param encryptedStr 密文
     * @return 明文
     */
    public static EncryptString decryptToObj(EncryptString encryptedStr) {
        return new EncryptString(AES.decryptStr(encryptedStr.toString()));
    }
}