package com.im.portal.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.PortalIpBlackWhiteCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.entity.PortalUser;
import com.im.common.entity.PortalUserProfile;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserProfileService;
import com.im.common.service.PortalUserService;
import com.im.common.service.TimUserDeviceStateService;
import com.im.common.service.UserAuthTokenService;
import com.im.common.util.aop.limit.RequestLimit;
import com.im.common.util.aop.limit.RequestLimitTypeEnum;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.api.im.tencent.service.rest.TiAccountService;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.jwt.JwtInfo;
import com.im.common.util.jwt.JwtUtil;
import com.im.common.util.url.RequestWebsite;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.PortalUserLastLoginIpVO;
import com.im.common.vo.PortalUserLoginVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * 前台用户Controller
 *
 * @author Barry
 * @date 2019/10/12
 */
@RestController
@Api(tags = "前台用户相关接口")
@ApiSupport(order = 5)
public class PortalUserController extends BaseController {
    private PortalUserService portalUserService;
    private PortalUserProfileService portalUserProfileService;
    private UserAuthTokenService userAuthTokenService;
    private SysConfigCache sysConfigCache;
    private TimUserDeviceStateService timUserDeviceStateService;
    private TiAccountService tiAccountService;
    private PortalIpBlackWhiteCache portalIpBlackWhiteCache;

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setPortalUserProfileService(PortalUserProfileService portalUserProfileService) {
        this.portalUserProfileService = portalUserProfileService;
    }

