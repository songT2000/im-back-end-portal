package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.SensitiveWord;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.SensitiveWordMapper;
import com.im.common.response.RestResponse;
import com.im.common.service.SensitiveWordService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 敏感词管理服务
 */
@Service
public class SensitiveWordServiceImpl extends MyBatisPlusServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {

    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse delete(Long id) {
        removeById(id);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SENSITIVE_WORD);
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse add(List<String> words) {
        List<SensitiveWord> list = words.stream().map(SensitiveWord::new).collect(Collectors.toList());
        saveBatch(list);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SENSITIVE_WORD);
        return RestResponse.OK;
    }
}
