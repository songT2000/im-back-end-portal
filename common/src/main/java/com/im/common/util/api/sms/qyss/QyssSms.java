package com.im.common.util.api.sms.qyss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.im.common.entity.SmsChannelConfig;
import com.im.common.entity.SmsTemplateConfig;
import com.im.common.entity.enums.SmsChannelTypeEnum;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.HttpClientUtil;
import com.im.common.util.Md5Util;
import com.im.common.util.StrUtil;
import com.im.common.util.api.sms.base.SmsHandler;
import com.im.common.util.api.sms.base.SmsHandlerProperty;
import com.im.common.util.api.sms.base.SmsRequestResponseVO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * QYSS，专门发国内短信
 *
 * @author Barry
 * @date 2022-02-10
 */
@SmsHandlerProperty(SmsChannelTypeEnum.QYSS)
public class QyssSms implements SmsHandler {
    private QyssSmsConfig getConfig(SmsChannelConfig channel) {
        return JSON.parseObject(channel.getThirdConfig(), QyssSmsConfig.class);
    }

    @Override
    public SmsRequestResponseVO send(SmsChannelConfig channel, String mobilePrefix, String mobile, String message) throws Exception {
        QyssSmsConfig config = getConfig(channel);

        String time = DateTimeUtil.toStr(LocalDateTime.now(), "yyyyMMddHHmmss");
        Map<String, String> param = new HashMap<>();
        param.put("userid", config.getUserId());
        param.put("timestamp", time);
        param.put("mobile", mobile);
        param.put("content", message);
        param.put("action", "send");
        param.put("rt", "json");

        String signStr = StrUtil.format("{}{}{}", config.getUserAccount(), config.getUserPassword(), time);
        String sign = Md5Util.getMd5Code(signStr);

        param.put("sign", sign);

        String rsp = HttpClientUtil.postWithForm(config.getUrl(), param, 30);

        if (StrUtil.isBlank(rsp)) {
            return SmsRequestResponseVO.failed("null returns");
        }
        JSONObject jsonObject = JSON.parseObject(rsp);
        String returnStatus = jsonObject.getString("ReturnStatus");
        if ("Success".equalsIgnoreCase(returnStatus)) {
            return SmsRequestResponseVO.success();
        }
        String errorMsg = jsonObject.getString("Message");
        return SmsRequestResponseVO.failed(errorMsg);

        // {"ReturnStatus":"Success","Message":"ok","RemainPoint":10004,"TaskID":2442818,"SuccessCounts":1}
        // {"ReturnStatus":"Faild","Message":"sign参数错误","RemainPoint":0,"TaskID":0,"SuccessCounts":0}
    }
}
