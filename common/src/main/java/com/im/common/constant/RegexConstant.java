package com.im.common.constant;

import java.util.regex.Pattern;

/**
 * 定义一些经常使用比较通用的正则
 *
 * @author Barry
 * @date 2020-06-02
 */
public final class RegexConstant {
    /**
     * 用户名
     */
    public static final String USERNAME_REGEX = "^([a-zA-Z0-9]){2,19}$";
    public static final String USERNAME_REGEX_REMARK = "用户名只能以英文字母，英文字母+数字，数字三种组合，长度2-20位";
    /**
     * 提现姓名
     */
    public static final String CHINESE_NAME_REGEX = "[\u4E00-\u9FFF·]{2,30}$";
    public static final String WITHDRAW_NAME_REGEX_REMARK = "提现姓名必须是中文";

    private RegexConstant() {
    }

    public static void main(String[] args) {
        Pattern compile = Pattern.compile(USERNAME_REGEX);
        System.out.println(compile.matcher("12").matches());
    }
}
