package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.I18nLanguageCache;
import com.im.common.entity.I18nLanguage;
import com.im.common.entity.I18nTranslate;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.mapper.I18nTranslateMapper;
import com.im.common.param.I18nTranslateAddParam;
import com.im.common.param.I18nTranslateDeleteParam;
import com.im.common.param.I18nTranslateEditParam;
import com.im.common.service.I18nTranslateService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 国际化表 服务实现类
 *
 * @author Barry
 * @date 2019/10/22
 */
@Service
public class I18nTranslateServiceImpl
        extends MyBatisPlusServiceImpl<I18nTranslateMapper, I18nTranslate>
        implements I18nTranslateService {

    private I18nLanguageCache languageCache;
    private CacheProxy cacheProxy;

    @Autowired
    public void setLanguageCache(I18nLanguageCache languageCache) {
        this.languageCache = languageCache;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse edit(I18nTranslateEditParam param) {
        List<I18nLanguage> languageList = languageCache.listFromRedis();
        if (CollectionUtil.isEmpty(languageList)) {
            return RestResponse.failed(ResponseCode.I18N_LANGUAGE_LIST_NOT_AVAILABLE);
        }

        // 组必须存在
        {
            boolean existed = existByGroup(param.getGroup());
            if (!existed) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
            }
        }

        List<I18nTranslate> translateList = getByGroupAndKey(param.getGroup(), param.getKey());
        if (CollectionUtil.isEmpty(translateList)) {
            // 对于组和key没有一条数据，认为数据不存在
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        Set<String> languageCodeSet = CollectionUtil.toSet(languageList, I18nLanguage::getCode);

        List<I18nTranslateEditParam.Data> dataList = param.getData();
        I18nTranslateEditParam.Data notMatch = CollectionUtil.findFirst(dataList, data -> !languageCodeSet.contains(data.getLanguageCode()));
        if (notMatch != null) {
            return RestResponse.failed(ResponseCode.I18N_LANGUAGE_NOT_AVAILABLE, notMatch.getLanguageCode());
        }

        // 修改
        for (I18nTranslateEditParam.Data data : dataList) {

            boolean contains = false;
            for (I18nTranslate i18nTranslate : translateList) {
                if (data.getLanguageCode().equals(i18nTranslate.getLanguageCode())) {
                    i18nTranslate.setValue(data.getValue());
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                I18nTranslate newTranslate = new I18nTranslate();
                newTranslate.setGroup(param.getGroup());
                newTranslate.setKey(param.getKey());
                newTranslate.setLanguageCode(data.getLanguageCode());
                newTranslate.setValue(data.getValue());
                translateList.add(newTranslate);
            }
        }

        saveOrUpdateBatch(translateList);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.I18N_TRANSLATE);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse add(I18nTranslateAddParam param) {
        List<I18nLanguage> languageList = languageCache.listFromRedis();
        if (CollectionUtil.isEmpty(languageList)) {
            return RestResponse.failed(ResponseCode.I18N_LANGUAGE_LIST_NOT_AVAILABLE);
        }

        // 组必须存在
        {
            boolean existed = existByGroup(param.getGroup());
            if (!existed) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
            }
        }

        // 组和KEY必须不存在
        {
            boolean existed = existByGroupAndKey(param.getGroup(), param.getKey());
            if (existed) {
                return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
            }
        }

        Set<String> languageCodeSet = CollectionUtil.toSet(languageList, I18nLanguage::getCode);

        List<I18nTranslateAddParam.Data> dataList = param.getData();
        I18nTranslateAddParam.Data notMatch = CollectionUtil.findFirst(dataList, data -> !languageCodeSet.contains(data.getLanguageCode()));
        if (notMatch != null) {
            return RestResponse.failed(ResponseCode.I18N_LANGUAGE_NOT_AVAILABLE, notMatch.getLanguageCode());
        }

        // 新增
        List<I18nTranslate> translateList = new ArrayList<>();
        for (I18nTranslateAddParam.Data data : dataList) {
            I18nTranslate newTranslate = new I18nTranslate();
            newTranslate.setGroup(param.getGroup());
            newTranslate.setKey(param.getKey());
            newTranslate.setLanguageCode(data.getLanguageCode());
            newTranslate.setValue(data.getValue());
            translateList.add(newTranslate);
        }

        saveBatch(translateList);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.I18N_TRANSLATE);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse delete(I18nTranslateDeleteParam param) {
        lambdaUpdate()
                .eq(I18nTranslate::getGroup, param.getGroup())
                .eq(I18nTranslate::getKey, param.getKey())
                .remove();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.I18N_TRANSLATE);

        return RestResponse.OK;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean existByGroup(String group) {
        Integer count = lambdaQuery()
                .eq(I18nTranslate::getGroup, group)
                .count();
        return count != null && count > 0;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean existByGroupAndKey(String group, String key) {
        Integer count = lambdaQuery()
                .eq(I18nTranslate::getGroup, group)
                .eq(I18nTranslate::getKey, key)
                .count();
        return count != null && count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(List<I18nTranslate> i18nTranslates) {
        List<I18nTranslate> list = list();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List<I18nTranslate> updateList = new ArrayList<>();

        // 组装唯一key
        Map<String, I18nTranslate> dbTranslateMap = CollectionUtil
                .toMapWithKey(list, translate -> translate.getLanguageCode() + "____" + translate.getGroup() + "____" + translate.getKey());

        i18nTranslates.forEach(i18nTranslate -> {
            String key = i18nTranslate.getLanguageCode() + "____" + i18nTranslate.getGroup() + "____" + i18nTranslate.getKey();

            I18nTranslate dbTranslate = dbTranslateMap.get(key);

            if (dbTranslate != null && !StrUtil.equals(dbTranslate.getValue(), i18nTranslate.getValue())) {
                dbTranslate.setValue(i18nTranslate.getValue());
                updateList.add(dbTranslate);
            }
        });

        if (CollectionUtil.isEmpty(updateList)) {
            return;
        }

        // 批量更新
        updateBatchById(updateList, 100);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.I18N_TRANSLATE);
    }

    public List<I18nTranslate> getByGroupAndKey(String group, String key) {
        return lambdaQuery()
                .eq(I18nTranslate::getGroup, group)
                .eq(I18nTranslate::getKey, key)
                .list();
    }

}