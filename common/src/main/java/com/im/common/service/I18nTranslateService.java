package com.im.common.service;

import com.im.common.entity.I18nTranslate;
import com.im.common.response.RestResponse;
import com.im.common.param.I18nTranslateAddParam;
import com.im.common.param.I18nTranslateDeleteParam;
import com.im.common.param.I18nTranslateEditParam;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import lombok.NonNull;

import java.util.List;

/**
 * 国际化 服务类
 *
 * @author Barry
 * @date 2019/10/22
 */
public interface I18nTranslateService extends MyBatisPlusService<I18nTranslate> {
    /**
     * 编辑翻译
     *
     * @param param 参数
     * @return OK表示成功
     */
    RestResponse edit(I18nTranslateEditParam param);

    /**
     * 新增翻译
     *
     * @param param 参数
     * @return OK表示成功
     */
    RestResponse add(I18nTranslateAddParam param);

    /**
     * 删除翻译
     *
     * @param param 参数
     * @return OK表示成功
     */
    RestResponse delete(I18nTranslateDeleteParam param);

    /**
     * 根据组判断该值是否存在
     *
     * @param group 组
     * @return boolean
     */
    boolean existByGroup(String group);

    /**
     * 根据组和key判断该值是否存在
     *
     * @param group 组
     * @param key   key
     * @return boolean
     */
    boolean existByGroupAndKey(String group, String key);

    /**
     * 批量更新
     * <p>
     * 不带ID，只能根据 group、key和languageCode定位到数据
     * </p>
     *
     * @param i18nTranslates
     */
    void updateBatch(List<I18nTranslate> i18nTranslates);
}
