package com.im.common.util.api.sms.aliyun;

import com.alibaba.fastjson.JSON;
import com.im.common.util.fastjson.FastJsonConfigUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.CachingConfigurerSupport;

/**
 * @author Barry
 * @date 2022-02-10
 */
@Data
@NoArgsConstructor
public class AliyunSmsConfig {
    /**
     * 接口地址，在阿里云控制台获取
     */
    private String url;

    /**
     * 密钥ID，在阿里云控制台获取
     */
    private String accessKeyId;

    /**
     * 密钥，在阿里云控制台获取
     */
    private String accessKeySecret;

    public static void main(String[] args) {
        FastJsonConfigUtil.configFastJson();

        AliyunSmsConfig config = new AliyunSmsConfig();
        config.setUrl("https://dysmsapi.ap-southeast-1.aliyuncs.com");
        config.setAccessKeyId("LTAI5tPGY4U7PJDKkUtUt28N");
        config.setAccessKeySecret("eP9BJuPBpOYXWveuvvTErbKHXDTP4w");
        System.out.println(JSON.toJSONString(config));
    }
}
