package com.im.common.util.google.auth;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.image.QrCodeUtil;
import com.warrenstrange.googleauth.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Barry
 * @date 2020-05-25
 */
public class GoogleAuthUtil {
    private static GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder GACB =
            new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                    .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
                    .setWindowSize(5)
                    .setCodeDigits(6)
                    .setKeyRepresentation(KeyRepresentation.BASE32);
    private static GoogleAuthenticator GOOGLE_AUTHENTICATOR = new GoogleAuthenticator(GACB.build());

    /**
     * 谷歌验证码多少分钟内不允许重复使用
     **/
    private static final long GOOGLE_CODE_EXPIRE_MILLS = Duration.ofMinutes(2).toMillis();

    /**
     * 已经使用过的谷歌验证码，不处理集群情况，没什么大的影响
     **/
    private static TimedCache<String, Boolean> GOOGLE_CODE_USED_CACHE = CacheUtil.newTimedCache(GOOGLE_CODE_EXPIRE_MILLS);

    static {
        // 定时清理过期的谷歌验证码，避免占用过多内存
        // 清理缓存
        GOOGLE_CODE_USED_CACHE.schedulePrune(GOOGLE_CODE_EXPIRE_MILLS);
    }

    /**
     * 显示在谷歌身份验证器中的发行者
     *
     * @param issuer 发行者字符串，为空则不显示
     * @return GoogleBindVO
     */
    public static GoogleBindVO createCredentials(String username, String issuer) {
        // 创建密钥
        final GoogleAuthenticatorKey credentials = GOOGLE_AUTHENTICATOR.createCredentials();

        // 生成otp地址；issuer=显示在谷歌身份验证器上的字符，可当作logo，这里不使用
        String otpAuthTotpURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, username, credentials);

        // 生成二维码
        String qrCode = QrCodeUtil.encodeToBase64(otpAuthTotpURL);

        // 手动绑定密钥
        String key = credentials.getKey();

        return new GoogleBindVO(qrCode, key);
    }

    /**
     * 验证谷歌验证码是否通过，同一用户名，相同验证码1分钟内不允许重复使用
     *
     * @param username   用户名
     * @param googleKey  谷歌密钥
     * @param googleCode 谷歌验证码
     * @return true|false
     */
    public static RestResponse authoriseGoogle(String username, String googleKey, Integer googleCode) {
        if (googleCode == null) {
            return RestResponse.failed(ResponseCode.GOOGLE_CODE_REQUIRED);
        }

        if (StrUtil.isBlank(googleKey)) {
            return RestResponse.failed(ResponseCode.GOOGLE_CODE_ERROR);
        }

        // 同一用户,同一验证码,60分钟内不允许多次使用
        boolean beforeUsed = checkGoogleCodeBeforeUsed(username, googleKey, googleCode);
        if (beforeUsed) {
            return RestResponse.failed(ResponseCode.GOOGLE_CODE_DUPLICATED);
        }

        // 验证谷歌
        String secretKey = googleKey;
        boolean isCodeValid = GOOGLE_AUTHENTICATOR.authorize(secretKey, googleCode);

        if (!isCodeValid) {
            return RestResponse.failed(ResponseCode.GOOGLE_CODE_ERROR);
        }

        // 验证通过,加入缓存
        cacheGoogleCode(username, googleKey, googleCode);

        return RestResponse.OK;
    }

    /**
     * 谷歌验证码缓存key
     */
    private static String getGoogleCacheKey(String username, String googleKey, int googleCode) {
        String cacheKey = username + "_" + googleCode + "_" + googleKey;
        return cacheKey;
    }

    /**
     * 同一用户,同一验证码,60分钟内不允许多次使用
     *
     * @param username   用户名
     * @param googleCode 谷歌验证码
     * @return true表示使用过; false表示没有使用过
     */
    private static boolean checkGoogleCodeBeforeUsed(String username, String googleKey, int googleCode) {
        // 不再检查
        // String cacheKey = getGoogleCacheKey(username, googleKey, googleCode);
        // GOOGLE_CODE_USED_CACHE.get(cacheKey);
        // return GOOGLE_CODE_USED_CACHE.containsKey(cacheKey);
        return false;
    }

    /**
     * 缓存谷歌验证码
     */
    private static void cacheGoogleCode(String username, String googleKey, int googleCode) {
        String cacheKey = getGoogleCacheKey(username, googleKey, googleCode);
        GOOGLE_CODE_USED_CACHE.put(cacheKey, true);
    }
}
