package com.im.common.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.cache.impl.SmsChannelConfigCache;
import com.im.common.cache.impl.SmsTemplateConfigCache;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.SmsChannelConfig;
import com.im.common.entity.SmsTemplateConfig;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.SmsService;
import com.im.common.util.RandomUtil;
import com.im.common.util.api.sms.base.SmsHandler;
import com.im.common.util.api.sms.base.SmsHandlerFactory;
import com.im.common.util.api.sms.base.SmsRequestResponseVO;
import com.im.common.util.redis.RedisKeySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author Barry
 * @date 2022-02-10
 */
@Service
public class SmsServiceImpl implements SmsService {
    private static final Log LOG = LogFactory.get();

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> redisValue;
    private RedisKeySerializer redisKeySerializer;
    private SmsTemplateConfigCache smsTemplateConfigCache;
    private SmsChannelConfigCache smsChannelConfigCache;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setRedisValue(ValueOperations<String, String> redisValue) {
        this.redisValue = redisValue;
    }

    @Autowired
    public void setRedisKeySerializer(RedisKeySerializer redisKeySerializer) {
        this.redisKeySerializer = redisKeySerializer;
    }

    @Autowired
    public void setSmsTemplateConfigCache(SmsTemplateConfigCache smsTemplateConfigCache) {
        this.smsTemplateConfigCache = smsTemplateConfigCache;
    }

    @Autowired
    public void setSmsChannelConfigCache(SmsChannelConfigCache smsChannelConfigCache) {
        this.smsChannelConfigCache = smsChannelConfigCache;
    }

    @Override
    public RestResponse sendVerificationCode(String countryCode, String mobile, String ip) {
        String realCountryCode = StrUtil.cleanBlank(countryCode);
        String realMobile = StrUtil.cleanBlank(mobile);

        // 检查该手机的同时发送数（1分钟内只能发送1次）
        {
            RestResponse rsp = checkVerificationRepeatForMobile(realCountryCode, realMobile);
            if (!rsp.isOkRsp()) {
                return rsp;
            }
        }
        // 检查该IP的同时发送数（同IP1分钟内只能有5笔发送）
        {
            RestResponse rsp = checkVerificationRepeatForIp(ip);
            if (!rsp.isOkRsp()) {
                return rsp;
            }
        }

        // 生成验证码
        String code = RandomUtil.randomNumberStr(6);

        // 获取模板
        SmsTemplateConfig template = smsTemplateConfigCache.getByCodeFromLocal(realCountryCode);
        if (template == null || !Boolean.TRUE.equals(template.getEnabled())) {
            return RestResponse.failed(ResponseCode.SMS_COUNTRY_NOT_SUPPORT);
        }
        SmsChannelConfig channel = smsChannelConfigCache.getByCodeFromLocal(template.getChannelCode());
        if (channel == null) {
            return RestResponse.failed(ResponseCode.SMS_COUNTRY_NOT_SUPPORT);
        }

        // 发送短信
        SmsHandler smsHandler = SmsHandlerFactory.getSmsHandler(channel.getType());
        String message = StrUtil.format(template.getVerificationTemplate(), MapUtil.of("code", code));
        try {
            SmsRequestResponseVO sendRsp = smsHandler.send(channel, template.getPrefix(), realMobile, message);
            if (!Boolean.TRUE.equals(sendRsp.getSuccess())) {
                return RestResponse.failed(ResponseCode.SMS_THIRD_CONNECT_INCORRECT, sendRsp.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e, "短信服务连接异常");
            return RestResponse.failed(ResponseCode.SMS_THIRD_CONNECT_EXCEPTION);
        }

        // 保存验证码到redis
        saveVerificationCode(realCountryCode, realMobile, code);

        // 保存该IP的同时发送数
        incrementVerificationRepeatForIp(ip);
        return RestResponse.OK;
    }

    @Override
    public RestResponse verifyVerificationCode(String countryCode, String mobile, String code) {
        // 通过手机从redis获取验证码
        String redisKey = StrUtil.format(RedisKeyEnum.SMS_VERIFICATION_BY_MOBILE.getVal(), countryCode, StrUtil.cleanBlank(mobile));
        Object val = redisValue.get(redisKey);
        if (val == null || StrUtil.isBlank(val.toString())) {
            return RestResponse.failed(ResponseCode.SMS_VERIFICATION_CODE_EXPIRED);
        }

        // 对比是否一样
        if (!val.toString().equalsIgnoreCase(code)) {
            return RestResponse.failed(ResponseCode.SMS_VERIFICATION_CODE_INCORRECT);
        }

        // 无需手机移除redis，会自动移除

        return RestResponse.OK;
    }

    private RestResponse checkVerificationRepeatForMobile(String countryCode, String mobile) {
        String redisKey = StrUtil.format(RedisKeyEnum.SMS_VERIFICATION_BY_MOBILE.getVal(), countryCode, mobile);
        Boolean hasKey = redisTemplate.hasKey(redisKey);
        return Boolean.TRUE.equals(hasKey) ? RestResponse.failed(ResponseCode.SYS_REQUEST_REPEAT) : RestResponse.OK;
    }

    private RestResponse checkVerificationRepeatForIp(String ip) {
        String redisKey = StrUtil.format(RedisKeyEnum.SMS_VERIFICATION_BY_IP.getVal(), ip);
        Object val = redisValue.get(redisKey);
        int alreadyCount = Convert.toInt(val, 0);
        final int totalCount = 5;

        if (alreadyCount >= totalCount) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_REPEAT_BY_IP);
        }
        return RestResponse.OK;
    }

    private void saveVerificationCode(String countryCode, String mobile, String code) {
        String redisKey = StrUtil.format(RedisKeyEnum.SMS_VERIFICATION_BY_MOBILE.getVal(), countryCode, mobile);

        redisValue.set(redisKey, code, Duration.ofMinutes(1));
    }

    private void incrementVerificationRepeatForIp(String ip) {
        String redisKey = StrUtil.format(RedisKeyEnum.SMS_VERIFICATION_BY_IP.getVal(), ip);
        byte[] redisKeyByte = redisKeySerializer.serialize(redisKey);
        redisValue.getOperations().executePipelined((RedisCallback<Object>) connection -> {
            connection.incrBy(redisKeyByte, 1);

            connection.expire(redisKeyByte, Duration.ofMinutes(1).getSeconds());

            return null;
        });
    }
}
