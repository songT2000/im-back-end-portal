package com.im.common.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.im.common.constant.CommonConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * 字符串工具类
 *
 * @author Barry
 * @date 2020-02-22
 */
public final class StrUtil extends cn.hutool.core.util.StrUtil {
    private static final String API_ENCRYPT_PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAK/ZgKkSRwq/opSgbxux18/AZKglKvrJbgVPyhtCiitqbzAKlBPcjmJvU+4tbTh9Oq7Zyvejr4za4fK+g6OuPMubypCab16T6PjB+Rm2kEWkARGuS0emgn88WhVfddUA/ZMprqjHFYeAXUU7ic5IFV6iG5ryHFXsgbx55BghZ+crAgMBAAECgYEAoF7Dr20zzOGn69PEgIVYQ+c55P7Ai6ZX6BA0SVgNmjqe3DlJgjOKxsqwyG41RWcvtXZ6XA2zc8nKdg65DvVACw0WzLy7J8yyellnA+TNUtyALrBb9ouhbFMiVGoafpcZNtjHH+QnQvuJSnnq8qtp+t3FOY5qfFPXMO94yxjzUoECQQDZLG+zGd4TyqxUWL/qrcuxFxEIRMIxwsLWAfNU/NQloDne8q8xp//DTEBxzhKJnaDQBkNN3BrfYGOYnheB8mCjAkEAz0nCG/d9y0mu5D+yzX1tiW5KCyq18aXmUla64bw63LOsWAD7wz6xSn9c+XoGsL3SjXLSiZvqDdWzuXL7goLf2QJBAK1KHd8ltuTIBfZP0uOpxi0KoNaeNu6J3/nwGIAqNgXH8iWKNG1FBnqJr6qfqn8Qvi7/sFjCVtcDDLXNACj6hE0CQQCpjFYGFBBDiBhSP7vbkp1WzOwpaBz788uJS8F+RD0NKrVsIhnLoGrZWYXN1T7Fr+4ZnnKQ9gOin0prBJ6uKKg5AkEAvHHJM0vBm83xemRPMWvXFWqxeyBAgNCWHm7xY0CsXTaStoMXmIzbHeVCXJDzSxjyuhZXIzsKIX0a/BuaC4zbBg==";
    private static final String API_ENCRYPT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCv2YCpEkcKv6KUoG8bsdfPwGSoJSr6yW4FT8obQooram8wCpQT3I5ib1PuLW04fTqu2cr3o6+M2uHyvoOjrjzLm8qQmm9ek+j4wfkZtpBFpAERrktHpoJ/PFoVX3XVAP2TKa6oxxWHgF1FO4nOSBVeohua8hxV7IG8eeQYIWfnKwIDAQAB";
    private static final cn.hutool.crypto.asymmetric.RSA API_ENCRYPT_RSA = SecureUtil.rsa(API_ENCRYPT_PRIVATE_KEY, API_ENCRYPT_PUBLIC_KEY);


    /**
     * 隐藏身份证
     * 前6位 ****
     *
     * @param sfzNum 身份证
     * @return String
     */
    public static final String hideSfz(String sfzNum) {
        if (isBlank(sfzNum)) {
            return sfzNum;
        }

        String numNoSpace = StrUtil.replace(sfzNum, CommonConstant.SPACE, StrUtil.EMPTY);
        String first;
        String last = repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        int numLength = numNoSpace.length();

        if (numLength >= CommonConstant.INT_6) {
            first = sub(numNoSpace, CommonConstant.INT_0, CommonConstant.INT_6);
        } else {
            return sfzNum;
        }

        return first + CommonConstant.SPACE + last;
    }

    /**
     * 隐藏电话号码
     * 大于等于12位：前4位****后4位
     * 11位：前3位****后4位
     * 10位：前2位****后4位
     * 5位：****后4位
     * 其它：****
     *
     * @param phone 电话号码
     * @return String
     */
    public static final String hidePhone(String phone) {
        if (isBlank(phone)) {
            return phone;
        }

        String strNoSpace = StrUtil.replace(phone, CommonConstant.SPACE, StrUtil.EMPTY);
        String first;
        String middle = repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        String last;
        int strLength = strNoSpace.length();

        if (strLength >= 12) {
            first = sub(strNoSpace, CommonConstant.INT_0, CommonConstant.INT_4);
            last = sub(strNoSpace, -CommonConstant.INT_4, strLength);
        } else if (strLength >= 11) {
            first = sub(strNoSpace, CommonConstant.INT_0, 3);
            last = sub(strNoSpace, -CommonConstant.INT_4, strLength);
        } else if (strLength >= 10) {
            first = sub(strNoSpace, CommonConstant.INT_0, 2);
            last = sub(strNoSpace, -CommonConstant.INT_4, strLength);
        } else if (strLength >= 5) {
            first = repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
            last = sub(strNoSpace, -CommonConstant.INT_4, strLength);
        } else {
            return repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        }

        return first + middle + last;
    }

