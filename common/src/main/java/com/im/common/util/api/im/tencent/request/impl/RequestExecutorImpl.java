package com.im.common.util.api.im.tencent.request.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ImConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.HttpClientUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.error.ApiErrorMsgEnum;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.fastjson.FastJsonConfigUtil;
import com.im.common.util.fastjson.LocalDateSerializer;
import com.im.common.util.fastjson.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * http请求执行器.
 */
@Component
public class RequestExecutorImpl implements RequestExecutor {
    private static final SerializerFeature[] FASTJSON_SERIALIZER_FEATURE = buildSerializerFeature();
    private static final SerializeConfig FASTJSON_SERIALIZER_CONFIG = buildSerializeConfig();

    private static final Log LOG = LogFactory.get();
    private static final int MAX_RETRY = 3;//最多重试3次
    private SysConfigCache sysConfigCache;

    private static SerializerFeature[] buildSerializerFeature() {
        return new SerializerFeature[]{SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteDateUseDateFormat,
                // SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.DisableCircularReferenceDetect,
                // SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteBigDecimalAsPlain};
    }

    /**
     * 配置序列化配置，同时全局设置FastJson
     *
     * @return
     */
    private static SerializeConfig buildSerializeConfig() {
        SerializeConfig serializeConfig = new SerializeConfig();

        // 日期时间
        serializeConfig.put(LocalDate.class, LocalDateSerializer.INSTANCE);
        serializeConfig.put(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);

        return serializeConfig;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Override
    public RestResponse execute(String uri, Object param) {
        return execute(uri, param, null);
    }

    @Override
    public <T> RestResponse<T> execute(String uri, Object param, Class<T> clazz) {
        return executeRetry(uri, param, clazz, CommonConstant.INT_0);
    }

    /**
     * 执行方法，支持重试
     */
    private <T> RestResponse<T> executeRetry(String uri, Object param, Class<T> clazz, int retryNum) {
        ImConfigBO imConfig = sysConfigCache.getImConfigFromLocal();

        String url = StrUtil.format("{}/{}?sdkappid={}&identifier={}&usersig={}&random={}&contenttype=json",
                imConfig.getTecentImSdkUrl(), uri, imConfig.getTecentImSdkAppid(), imConfig.getTecentImSdkIdentifier(),
                imConfig.getTecentImSdkIdentifierSig(), RandomUtil.randomInt());

        // 需要特别注意的是，POST 包体不能为空，即使某条协议包体中不需要携带任何信息，也需要携带一个空的 JSON 对象，即{}。
        Object realParam = Optional.ofNullable(param).orElse(new Object());
        String req = JSON.toJSONString(realParam, FASTJSON_SERIALIZER_CONFIG, null, null,
                FastJsonConfigUtil.DEFAULT_GENERATE_FEATURE, FASTJSON_SERIALIZER_FEATURE);

        // LOG.info("请求URL：{}", url);
        // LOG.info("请求json：{}", req);

        String rsp;
        try {
            // OK HTTP默认启用了连接池
            rsp = HttpClientUtil.postWithAny(url, req, null, MediaType.APPLICATION_JSON_UTF8_VALUE, 30);
            // rsp = HttpClientUtil.postWithJson(url, realParam, 30);
        } catch (IOException e) {
            LOG.error(e, "IM连接异常");
            throw new ImException(ResponseCode.IM_CONNECT_EXCEPTION);
        }

        // LOG.info("返回结果：{}", rsp);

        JSONObject jsonObject = JSONObject.parseObject(rsp);
        if (!jsonObject.containsKey("ErrorCode")) {
            return RestResponse.failed(ResponseCode.IM_EXECUTE_EXCEPTION, "null returns");
        }
        // 错误码，0为成功，其他为失败，可查询 错误码表(BaseErrorMsgEnum) 得到具体的原因.
        Integer errorCode = jsonObject.getInteger("ErrorCode");
        if (errorCode != 0) {
            LOG.error("IM执行返回失败：{}", rsp);
            LOG.info("请求URL：{}", url);
            LOG.info("请求json：{}", req);
            if (errorCode == ApiErrorMsgEnum.CODE_70001.getCode() || errorCode == ApiErrorMsgEnum.CODE_70003.getCode()) {
                //签名失效，重新签名，然后重试
                imConfig.renewSig();
                if (retryNum < MAX_RETRY) {
                    retryNum++;
                    return executeRetry(uri, param, clazz, retryNum);
                }
            }

            String errorMsg = ApiErrorMsgEnum.findMsgByCode(errorCode);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = jsonObject.getString("ErrorInfo");
            }
            return RestResponse.failed(ResponseCode.IM_EXECUTE_EXCEPTION, errorMsg);
        }

        T ret = clazz == null ? null : JSON.parseObject(rsp, clazz);
        return RestResponse.ok(ret);
    }
}
