package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.I18nLanguageCache;
import com.im.common.entity.I18nLanguage;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.I18nLanguageCommonVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 国际化Controller
 *
 * @author Barry
 * @date 2019/10/23
 */
@RestController
@Api(tags = "国际化相关接口")
@ApiSupport(order = 4)
public class I18nController extends BaseController {
    private I18nLanguageCache i18nLanguageCache;

    @Autowired
    public void setI18nLanguageCache(I18nLanguageCache i18nLanguageCache) {
        this.i18nLanguageCache = i18nLanguageCache;
    }

    @RequestMapping(value = ApiUrl.I18N_LANGUAGE_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("启用的语言列表-无需登录")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<I18nLanguageCommonVO>> i18nLanguageSimpleList() {
        List<I18nLanguage> list = i18nLanguageCache.listEnabledFromRedis();
        return ok(CollectionUtil.toList(list, I18nLanguageCommonVO::new));
    }
}