    /**
     * 隐藏社交号码ID或用户名等，如果是邮箱则以邮箱进行屏蔽
     * <p>
     * 大于等于12位：前4位****
     * 大于等于8位：前3位****
     * 大于等于5位：前2位****
     * 大于等于2位：前1位****
     * 其它：****
     *
     * @param socialId 社交号码ID或用户名等
     * @return String
     */
    public static final String hideSocialId(String socialId) {
        if (isBlank(socialId)) {
            return socialId;
        }

        int atIndex = socialId.lastIndexOf(CommonConstant.AT);
        if (atIndex > 0) {
            return hideEmail(socialId);
        }

        String strNoSpace = StrUtil.replace(socialId, CommonConstant.SPACE, StrUtil.EMPTY);
        String first;
        String last = repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        int strLength = strNoSpace.length();

        if (strLength >= 12) {
            first = sub(strNoSpace, CommonConstant.INT_0, CommonConstant.INT_4);
        } else if (strLength >= 8) {
            first = sub(strNoSpace, CommonConstant.INT_0, 3);
        } else if (strLength >= 5) {
            first = sub(strNoSpace, CommonConstant.INT_0, 2);
        } else if (strLength >= 2) {
            first = sub(strNoSpace, CommonConstant.INT_0, 1);
        } else {
            return repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        }

        return first + last;
    }

    /**
     * 隐藏卡号或邮箱
     *
     * @param cardNumber 卡号
     * @return String
     */
    public static final String hideCardNumber(String cardNumber) {
        if (isBlank(cardNumber)) {
            return cardNumber;
        }

        if (cardNumber.indexOf(CommonConstant.AT) > -1) {
            return hideEmail(cardNumber);
        }
        return hideBankAccountNumber(cardNumber);
    }

    /**
     * 隐藏邮箱
     *
     * <p>
     *
     * @param email 邮箱账号
     * @return
     * @前字符>=8位：前4位****@XXX.XXX <p>
     * @前字符>=4位：前2位****@XXX.XXX <p>
     * @前字符>=1位：前1位****@XXX.XXX <p>
     * 其它：原样返回
     */
    public static final String hideEmail(String email) {
        if (isBlank(email)) {
            return email;
        }

        int atIndex = email.lastIndexOf(CommonConstant.AT);
        if (atIndex <= -1) {
            return email;
        }

        String first;
        String middle = repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        String last = subSuf(email, atIndex);
        int atBeforeLength = sub(email, CommonConstant.INT_0, atIndex).length();

        if (atBeforeLength >= CommonConstant.INT_8) {
            first = sub(email, CommonConstant.INT_0, CommonConstant.INT_4);
        } else if (atBeforeLength >= CommonConstant.INT_4) {
            first = sub(email, CommonConstant.INT_0, CommonConstant.INT_2);
        } else if (atBeforeLength >= CommonConstant.INT_1) {
            first = sub(email, CommonConstant.INT_0, CommonConstant.INT_1);
        } else {
            return email;
        }

        return first + middle + last;
    }

    /**
     * 隐藏卡号或邮箱
     * <p>
     * 卡号>=8位：前4位 **** 后4位
     * <p>
     * 卡号>=4位：前2位 **** 后2位
     * <p>
     * 卡号>=1位：前1位 **** ****
     * <p>
     * 其它：原样返回
     *
     * @param cardNumber 卡号
     * @return String
     */
    public static final String hideBankAccountNumber(String cardNumber) {
        if (isBlank(cardNumber)) {
            return cardNumber;
        }

        String first;
        String middle = repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        String last;
        int cardNumberLength = cardNumber.length();

        if (cardNumberLength >= CommonConstant.INT_10) {
            first = sub(cardNumber, CommonConstant.INT_0, CommonConstant.INT_4);
            last = sub(cardNumber, -CommonConstant.INT_4, cardNumberLength);
        } else if (cardNumberLength >= CommonConstant.INT_4) {
            first = sub(cardNumber, CommonConstant.INT_0, CommonConstant.INT_2);
            last = sub(cardNumber, -CommonConstant.INT_2, cardNumberLength);
        } else if (cardNumberLength >= CommonConstant.INT_1) {
            first = sub(cardNumber, CommonConstant.INT_0, CommonConstant.INT_1);
            last = repeat(CommonConstant.ASTERISK, CommonConstant.INT_4);
        } else {
            return cardNumber;
        }

        return first + CommonConstant.SPACE + middle + CommonConstant.SPACE + last;
    }

    /**
     * 隐藏用户名
     * <p>
     * 取前2位，再***
     *
     * @param username 用户名
     * @return String
     */
    public static final String hideUsername(String username) {
        if (isBlank(username)) {
            return username;
        }

        String first = sub(username, CommonConstant.INT_0, CommonConstant.INT_2);
        String last = repeat(CommonConstant.ASTERISK, 3);

        return first + last;
    }

    /**
     * 隐藏姓名
     * <p>
     * 取第1位，再**
     *
     * @param withdrawName 姓名
     * @return String
     */
    public static final String hideWithdrawName(String withdrawName) {
        if (isBlank(withdrawName)) {
            return withdrawName;
        }

        String first = sub(withdrawName, CommonConstant.INT_0, CommonConstant.INT_1);
        String last = repeat(CommonConstant.ASTERISK, CommonConstant.INT_2);

        return first + last;
    }

