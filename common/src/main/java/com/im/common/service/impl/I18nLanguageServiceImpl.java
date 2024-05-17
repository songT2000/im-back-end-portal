package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.I18nLanguage;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.mapper.I18nLanguageMapper;
import com.im.common.service.I18nLanguageService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 国际化语言国家编码 服务实现类
 *
 * @author Barry
 * @date 2019/10/23
 */
@Service
public class I18nLanguageServiceImpl
        extends MyBatisPlusServiceImpl<I18nLanguageMapper, I18nLanguage>
        implements I18nLanguageService {
    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<I18nLanguage> listBySortAsc() {
        return lambdaQuery()
                .orderByAsc(I18nLanguage::getSort)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisable(String code, boolean enable) {
        // 修改
        boolean updated = lambdaUpdate()
                .eq(I18nLanguage::getCode, code)
                .eq(I18nLanguage::getEnabled, !enable)
                .set(I18nLanguage::getEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.I18N_LANGUAGE);

        return RestResponse.OK;
    }
}