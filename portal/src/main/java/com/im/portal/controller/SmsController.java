package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.SmsTemplateConfigCache;
import com.im.common.entity.SmsTemplateConfig;
import com.im.common.param.SmsSendPortalParam;
import com.im.common.response.RestResponse;
import com.im.common.service.SmsService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.limit.RequestLimit;
import com.im.common.util.aop.limit.RequestLimitTypeEnum;
import com.im.common.util.ip.RequestIp;
import com.im.common.vo.SmsCountryPortalVO;
import com.im.portal.controller.url.ApiUrl;
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
 * 短信Controller
 *
 * @author Barry
 * @date 2022-02-10
 */
@RestController
@Api(tags = "短信相关接口")
@ApiSupport(order = 15)
public class SmsController extends BaseController {
    private SmsService smsService;
    private SmsTemplateConfigCache smsTemplateConfigCache;

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @Autowired
    public void setSmsTemplateConfigCache(SmsTemplateConfigCache smsTemplateConfigCache) {
        this.smsTemplateConfigCache = smsTemplateConfigCache;
    }

    @RequestMapping(value = ApiUrl.SMS_COUNTRY_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "短信支持国家列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<SmsCountryPortalVO>> smsCountryList() {
        List<SmsTemplateConfig> templateList = smsTemplateConfigCache.listFromLocal();
        templateList = CollectionUtil.filterList(templateList, e -> Boolean.TRUE.equals(e.getEnabled()));

        List<SmsCountryPortalVO> voList = CollectionUtil.toList(templateList, SmsCountryPortalVO::new);
        return ok(voList);
    }

    @RequestMapping(value = ApiUrl.SMS_SEND, method = RequestMethod.POST)
    @RequestLimit(type = RequestLimitTypeEnum.IP, second = 60, count = 180)
    @ApiOperation(value = "发送验证码", notes = "（6位数字）每60秒只能发送1次，注意用倒计时来体现")
    @ApiOperationSupport(order = 2)
    public RestResponse smsSend(@RequestBody @Valid SmsSendPortalParam param, @RequestIp String requestIp) {
        return smsService.sendVerificationCode(param.getCountryCode(), param.getMobile(), requestIp);
    }
}
