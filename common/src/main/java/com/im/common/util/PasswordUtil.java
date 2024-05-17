package com.im.common.util;

import cn.hutool.core.util.StrUtil;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;

import java.util.Locale;

/**
 * 密码加密工具类
 *
 * @author Barry
 * @date 2017/6/28
 */
public final class PasswordUtil {
    private PasswordUtil() {
    }

    public static void main(String[] args) {
        // System.out.println(generateDbPwdFromPlainString("a1234567"));
        System.out.println(Md5Util.getMd5Code(Md5Util.getMd5Code("a123456").toUpperCase()).toUpperCase());
        // System.out.println(Md5Util.getMd5Code(Md5Util.getMd5Code("a1234567").toUpperCase()));
        // dc483e80a7a0bd9ef71d8cf973673924
        // System.out.println(PasswordUtil.validatePwd("834278DE521BAA25062194902110A99F", "5F772190E8093EDD3BDA191DA7CE4D32"));
    }

    /**
     * 把给定的明文加密成数据库密码
     *
     * @return String
     */
    public static String generateDbPwdFromPlainString(String plainString) {
        // 第一次加密，并转换成大写
        String dbPwd = Md5Util.getMd5Code(plainString).toUpperCase();

        // 第二次加密，并转换成大写
        dbPwd = Md5Util.getMd5Code(dbPwd).toUpperCase();

        // 第三次加密，并转换成大写
        dbPwd = Md5Util.getMd5Code(dbPwd).toUpperCase();

        return dbPwd;
    }

    /**
     * 把给定的前端传过来的字符串加密成数据库密码
     * <p>
     * 前端进行2次MD5加密
     * 后端再对前端加密后的字符串再加密一次，就是数据库密码
     *
     * @return String
     */
    public static String generateDbPwdFromMd5String(String md5String) {
        String dbPwd = Md5Util.getMd5Code(md5String).toUpperCase();
        return dbPwd;
    }

    /**
     * 验证输入的密码是否正确
     *
     * @param dbPwd     数据库密码
     * @param md5String 前端传过来的密码，2次MD5
     * @return 验证结果，true:正确 false:错误
     */
    public static boolean validatePwd(String dbPwd, String md5String) {
        // 把前端传过来的密码再进行一次MD5
        String md5StringEncrypt = Md5Util.getMd5Code(md5String).toUpperCase();

        // 然后与数据库直接对比
        return md5StringEncrypt.equalsIgnoreCase(dbPwd);
    }



    /**
     * 验证绑定资金密码
     *
     * @param dbFundPwd  原资金密码
     * @param dbLoginPwd 原登录密码
     * @param inputPwd   输入的资金密码
     * @return
     */
    public static RestResponse validBindFundPwd(String dbFundPwd, String dbLoginPwd, String inputPwd) {
        // 检测旧密码是否匹配
        if (StrUtil.isNotBlank(dbFundPwd)) {
            return RestResponse.failed(ResponseCode.USER_PASSWORD_ALREADY_BOUND);
        }
        // 不允许和登录密码一样
        if (PasswordUtil.validatePwd(dbLoginPwd, inputPwd)) {
            return RestResponse.failed(ResponseCode.USER_FUND_PWD_MUST_DIFFERENT_LOGIN_PWD);
        }

        return RestResponse.OK;
    }

    /**
     * 验证编辑资金密码
     *
     * @param dbFundPwd   原资金密码
     * @param dbLoginPwd  原登录密码
     * @param inputOldPwd 输入的原资金密码
     * @param inputNewPwd 输入的新资金密码
     * @return
     */
    public static RestResponse validEditFundPwd(String dbFundPwd, String dbLoginPwd, String inputOldPwd, String inputNewPwd) {
        // 检测旧密码是否匹配
        if (StrUtil.isBlank(dbFundPwd)) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_NOT_YET_BIND);
        }
        if (!PasswordUtil.validatePwd(dbFundPwd, inputOldPwd)) {
            return RestResponse.failed(ResponseCode.USER_OLD_PASSWORD_INCORRECT);
        }

        // 新密码不允许和旧密码一样
        if (inputNewPwd.equalsIgnoreCase(inputOldPwd)) {
            return RestResponse.failed(ResponseCode.USER_NEW_PWD_MUST_DIFFERENT_OLD);
        }
        if (PasswordUtil.validatePwd(dbFundPwd, inputNewPwd)) {
            return RestResponse.failed(ResponseCode.USER_NEW_PWD_MUST_DIFFERENT_OLD);
        }

        // 不允许和登录密码一样
        if (PasswordUtil.validatePwd(dbLoginPwd, inputNewPwd)) {
            return RestResponse.failed(ResponseCode.USER_FUND_PWD_MUST_DIFFERENT_LOGIN_PWD);
        }

        return RestResponse.OK;
    }

    /**
     * 验证编辑登录密码
     *
     * @param dbLoginPwd  原登录密码
     * @param inputOldPwd 输入的原资金密码
     * @param inputNewPwd 输入的新资金密码
     * @return
     */
    public static RestResponse validEditLoginPwd(String dbLoginPwd, String inputOldPwd, String inputNewPwd) {
        if (!PasswordUtil.validatePwd(dbLoginPwd, inputOldPwd)) {
            return RestResponse.failed(ResponseCode.USER_OLD_PASSWORD_INCORRECT);
        }

        // 新密码不允许和旧密码一样
        if (inputNewPwd.equalsIgnoreCase(inputOldPwd)) {
            return RestResponse.failed(ResponseCode.USER_NEW_PWD_MUST_DIFFERENT_OLD);
        }
        if (PasswordUtil.validatePwd(dbLoginPwd, inputNewPwd)) {
            return RestResponse.failed(ResponseCode.USER_NEW_PWD_MUST_DIFFERENT_OLD);
        }

        return RestResponse.OK;
    }
}
