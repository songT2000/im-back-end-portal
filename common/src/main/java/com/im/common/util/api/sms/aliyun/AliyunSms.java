package com.im.common.util.api.sms.aliyun;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.AcsURLEncoder;
import com.aliyuncs.profile.DefaultProfile;
import com.google.common.net.PercentEscaper;
import com.im.common.entity.SmsChannelConfig;
import com.im.common.entity.enums.SmsChannelTypeEnum;
import com.im.common.util.HttpClientUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.sms.base.SmsHandler;
import com.im.common.util.api.sms.base.SmsHandlerProperty;
import com.im.common.util.api.sms.base.SmsRequestResponseVO;
import com.im.common.util.url.UrlParamUtil;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * 阿里云
 * 接入国际版，不需要备案模板
 * 接入国内版，需要在阿里云控制台提前备案模板，暂时没支持国内版
 *
 * @author Barry
 * @date 2022-02-10
 */
@SmsHandlerProperty(SmsChannelTypeEnum.ALIYUN)
public class AliyunSms implements SmsHandler {
    public static void main(String[] args) {
        // try {
        //     String str = "【IMTECH】Your verification code is {code}, please use it in one minute.*";
        //     System.out.println(URLEncoder.encode(str, "UTF-8"));
        //     System.out.println(percentEncode(str));
        //
        // } catch (UnsupportedEncodingException e) {
        //     e.printStackTrace();
        // }

        try {
            AliyunSmsConfig config = new AliyunSmsConfig();
            config.setUrl("https://dysmsapi.ap-southeast-1.aliyuncs.com");
            config.setAccessKeyId("LTAI5tPGY4U7PJDKkUtUt28N");
            config.setAccessKeySecret("eP9BJuPBpOYXWveuvvTErbKHXDTP4w");

            // 6309694195648

            Map<String, String> param = new TreeMap<>();

            // 公共参数
            param.put("Format", "JSON");
            param.put("Version", "2018-05-01");
            param.put("AccessKeyId", config.getAccessKeyId());

            // 使用UTC时间，比如北京时间2013年1月10日20点0分0秒，表示为2013-01-10T12:00:00Z，注意20变成12
            // 先获取北京时间
            ZonedDateTime beijingNow = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
            // 再获取UTC时间
            ZonedDateTime utfNow = beijingNow.withZoneSameInstant(ZoneOffset.UTC);
            String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(utfNow);
            param.put("Timestamp", URLEncoder.encode(timestamp, "UTF-8"));
            param.put("SignatureNonce", RandomUtil.randomNumbers(15));
            param.put("SignatureMethod", "HMAC-SHA1");
            param.put("SignatureVersion", "1.0");

            // 请求参数
            param.put("Action", "SendMessageToGlobe");
            // String toMobileNumber = "6309694195648";
            String toMobileNumber = "85218718050413";
            param.put("To", toMobileNumber);
            // param.put("From", "IMTECH");
            // 【IMTECH】您好，验证码123456，一分钟内有效。
            // param.put("Message", URLEncoder.encode("【IMTECH】Your verification code is 23456, please use it in one minute.", "UTF-8"));
            // param.put("Message", percentEncode("【IMTECH】您好，验证码45678，一分钟内有效。"));
            param.put("Message", percentEncode("【IMTECH】Your verification code is 23456, please use it in one minute."));

            String signStr = UrlParamUtil.join(param);
            signStr = "GET&" + URLEncoder.encode("/", "UTF-8") + "&" + URLEncoder.encode(signStr, "UTF-8");

            HMac hMac = SecureUtil.hmacSha1(config.getAccessKeySecret() + "&");
            String sign = hMac.digestBase64(signStr, false);
            // System.out.println(hMac.digestBase64(curl, true));

            param.put("Signature", URLEncoder.encode(sign, "UTF-8"));

            String rsp = HttpClientUtil.get(config.getUrl(), param, 30);
            System.out.println(rsp);
            if (StrUtil.isBlank(rsp)) {
                System.out.println("null returns");
                return;
            }
            JSONObject jsonObject = JSON.parseObject(rsp);
            if (!jsonObject.containsKey("ResponseCode")) {
                String errorMsg = jsonObject.getString("Message");
                System.out.println(errorMsg);
                return;
            }
            String responseCode = jsonObject.getString("ResponseCode");
            if ("OK".equalsIgnoreCase(responseCode)) {
                System.out.println("OK");
                return;
            }
            String errorMsg = getErrorMsgFromResponse(jsonObject);
            System.out.println(errorMsg);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AliyunSmsConfig getConfig(SmsChannelConfig channel) {
        return JSON.parseObject(channel.getThirdConfig(), AliyunSmsConfig.class);
    }

    @Override
    public SmsRequestResponseVO send(SmsChannelConfig channel, String mobilePrefix, String mobile, String message) throws Exception {
        return sendMessageToGlobe(channel, mobilePrefix, mobile, message);
    }


    /**
     * 发送国际/港澳台短信接口
     *
     * @return
     */
    private SmsRequestResponseVO sendMessageToGlobe(SmsChannelConfig channel, String mobilePrefix, String mobile, String message) throws Exception {
        AliyunSmsConfig config = getConfig(channel);

        Map<String, String> param = new TreeMap<>();

        // 公共参数
        param.put("Format", "JSON");
        param.put("Version", "2018-05-01");
        param.put("AccessKeyId", config.getAccessKeyId());

        // 使用UTC时间，比如北京时间2013年1月10日20点0分0秒，表示为2013-01-10T12:00:00Z，注意20变成12
        // 先获取北京时间
        ZonedDateTime beijingNow = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        // 再获取UTC时间
        ZonedDateTime utfNow = beijingNow.withZoneSameInstant(ZoneOffset.UTC);
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(utfNow);
        param.put("Timestamp", URLEncoder.encode(timestamp, "UTF-8"));
        param.put("SignatureNonce", RandomUtil.randomNumbers(15));
        param.put("SignatureMethod", "HMAC-SHA1");
        param.put("SignatureVersion", "1.0");

        // 请求参数
        param.put("Action", "SendMessageToGlobe");
        String toMobileNumber = getToMobileNumber(mobilePrefix, mobile);
        param.put("To", toMobileNumber);
        // param.put("From", "IMTECH");
        // 【IMTECH】您好，验证码123456，一分钟内有效。
        // 像这样加密，发给国外，中文的发不出去，但是API响应是成功，发英文可以发出去
        param.put("Message", percentEncode(message));

        String signStr = UrlParamUtil.join(param);
        signStr = "GET&" + URLEncoder.encode("/", "UTF-8") + "&" + URLEncoder.encode(signStr, "UTF-8");

        HMac hMac = SecureUtil.hmacSha1(config.getAccessKeySecret() + "&");
        String sign = hMac.digestBase64(signStr, false);
        // System.out.println(hMac.digestBase64(curl, true));

        param.put("Signature", URLEncoder.encode(sign, "UTF-8"));

        String rsp = HttpClientUtil.get(config.getUrl(), param, 30);
        if (StrUtil.isBlank(rsp)) {
            return SmsRequestResponseVO.failed("null returns");
        }
        JSONObject jsonObject = JSON.parseObject(rsp);
        if (!jsonObject.containsKey("ResponseCode")) {
            String errorMsg = getErrorMsgFromResponse(jsonObject);
            return SmsRequestResponseVO.failed(errorMsg);
        }
        String responseCode = jsonObject.getString("ResponseCode");
        if ("OK".equalsIgnoreCase(responseCode)) {
            return SmsRequestResponseVO.success();
        }
        String errorMsg = getErrorMsgFromResponse(jsonObject);
        return SmsRequestResponseVO.failed(errorMsg);
    }

    /**
     * 发送中国内地（大陆）短信接口，需要在阿里云控制台提前备案模板
     *
     * @return
     */
    private SmsRequestResponseVO sendMessageWithTemplate(SmsChannelConfig channel, String mobilePrefix, String mobile, String message) throws Exception {
        // todo 实现文档在下面，暂时没做
        // https://www.alibabacloud.com/help/zh/doc-detail/160525.htm?spm=a2c63.p38356.0.0.268a363f9bA3sb
        return SmsRequestResponseVO.failed("not yet support");
    }

    private String getToMobileNumber(String mobilePrefix, String to) {
        // 把+号替换掉，阿里云不需要
        String prefix = StrUtil.replace(mobilePrefix, "+", "");
        return prefix + to;
    }

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, "UTF-8").replace("+", "%20")
                .replace("*", "%2A").replace("%7E", "~") : null;
    }

    private static String getErrorMsgFromResponse(JSONObject jsonObject) {
        if (jsonObject.containsKey("Message")) {
            return jsonObject.getString("Message");
        }
        if (jsonObject.containsKey("ResponseDescription")) {
            return jsonObject.getString("ResponseDescription");
        }
        return "";
    }
}
