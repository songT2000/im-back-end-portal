package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.I18nLanguageCache;
import com.im.common.entity.I18nLanguage;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.CodeEnableDisableParam;
import com.im.common.param.I18nTranslateAddParam;
import com.im.common.param.I18nTranslateDeleteParam;
import com.im.common.param.I18nTranslateEditParam;
import com.im.common.response.RestResponse;
import com.im.common.service.I18nLanguageService;
import com.im.common.service.I18nTranslateService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.vo.I18nLanguageCommonVO;
import com.im.common.vo.I18nTranslateCommonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * 国际化Controller
 *
 * @author Barry
 * @date 2019/10/23
 */
@RestController
@Api(tags = "国际化相关接口")
public class I18nController extends BaseController {
    private I18nTranslateService i18nTranslateService;
    private I18nLanguageService i18nLanguageService;
    private I18nLanguageCache i18nLanguageCache;

    @Autowired
    public void setI18nTranslateService(I18nTranslateService i18nTranslateService) {
        this.i18nTranslateService = i18nTranslateService;
    }

    @Autowired
    public void setI18nLanguageService(I18nLanguageService i18nLanguageService) {
        this.i18nLanguageService = i18nLanguageService;
    }

    @Autowired
    public void setI18nLanguageCache(I18nLanguageCache i18nLanguageCache) {
        this.i18nLanguageCache = i18nLanguageCache;
    }

    @RequestMapping(value = ApiUrl.I18N_LANGUAGE_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("启用的语言列表-无需登录")
    public RestResponse<List<I18nLanguageCommonVO>> i18nLanguageSimpleList() {
        List<I18nLanguage> list = i18nLanguageCache.listEnabledFromRedis();
        return ok(CollectionUtil.toList(list, I18nLanguageCommonVO::new));
    }

    @RequestMapping(value = ApiUrl.I18N_LANGUAGE_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("获取系统所有语言列表")
    public RestResponse<List<I18nLanguageCommonVO>> i18nLanguageList() {
        List<I18nLanguage> list = i18nLanguageService.listBySortAsc();
        return ok(CollectionUtil.toList(list, I18nLanguageCommonVO::new));
    }

    @RequestMapping(value = ApiUrl.I18N_LANGUAGE_ENABLE_DISABLE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.I18N_LANGUAGE_ENABLE_DISABLE)
    @ApiOperation("启/禁语言")
    public RestResponse i18nLanguageEnableDisable(@RequestBody @Valid CodeEnableDisableParam param) {
        return i18nLanguageService.enableDisable(param.getCode(), param.getEnable());
    }

    @RequestMapping(value = ApiUrl.I18N_TRANSLATE_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("获取翻译列表")
    public RestResponse<List<I18nTranslateCommonVO>> i18nTranslateList() {
        List<I18nTranslateCommonVO> list = i18nTranslateService.listVO(I18nTranslateCommonVO::new);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.I18N_TRANSLATE_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.I18N_TRANSLATE_EDIT)
    @ApiOperation("编辑翻译")
    public RestResponse i18nTranslateEdit(@RequestBody @Valid I18nTranslateEditParam param) {
        return i18nTranslateService.edit(param);
    }

    @RequestMapping(value = ApiUrl.I18N_TRANSLATE_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.I18N_TRANSLATE_ADD)
    @ApiOperation("新增翻译")
    public RestResponse i18nTranslateAdd(@RequestBody @Valid I18nTranslateAddParam param) {
        return i18nTranslateService.add(param);
    }

    @RequestMapping(value = ApiUrl.I18N_TRANSLATE_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.I18N_TRANSLATE_DELETE)
    @ApiOperation("删除翻译")
    public RestResponse i18nTranslateDelete(@RequestBody @Valid I18nTranslateDeleteParam param) {
        return i18nTranslateService.delete(param);
    }
}