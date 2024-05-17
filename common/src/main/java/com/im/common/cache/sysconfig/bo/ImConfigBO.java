package com.im.common.cache.sysconfig.bo;

import com.im.common.util.api.im.tencent.util.TLSSigAPIv2;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * IM配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
public class ImConfigBO extends BaseSysConfigBO {
    private static final Duration DURATION = Duration.ofDays(8);

    /**
     * 腾讯IM SDK URL，腾讯IM SDK的URL，此项只能在数据库手动修改，且一旦确定不可变更
     */
    private String tecentImSdkUrl;

    /**
     * 腾讯IM SDK APPID，腾讯IM SDK的APPID，此项只能在数据库手动修改，且一旦确定不可变更
     */
    private Long tecentImSdkAppid;

    /**
     * 腾讯IM SDK KEY，腾讯IM SDK的密钥，此项只能在数据库手动修改，且一旦确定不可变更
     */
    private String tecentImSdkKey;

    /**
     * 腾讯IM SDK管理员账号，腾讯IM的APP管理员账号名，此项只能在数据库手动修改，且一旦确定不可变更
     */
    private String tecentImSdkIdentifier;

    /**
     * 腾讯IM SDK管理员账号签名，该签名是动态生成的，不存数据库
     */
    private String tecentImSdkIdentifierSig;

    public void renewSig() {
        TLSSigAPIv2 sig = new TLSSigAPIv2(this.tecentImSdkAppid, this.tecentImSdkKey);
        this.tecentImSdkIdentifierSig = sig.genUserSig(this.tecentImSdkIdentifier, DURATION.getSeconds());
    }
}
