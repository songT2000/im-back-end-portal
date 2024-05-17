package com.im.common.service;

import com.im.common.entity.UserAuthToken;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.param.BaseLoginParam;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.util.redis.RedisSessionUser;
import lombok.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 前台登录token表 服务类
 *
 * @author Barry
 * @date 2018/6/8
 */
public interface UserAuthTokenService extends MyBatisPlusService<UserAuthToken> {
    /**
     * 根据token获取记录
     *
     * @param token      token
     * @param portalType 门户类型
     * @return UserAuthToken
     */
    UserAuthToken getByToken(String token, PortalTypeEnum portalType);

    /**
     * 根据用户ID列出所有token，有默认排序
     *
     * @param userId     用户ID
     * @param portalType 门户类型
     * @return
     */
    default List<UserAuthToken> listByUserId(long userId, PortalTypeEnum portalType) {
        return listByUserId(userId, portalType, null);
    }

    /**
     * 根据用户ID列出所有token，有默认排序
     *
     * @param userId       用户ID
     * @param portalType   门户类型
     * @param excludeToken 排除掉该token，为空该条件无效
     * @return
     */
    List<UserAuthToken> listByUserId(long userId, PortalTypeEnum portalType, String excludeToken);

    /**
     * 当用户使用用户名登录成功后，调用该方法生成新的token
     *
     * @param sessionUser 会话用户
     * @param param       登录参数
     * @return
     */
    String saveAuthTokenAfterLogin(RedisSessionUser sessionUser, BaseLoginParam param);

    /**
     * 删除token和redis会话
     *
     * @param token      token
     * @param portalType 门户类型
     * @return Boolean
     */
    boolean deleteTokenAndSession(String token, PortalTypeEnum portalType);

    /**
     * 删除所有token和redis会话
     *
     * @param userId     用户ID
     * @param portalType 门户类型
     * @return Boolean
     */
    boolean deleteUserTokenAndSession(long userId, PortalTypeEnum portalType);

    /**
     * 删除token，不删除redis会话
     *
     * @param token      token
     * @param portalType 门户类型
     * @return Boolean
     */
    boolean deleteTokenOnly(String token, PortalTypeEnum portalType);

    /**
     * 删除所有token，不删除redis会话
     *
     * @param userId     用户ID
     * @param portalType 门户类型
     * @return Boolean
     */
    boolean deleteUserTokenOnly(long userId, PortalTypeEnum portalType);

    /**
     * 检查token有效性，并刷新token
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param portalType 门户类型
     * @param <T>
     * @return 返回是个父类，各个工程可以转成自己的会话用户对象
     */
    <T extends RedisSessionUser> T checkToken(HttpServletRequest request, HttpServletResponse response, PortalTypeEnum portalType);

    /**
     * 创建新的JWT token
     *
     * @param username   用户名
     * @param deviceId   设备标识
     * @param deviceType 设备类型
     * @param portalType 门户类型
     * @return
     */
    Object[] newJwtToken(String username, String deviceId,
                         DeviceTypeEnum deviceType, PortalTypeEnum portalType);

    /**
     * 查询所有已过期token
     *
     * @return List<UserAuthToken>
     */
    List<UserAuthToken> listExpiredUserAuthToken();
}
