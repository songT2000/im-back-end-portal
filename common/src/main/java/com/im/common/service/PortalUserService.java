package com.im.common.service;

import com.im.common.entity.PortalUser;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.PortalUserAdminVO;
import com.im.common.vo.PortalUserLoginVO;
import lombok.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户表 服务类
 *
 * @author Barry
 * @date 2021-12-11
 */
public interface PortalUserService extends MyBatisPlusService<PortalUser> {
    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return User
     */
    PortalUser getByUsername(@NonNull String username);

    /**
     * 用户名密码登录
     *
     * @param request HttpServletRequest
     * @param param   登录参数
     * @return 如果成功，则data是AdminLoginUserVO
     */
    RestResponse<PortalUserLoginVO> manualLogin(HttpServletRequest request, ManualLoginParam param);

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
    void kickOutOtherLoginClient(PortalUser user, String excludeToken, ResponseCode logoutCode);

    /**
     * 踢出其它登录端，并提示在别处登录
     *
     * @param user         用户ID
     * @param excludeToken 排序掉该token
     * @return ResponseCode
     */
    default ResponseCode kickOutOtherByOtherLogin(PortalUser user, String excludeToken) {
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
        PortalUser user = getByUsername(username);
        if (user != null) {
            kickOutAllLoginClient(user.getId(), logoutCode);
        }
    }

    /**
     * 检查用户名是否存在
     *
     * @param username
     * @return
     */
    boolean isExists(String username);

    /**
     * 修改当前登录登录密码
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse editLoginPwdForPortal(@NonNull PortalSessionUser sessionUser, @NonNull UserEditPwdParam param);

    /**
     * 修改当前登录资金密码
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse bindFundPwdForPortal(@NonNull PortalSessionUser sessionUser, @NonNull UserBindPwdParam param);

    /**
     * 修改当前登录资金密码
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse editFundPwdForPortal(@NonNull PortalSessionUser sessionUser, @NonNull UserEditPwdParam param);

    /**
     * 绑定提现姓名
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse bindWithdrawNameForPortal(PortalSessionUser sessionUser, UserBindWithdrawNameParam param);

    /**
     * 给管理后台用的查询详情
     *
     * @param param 参数
     * @return PageVO
     */
    PortalUserAdminVO getVOForAdmin(UsernameParam param);

    /**
     * 给管理后台用的查询列表
     *
     * @param param 参数
     * @return PageVO
     */
    PageVO<PortalUserAdminVO> pageVOForAdmin(PortalUserPageAdminParam param);

    /**
     * 给管理后台用的新增用户
     *
     * @param param 参数
     * @return OK
     */
    RestResponse addForAdmin(PortalUserAddAdminParam param);

    /**
     * 给管理后台用的编辑用户
     *
     * @param param 参数
     * @return OK
     */
    RestResponse editForAdmin(PortalUserEditAdminParam param);

    /**
     * 启/禁登录
     *
     * @param param 参数
     * @return OK
     */
    RestResponse enableDisableLoginForAdmin(UsernameEnableDisableParam param);

    /**
     * 启/禁加好友
     *
     * @param param 参数
     * @return OK
     */
    RestResponse enableDisableAddFriendForAdmin(UsernameEnableDisableParam param);

    /**
     * 启/禁充值
     *
     * @param param 参数
     * @return OK
     */
    RestResponse enableDisableRechargeForAdmin(UsernameEnableDisableParam param);

    /**
     * 启/禁提现
     *
     * @param param 参数
     * @return OK
     */
    RestResponse enableDisableWithdrawForAdmin(UsernameEnableDisableParam param);

    /**
     * 启/禁用户
     *
     * @param param 参数
     * @return OK
     */
    RestResponse enableDisableForAdmin(UsernameEnableDisableParam param);

    /**
     * 重置资金密码
     *
     * @param param
     * @return
     */
    RestResponse resetFundPwdForAdmin(AdminSessionUser sessionUser, UsernameGoogleCodeParam param);

    /**
     * 编辑提现姓名
     *
     * @param param
     * @return
     */
    RestResponse editWithdrawNameForAdmin(AdminSessionUser sessionUser, PortalUserEditWithdrawNameAdminParam param);

    /**
     * 编辑我的邀请码
     *
     * @param param
     * @return
     */
    RestResponse editMyInviteCodeForAdmin(AdminSessionUser sessionUser, PortalUserEditMyInviteCodeAdminParam param);

    /**
     * 获取余额
     *
     * @param userId 用户ID
     * @return
     */
    BigDecimal getBalance(long userId);

    /**
     * 管理员增减余额
     *
     * @param sessionUser 当前登录用户
     * @param param       参数
     * @return OK
     */
    RestResponse adminAddBalance(AdminSessionUser sessionUser, UserAddBalanceAdminParam param);

    /**
     * 增减余额
     *
     * @param userId         用户ID
     * @param amount         金额
     * @param orderNum       注单号，可以为空
     * @param billType       账变类型，如果为空则不生成账单
     * @param billRemark     账变备注
     * @param billReportDate 记账日
     * @param allowToNegate  是否允许扣成负数
     * @return
     */
    RestResponse addBalanceWithReportDate(long userId, BigDecimal amount, String orderNum, UserBillTypeEnum billType, String billRemark,
                                          String billReportDate, boolean allowToNegate);

    /**
     * 列出所有人的余额快照
     *
     * @return
     */
    List<PortalUser> listBalanceSnapshot();

    /**
     * 注册
     *
     * @param param
     * @return
     */
    RestResponse registerForPortal(PortalUserRegisterParam param);

    /**
     * 设置首充&累计充
     *
     * @param userId
     * @param amount
     * @param time
     */
    void updateFirstAndTotalRecharge(long userId, BigDecimal amount, LocalDateTime time);

    /**
     * 设置首提&累计提
     *
     * @param userId
     * @param amount
     * @param time
     */
    void updateFirstAndTotalWithdraw(long userId, BigDecimal amount, LocalDateTime time);

    /**
     * 修改是否内部账号
     */
    RestResponse editInternalUserForAdmin(UsernameEnableDisableParam param);
}