    /**
     * 将返回给前端的数据进行加密,RSA公钥加密，前端用私钥解密
     *
     * @param str str
     * @return String
     */
    public static final String rsaPublicEncryptApiData(String str) {
        if (isBlank(str)) {
            return str;
        }
        return API_ENCRYPT_RSA.encryptBase64(str, KeyType.PublicKey);
    }

    /**
     * 将前端传过来的加密数据进行解密，RSA公钥解密，前端用私钥加密
     *
     * @param str str
     * @return String
     */
    public static final String rsaPublicDecryptApiData(String str) {
        if (isBlank(str)) {
            return str;
        }
        return API_ENCRYPT_RSA.decryptStr(str, KeyType.PublicKey);
    }

    /**
     * 将前端传过来的加密数据进行解密，RSA私钥解密，前端用私钥加密
     *
     * @param str str
     * @return String
     */
    public static final String rsaPrivateDecryptApiData(String str) {
        if (isBlank(str)) {
            return str;
        }
        return API_ENCRYPT_RSA.decryptStr(str, KeyType.PrivateKey);
    }

    public static Set<String> strArrToSet(String strArr) {
        if (isBlank(strArr)) {
            return new HashSet<>();
        }

        String[] arr = splitToArray(strArr, CommonConstant.DOT_EN);
        Set<String> set = new HashSet<>();
        for (String s : arr) {
            set.add(s);
        }

        return set;
    }

    public static Set<String> strArrToSetTrim(String strArr) {
        if (isBlank(strArr)) {
            return new HashSet<>();
        }

        String[] arr = splitToArray(strArr, CommonConstant.DOT_EN);
        Set<String> set = new HashSet<>();
        for (String s : arr) {
            set.add(trim(s));
        }

        return set;
    }

    public static boolean hasRepeatArr(String strArr) {
        if (isBlank(strArr)) {
            return false;
        }

        String[] arr = splitToArray(strArr, CommonConstant.DOT_EN);
        Set<String> set = strArrToSet(strArr);

        return arr.length != set.size();
    }

    /**
     * 任意两个相同，就是相同的
     * <p>
     * 比如
     * 1
     * 2
     * 3
     * <p>
     * 12
     * 13
     * 23
     *
     * @param strArr
     * @return
     */
    public static boolean any2Equals(String... strArr) {
        for (int i = 0; i < strArr.length - 1; i++) {
            for (int j = i + 1; j < strArr.length; j++) {
                String iStr = strArr[i];
                String jStr = strArr[j];

                if (equals(iStr, jStr)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        // String test = rsaPublicEncryptApiData("test");
        // System.out.println(rsaPrivateDecryptApiData(test));

        // System.out.println(hideUsername("aaaaa"));
        // System.out.println(hideUsername("aaaa"));
        // System.out.println(hideUsername("aaa"));
        // System.out.println(hideUsername("aa"));
        // System.out.println(hideUsername("a"));
        //
        // RSA rsa = new RSA();
        // System.out.println(rsa.getPublicKeyBase64());
        // System.out.println(rsa.getPrivateKeyBase64());

        // System.out.println(rsaPublicEncryptApiData("我是你爸爸"));
        // System.out.println(hideCardNumber("123456789"));
        // System.out.println(hideCardNumber("123456789@gmail.com"));
        // System.out.println(hideCardNumber("123456789@gmail.com"));
        // System.out.println(hideCardNumber("12345678@gmail.com"));
        // System.out.println(hideCardNumber("1234567@gmail.com"));
        // System.out.println(hideCardNumber("123456@gmail.com"));
        // System.out.println(hideCardNumber("12345@gmail.com"));
        // System.out.println(hideCardNumber("1234@gmail.com"));
        // System.out.println(hideCardNumber("123@gmail.com"));
        // System.out.println(hideCardNumber("12@gmail.com"));
        // System.out.println(hideCardNumber("1@gmail.com"));
        // System.out.println(hideCardNumber("@gmail.com"));
        // System.out.println(hideSocialId("@telegram"));
        // System.out.println(rsaPrivateDecryptApiData("cZEN5vs8eroSdh0gHPjcI5umf1opKXEpc2faKP7rDvAtMzJiB0shIbLa2ay0PckbJngWGqbImcOOfKDSNLK3vbU936Rnv7dcegEKmHwQMwCq1rK4IkimlsttNBrEyN8xQgWDdrknxzvXp08yay38T9cluY8f30L7RHbcBj/WSQw="));
        // System.out.println(rsaPublicDecryptApiData("cZEN5vs8eroSdh0gHPjcI5umf1opKXEpc2faKP7rDvAtMzJiB0shIbLa2ay0PckbJngWGqbImcOOfKDSNLK3vbU936Rnv7dcegEKmHwQMwCq1rK4IkimlsttNBrEyN8xQgWDdrknxzvXp08yay38T9cluY8f30L7RHbcBj/WSQw="));
    }
}