    @Autowired
    public void setUserAuthTokenService(UserAuthTokenService userAuthTokenService) {
        this.userAuthTokenService = userAuthTokenService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setTimUserDeviceStateService(TimUserDeviceStateService timUserDeviceStateService) {
        this.timUserDeviceStateService = timUserDeviceStateService;
    }

    @Autowired
    public void setTiAccountService(TiAccountService tiAccountService) {
        this.tiAccountService = tiAccountService;
    }

    @Autowired
    public void setPortalIpBlackWhiteCache(PortalIpBlackWhiteCache portalIpBlackWhiteCache) {
        this.portalIpBlackWhiteCache = portalIpBlackWhiteCache;
    }

    @RequestMapping(value = ApiUrl.LOGIN, method = RequestMethod.POST)
    @RequestLimit(type = RequestLimitTypeEnum.IP, second = 60, count = 180)
    @ApiOperation(value = "登录", notes = "登录成功后，把返回数据里面的token字段，保存在客户端里(LocalStorage)，以后所有以/api/*/auth开头的接口，" +
            "都需要携带在header里，并取名" + JwtUtil.AUTH_HEADER +
            "如果RestResponse.code=GOOGLE_NOT_YET_BIND，则要弹窗提示用户绑定谷歌，谷歌相应的key和qrCode会在data里面")
    @ApiOperationSupport(order = 1)
    public RestResponse<PortalUserLoginVO> login(@RequestBody @Valid ManualLoginParam param,
                                                 @RequestIp String requestIp,
                                                 @RequestWebsite String requestWebsite,
                                                 HttpServletRequest request) {
        RestResponse ipRsp = portalIpBlackWhiteCache.checkIpBlackWhite(param.getUsername(), requestIp);
        if (!ipRsp.isOkRsp()) {
            return ipRsp;
        }

        param.setIp(requestIp);
        param.setUrl(requestWebsite);
        return portalUserService.manualLogin(request, param);
    }

    @RequestMapping(value = ApiUrl.LOGOUT, method = RequestMethod.POST)
    @ApiOperation("登出")
    @ApiOperationSupport(order = 2)
    public RestResponse logout(HttpServletRequest request) {
        return portalUserService.logout(request);
    }

    @RequestMapping(value = ApiUrl.GET_LOGIN_INFO, method = RequestMethod.POST)
    @ApiOperation("获取当前登录用户个人信息")
    @ApiOperationSupport(order = 3)
    public RestResponse<PortalUserLoginVO> getLoginInfo(@RequestIp String requestIp, HttpServletRequest request, HttpServletResponse response) {
        String token = JwtUtil.getTokenFromHeader(request);
        if (token == null) {
            return failed(ResponseCode.USER_SESSION_PLEASE_LOGIN_FIRST);
        }
        if (StrUtil.isNotBlank(token)) {
            JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
            if (jwtInfo != null) {
                RestResponse ipRsp = portalIpBlackWhiteCache.checkIpBlackWhite(jwtInfo.getUsername(), requestIp);
                if (!ipRsp.isOkRsp()) {
                    return ipRsp;
                }
            }
        }

        PortalSessionUser sessionUser = userAuthTokenService.checkToken(request, response, PortalTypeEnum.PORTAL);
        if (sessionUser == null) {
            portalUserService.logout(request);
            return null;
        }

        PortalUser user = portalUserService.getById(sessionUser.getId());
        if (user == null || !Boolean.TRUE.equals(user.getLoginEnabled())) {
            portalUserService.logout(request);
            return RestResponse.logout(ResponseCode.USER_SESSION_INFO_CHANGED);
        }
        PortalUserProfile profile = portalUserProfileService.getById(user.getId());

        PortalUserLoginVO loginUserVo = new PortalUserLoginVO(sessionUser, user, profile);

        return ok(loginUserVo);
    }

    @RequestMapping(value = ApiUrl.EDIT_LOGIN_PWD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.LOGIN_PWD_EDIT, hideParam = {"oldPwd", "newPwd"})
    @ApiOperation("修改当前登录用户登录密码")
    @ApiOperationSupport(order = 4)
    public RestResponse editLoginPwd(HttpServletRequest request,
                                     @RequestBody @Valid UserEditPwdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return portalUserService.editLoginPwdForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BIND_FUND_PWD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.FUND_PWD_BIND, hideParam = "password")
    @ApiOperation("绑定当前登录用户资金密码")
    @ApiOperationSupport(order = 5)
    public RestResponse bindFundPwd(HttpServletRequest request,
                                    @RequestBody @Valid UserBindPwdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return portalUserService.bindFundPwdForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.EDIT_FUND_PWD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.FUND_PWD_EDIT, hideParam = {"oldPwd", "newPwd"})
    @ApiOperation("修改当前登录用户资金密码")
    @ApiOperationSupport(order = 6)
    public RestResponse editFundPwd(HttpServletRequest request,
                                    @RequestBody @Valid UserEditPwdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return portalUserService.editFundPwdForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BIND_WITHDRAW_NAME, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BIND_WITHDRAW_NAME, hideParam = {"fundPwd"})
    @ApiOperation("绑定提现姓名")
    @ApiOperationSupport(order = 7)
    public RestResponse bindWithdrawName(HttpServletRequest request,
                                         @RequestBody @Valid UserBindWithdrawNameParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return portalUserService.bindWithdrawNameForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.REGISTER, method = RequestMethod.POST)
    @ApiOperation(value = "注册", notes = "注册后不会自动登录")
    @ApiOperationSupport(order = 8)
    @Lock4j(keys = "#requestIp")
    public RestResponse register(HttpServletRequest request,
                                 @RequestBody @Valid PortalUserRegisterParam param,
                                 @RequestIp String requestIp,
                                 @RequestWebsite String requestWebsite) {
        PortalSessionUser sessionUser = getSessionUser(request);
        if (sessionUser != null) {
            return RestResponse.failed(ResponseCode.USER_ALREADY_LOGIN);
        }
        param.setIp(requestIp);
        param.setUrl(requestWebsite);
        return portalUserService.registerForPortal(param);
    }

    @RequestMapping(value = ApiUrl.USER_USERNAME_EXIST, method = RequestMethod.POST)
    @ApiOperation("判断用户名是否存在")
    @ApiOperationSupport(order = 9)
    public RestResponse<Boolean> userUsernameExist(@RequestBody @Valid UsernameParam param) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), PortalTypeEnum.PORTAL);
        return ok(userId != null);
    }

    @RequestMapping(value = ApiUrl.USER_GET_ANYONE_LAST_LOGIN_IP, method = RequestMethod.POST)
    @ApiOperation("获取任意用户上次登录IP")
    @ApiOperationSupport(order = 10)
    public RestResponse<PortalUserLastLoginIpVO> userGetAnyoneLastLoginIp(@RequestBody @Valid UsernameParam param) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), PortalTypeEnum.PORTAL);
        if (userId == null) {
            return ok();
        }
        // 查询在线状态
        RestResponse<Map<String, Boolean>> restResponse = tiAccountService.queryOnlineStatus(ListUtil.of(param.getUsername()));
        Map<String, Boolean> data = restResponse.getData();

        // 先从在线状态里面查
        TimUserDeviceState lastLoginDevice = timUserDeviceStateService.getLastLoginDevice(userId);

        // 再从profile里面查
        PortalUserProfile profile = portalUserProfileService.getById(userId);
        return ok(new PortalUserLastLoginIpVO(lastLoginDevice, profile, param.getUsername(), data.get(param.getUsername())));
    }
}
