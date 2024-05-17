package com.im.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 该工具类与前端md5.js一致，为保持一致，所有MD5加密都应该使用该类
 *
 * @author Barry
 * @date 2017/6/28
 */
public final class Md5Util {
    private Md5Util() {
    }

    private static final String KEY_MD5 = "MD5";
    /**
     * 全局数组
     **/
    private static final String[] STR_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 返回形式为数字跟字符串
     *
     * @param bByte
     * @return
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return STR_DIGITS[iD1] + STR_DIGITS[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bByte
     * @return
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 将字符串加密成32位MD5格式，小写格式
     *
     * @param strObj 字符串
     * @return 32位MD5字符串，小写格式
     */
    public static String getMd5Code(String strObj) {
        try {
            MessageDigest md = MessageDigest.getInstance(KEY_MD5);
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            return byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(getMd5Code("123456"));
    }
}
