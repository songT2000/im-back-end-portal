package com.im.common.service.impl;

import com.im.common.entity.SysBackupConfig;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.mapper.SysBackupConfigMapper;
import com.im.common.param.IdEnableDisableGoogleParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.param.SysBackupConfigEditParam;
import com.im.common.response.RestResponse;
import com.im.common.service.GoogleAuthService;
import com.im.common.service.SysBackupConfigService;
import com.im.common.util.NumberUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据备份配置表 实现类
 *
 * @author Barry
 * @date 2020-01-04
 */
@Service
public class SysBackupConfigServiceImpl
        extends MyBatisPlusServiceImpl<SysBackupConfigMapper, SysBackupConfig>
        implements SysBackupConfigService {
    private GoogleAuthService googleAuthService;

    @Autowired
    public void setGoogleAuthService(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, SysBackupConfigEditParam param) {
        RestResponse rsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        boolean updated = lambdaUpdate()
                .eq(SysBackupConfig::getId, param.getId())
                .set(SysBackupConfig::getBackupKeepDay, NumberUtil.belowZeroOrZero(param.getBackupKeepDay()))
                .set(SysBackupConfig::getRealKeepDay, NumberUtil.belowZeroOrZero(param.getRealKeepDay()))
                .update();

        if (updated) {
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(AdminSessionUser sessionUser, IdEnableDisableGoogleParam param) {
        RestResponse rsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        boolean updated = lambdaUpdate()
                .eq(SysBackupConfig::getId, param.getId())
                .set(SysBackupConfig::getEnabled, param.getEnable())
                .update();
        if (updated) {
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param) {
        RestResponse rsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        boolean updated = lambdaUpdate()
                .eq(SysBackupConfig::getId, param.getId())
                .remove();
        if (updated) {
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }
}
