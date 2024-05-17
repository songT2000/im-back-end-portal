package com.im.common.vo;

import com.im.common.entity.PortalUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.redis.RedisSessionUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>用户SessionUser，保存一些不经常变化的数据</p>
 *
 * <p>像用户的基础属性，如真实类型、用户类型等这些发生变化，应当踢出当前在线用户</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@Data
@NoArgsConstructor
public class PortalSessionUser extends RedisSessionUser implements Serializable {
    private static final long serialVersionUID = -2542232390661330722L;

    /**
     * 用户签名，用来跟腾讯IM交互用的
     */
    private String userSig;

    /**
     * App 在即时通信 IM 分配的应用标识
     */
    private Long tecentImSdkAppid;

    /**
     * 初始化
     *
     * @param user  User
     * @param token token
     */
    public PortalSessionUser(PortalUser user, String token, String userSig) {
        this.id = user.getId();
        this.portalType = PortalTypeEnum.PORTAL;
        this.username = user.getUsername();
        this.token = token;
        this.userSig = userSig;
    }
}
