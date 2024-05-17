package com.im.common.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.entity.PortalUser;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.GoogleAuthService;
import com.im.common.service.PortalUserService;
import com.im.common.service.UserService;
import com.im.common.util.PasswordUtil;
import com.im.common.util.StrUtil;
import com.im.common.vo.PortalSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务类
 *
 * @author Barry
 * @date 2020-07-02
 */
@Service
public class UserServiceImpl implements UserService {
//    private static final Log LOG = LogFactory.get();
//
//     private PortalUserService portalUserService;
//     private GoogleAuthService googleAuthService;
//
//     @Autowired
//     public void setPortalUserService(PortalUserService portalUserService) {
//         this.portalUserService = portalUserService;
//     }
//
//     @Autowired
//     public void setGoogleAuthService(GoogleAuthService googleAuthService) {
//         this.googleAuthService = googleAuthService;
//     }
//
//     @Override
//     @Transactional(rollbackFor = Exception.class)
//     public RestResponse checkFundPwdAndGoogleCode(PortalSessionUser sessionUser, String inputFundPwd, Integer googleCode) {
//         // 必须先绑定资金密码
//         PortalUser user = portalUserService.getById(sessionUser.getId());
//         return checkFundPwdAndGoogleCode(user, inputFundPwd, googleCode);
//     }
//
//     @Override
//     @Transactional(rollbackFor = Exception.class, readOnly = true)
//     public RestResponse checkFundPwdAndGoogleCode(PortalUser user, String inputFundPwd, Integer googleCode) {
//         // 必须先绑定资金密码
//         if (StrUtil.isBlank(user.getFundPwd())) {
//             return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_NOT_YET_BIND);
//         }
//         if (!PasswordUtil.validatePwd(user.getFundPwd(), inputFundPwd)) {
//             LOG.info("前台用户{}资金密码错误，输入密码：{}，数据库密码：{}", user.getUsername(), inputFundPwd, user.getFundPwd());
//             return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_INCORRECT);
//         }
//
//         // 只有当用户绑定了谷歌，才有效
//         if (Boolean.TRUE.equals(user.getGoogleBound()) && StrUtil.isNotBlank(user.getGoogleKey())) {
//             // 验证谷歌，验证谷歌要放在最后一步，因为一组验证码短时间内只能使用一次
//             RestResponse googleRsp = googleAuthService.authoriseGoogle(user, googleCode);
//             if (!googleRsp.isOkRsp()) {
//                 return googleRsp;
//             }
//         }
//
//         return RestResponse.OK;
//     }
}
