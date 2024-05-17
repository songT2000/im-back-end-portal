package com.im.common.service;

import com.im.common.entity.AdminUser;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminUserLoginVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.AdminUserSimpleVO;
import com.im.common.vo.AdminUserVO;
import lombok.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 后台管理用户表 服务类
 *
 * @author Barry
 * @date 2019-11-06
 */
public interface AdminUserService extends MyBatisPlusService<AdminUser> {
    /**
     * 列出在线用户ID
     *
     * @return 在线用户ID
     */
    List<Long> listOnlineUserId();

    /**
     * 根据用户名获取用户，但不是被删除的
     *
     * @param username 用户名
     * @return AdminUser
     */
    AdminUser getByUsernameNotDeleted(String username);

    /**
     * 根据用户名修改用户，仅修改不为空的数据
     *
     * @param user AdminUser
     * @return boolean
     */
    boolean updateByUsername(AdminUser user);

    /**
     * 用户名密码登录
     *
     * @param request HttpServletRequest
     * @param param   登录参数
     * @return 如果成功，则data是AdminLoginUserVO
     */
    RestResponse<AdminUserLoginVO> manualLogin(HttpServletRequest request, ManualLoginParam param);

    /**
     * 使用token登录（系统自己调用，自动登录，用户并没有感知）
     *
     * @param request HttpServletRequest
     * @param param   登录参数
     * @return 如果成功，data为SupplierSessionUser
     */
    RestResponse tokenLogin(HttpServletRequest request, TokenLoginParam param);

    /**
     * 登出
     *
     * @param request HttpServletRequest
     * @return RestResponse
     */
    RestResponse logout(HttpServletRequest request);

    /**
     * 踢出其它登录端，并提示具体原因
     *
     * @param user         用户
     * @param excludeToken 排除掉该token
     * @param logoutCode   如果为空，则提示会话失效
     */
    void kickOutOtherLoginClient(AdminUser user, String excludeToken, ResponseCode logoutCode);

    /**
     * 踢出其它登录端，并提示在别处登录
     *
     * @param user         用户ID
     * @param excludeToken 排序掉该token
     * @return ResponseCode
     */
    default ResponseCode kickOutOtherByOtherLogin(AdminUser user, String excludeToken) {
        ResponseCode code = ResponseCode.USER_SESSION_OTHER_LOGIN;
        kickOutOtherLoginClient(user, excludeToken, code);
        return code;
    }

    /**
     * 踢出所有登录端，并提示具体原因（删除所有token，在redis session中存放logout code，用户在线状态改为离线）
     *
     * @param userId     用户ID
     * @param logoutCode 如果为空，则提示会话失效
     */
    void kickOutAllLoginClient(long userId, ResponseCode logoutCode);

    /**
     * 踢出所有登录端，并提示具体原因（删除所有token，在redis session中存放logout code，用户在线状态改为离线）
     *
     * @param username   用户名
     * @param logoutCode 如果为空，则提示会话失效
     */
    default void kickOutAllLoginClient(String username, ResponseCode logoutCode) {
        AdminUser user = getByUsernameNotDeleted(username);
        if (user != null) {
            kickOutAllLoginClient(user.getId(), logoutCode);
        }
    }

    /**
     * 是否已经绑定谷歌
     *
     * @param username 用户名
     * @return boolean
     */
    boolean hasBoundGoogle(String username);

    /**
     * 绑定谷歌
     *
     * @param param 绑定谷歌参数
     * @return data无数字，判断成功即可
     */
    RestResponse bindGoogle(BindGoogleParam param);

    /**
     * 列出管理员用户列表，列出自己下级账号
     *
     * @param param       列表
     * @param sessionUser 当前登录用户
     * @return
     */
    PageVO<AdminUserVO> pageLowerUsers(AdminUserPageParam param, AdminSessionUser sessionUser);

    /**
     * 检查用户名是否已经存在，不会忽略已删除的数据
     *
     * @param username 用户名
     * @return true：已存在；false：不存在
     */
    boolean isExists(String username);

    /**
     * 修改当前登录账户密码
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse editLoginPwd(AdminSessionUser sessionUser, UserEditPwdParam param);

    /**
     * 新增下级用户，登录密码：数据库格式
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse add(AdminSessionUser sessionUser,
                     AdminUserAddParam param);

    /**
     * 编辑下级用户，登录密码：数据库格式
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse edit(AdminSessionUser sessionUser,
                      AdminUserEditParam param);

    /**
     * 删除下级用户
     *
     * @param sessionUser 当前登录用户
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse delete(AdminSessionUser sessionUser,
                        UsernameGoogleCodeParam param);

    /**
     * 启用/禁用下级用户
     *
     * @param sessionUser 当前登录用户
     * @param username    要编辑的用户
     * @param enable      enable
     * @return 返回OK表示成功
     */
    RestResponse enableDisable(AdminSessionUser sessionUser,
                               String username, boolean enable);

    /**
     * 解绑下级用户谷歌
     *
     * @param sessionUser 当前登录用户
     * @param username    要编辑的用户
     * @return 返回OK表示成功
     */
    RestResponse unbindGoogle(AdminSessionUser sessionUser,
                              String username);

    /***
     * 重置密码错误次数
     * @param sessionUser AdminSessionUser
     * @param username 管理用户名
     * @return
     */
    RestResponse resetLoginPwdErrorNum(AdminSessionUser sessionUser, String username);

    /**
     * 列出所有已启用的管理员用户
     *
     * @return List<AdminUserSimpleVO>
     */
    List<AdminUserSimpleVO> listEnabledSimpleUser();
}
