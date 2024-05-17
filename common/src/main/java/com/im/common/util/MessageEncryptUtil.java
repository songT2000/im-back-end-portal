package com.im.common.util;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.im.common.entity.enums.CustomMessageTypeEnum;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgCustomItem;

/**
 * 消息加密解密工具类
 *
 * @author Barry
 * @date 2019/3/5
 */
public class MessageEncryptUtil {
    private MessageEncryptUtil() {
    }

    public static void main(String[] args) {
        System.out.println(decrypt("bIQKmSqtNW35xDt6n9YlUkl1lZGjVTj1zb1wrQJdp5uu1AZZlDHv5D/1ACJCy8Wwk1bbynjbZxNM75mA7CosDhTJzd6Sa9Y+lRWCWEwaHpLnX20ILzn2IOsnLIWf4xrg5MvcPPi1biobxDnhK3J9Pr3KXEYtHAUaaU5jZ4Igo9s="));
    }

    /**
     * 数据库字段加密密钥
     * todo 后续必须将该值配置化，需要考虑泄密
     **/
    public static final String ENCRYPT_KEY = "vdjyka39=Ck3*z8~";
    private static final byte[] ENCRYPT_KEY_BYTE = ENCRYPT_KEY.getBytes();
    private static final cn.hutool.crypto.symmetric.AES AES = SecureUtil.aes(ENCRYPT_KEY_BYTE);

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
     * 密文解密成明文
     *
     * @param encryptedStr 密文
     * @return 明文
     */
    public static String decrypt(String encryptedStr) {
        return AES.decryptStr(encryptedStr);
    }
}