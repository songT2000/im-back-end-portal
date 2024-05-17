package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.mapper.SysConfigMapper;
import com.im.common.param.SysConfigEditParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.GoogleAuthService;
import com.im.common.service.SwitchAppService;
import com.im.common.service.SysConfigService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.spring.SpringContextUtil;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置表 服务实现类
 *
 * @author Barry
 * @date 2018/5/18
 */
@Service
public class SysConfigServiceImpl
        extends MyBatisPlusServiceImpl<SysConfigMapper, SysConfig>
        implements SysConfigService {
    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<SysConfig> listEditable(boolean advance) {
        return lambdaQuery()
                .eq(SysConfig::getAdvance, advance)
                .eq(SysConfig::getEditable, true)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse edit(AdminSessionUser sessionUser, SysConfigEditParam param, boolean advanced) {
        // 检查谷歌
        {
            GoogleAuthService googleAuthService = SpringContextUtil.getBean(GoogleAuthService.class);
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        List<SysConfigEditParam.Config> list = param.getList();

        List<SysConfig> configs = new ArrayList<>();
        for (SysConfigEditParam.Config config : list) {
            SysConfig sysConfig = getByGroupAndItem(param.getGroup(), config.getItem());

            if (sysConfig == null) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
            }
            if (advanced != sysConfig.getAdvance() || !Boolean.TRUE.equals(sysConfig.getEditable())) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EDITABLE);
            }
            sysConfig.setValue(config.getValue());
            configs.add(sysConfig);
        }

        updateBatchById(configs);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public SysConfig getByGroupAndItem(SysConfigGroupEnum group, String item) {
        return lambdaQuery()
                .eq(SysConfig::getGroup, group)
                .eq(SysConfig::getItem, item)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateItem(SysConfigGroupEnum group, String item, String value) {
        SysConfig one = lambdaQuery()
                .eq(SysConfig::getGroup, group)
                .eq(SysConfig::getItem, item)
                .one();
        if (one == null) {
            one = new SysConfig();
            one.setGroup(group);
            one.setItem(item);
            one.setValue(value);
            save(one);
        } else {
            lambdaUpdate()
                    .eq(SysConfig::getGroup, group)
                    .eq(SysConfig::getItem, item)
                    .set(SysConfig::getValue, value)
                    .update();
        }
    }
}