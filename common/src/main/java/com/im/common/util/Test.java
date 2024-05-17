package com.im.common.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Barry
 * @date 2022-01-27
 */
public class Test {
    public static void main(String[] args) {
        String time = DateTimeUtil.toStr(LocalDateTime.now(), "yyyyMMddHHmmss");
        Map<String, String> param = new HashMap<>();
        param.put("userid", "3477");
        param.put("timestamp", time);
        param.put("mobile", "14783505211");
        param.put("content", "【IMTECH】您好，验证码123456，一分钟内有效。");
        // param.put("content", new String("Hello".getBytes(StandardCharsets.UTF_8)));
        // param.put("sendTime", "");
        param.put("action", "send");
        param.put("rt", "json");

        String signStr = StrUtil.format("{}{}{}", "2026749374", "2026749374", time);
        String sign = Md5Util.getMd5Code(signStr);

        param.put("sign", sign + "1");

        String url = "http://zzsms365.com/v2sms.aspx";

        try {
            String rsp = HttpClientUtil.postWithForm(url, param, 30);
            // String rsp = HttpClientUtil.postWithForm("http://zzsms365.com/v2smsGBK.aspx", param, 30);

            // String curl = UrlParamUtil.join(param);
            // String rsp = HttpClientUtil.postWithAny(url, curl, null,MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8", 30);
            System.out.println(rsp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
