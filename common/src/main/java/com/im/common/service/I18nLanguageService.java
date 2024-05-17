package com.im.common.service;

import com.im.common.entity.I18nLanguage;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import lombok.NonNull;

import java.util.List;

/**
 * 国际化语言国家编码 服务类
 *
 * @author Barry
 * @date 2019/10/23
 */
public interface I18nLanguageService extends MyBatisPlusService<I18nLanguage> {
    /**
     * 按正序列出语言列表
     *
     * @return List<I18nLanguage>
     */
    List<I18nLanguage> listBySortAsc();

    /**
     * 启用/禁用语言
     *
     * @param code   编码
     * @param enable enable
     * @return 返回OK表示成功
     */
    RestResponse enableDisable(String code, boolean enable);
}
