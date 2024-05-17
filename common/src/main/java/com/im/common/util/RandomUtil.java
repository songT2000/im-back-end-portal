package com.im.common.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 随机工具类
 *
 * @author Barry
 * @date 2018/5/14
 */
public final class RandomUtil extends cn.hutool.core.util.RandomUtil {
    public static final String BASE_NUMBER = "0123456789";
    public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;

    private RandomUtil() {
    }

    /**
     * 生成随机token，分布式下保证唯一性
     *
     * @return token Str
     */
    public static String randomToken() {
        return IdWorker.getIdStr();
    }

    /**
     * 随机生成指定长度的字符串,不保证唯一性
     *
     * @param length           长度
     * @param includeNum       是否需要包含数字
     * @param firstShouldBeStr 首个字符是否应该是字符串
     * @return 随机字符串
     */
    public static String randomString(int length, boolean includeNum, boolean firstShouldBeStr) {
        // 首字符字符串
        if (firstShouldBeStr) {
            String firstCharacter = cn.hutool.core.util.RandomUtil.randomString(BASE_CHAR, 1);

            int leftLength = length - 1;

            if (leftLength <= 0) {
                return firstCharacter;
            }

            String leftStr = cn.hutool.core.util.RandomUtil.randomString(includeNum ? BASE_CHAR_NUMBER : BASE_CHAR, leftLength);

            return firstCharacter + leftStr;
        } else {
            return cn.hutool.core.util.RandomUtil.randomString(includeNum ? BASE_CHAR_NUMBER : BASE_CHAR, length);
        }
    }

    public static String randomNumberStr(int length) {
        return cn.hutool.core.util.RandomUtil.randomString(BASE_NUMBER, length);
    }
}
