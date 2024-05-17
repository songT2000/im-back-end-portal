package com.im.common.util.api.sms.qyss;

import com.alibaba.fastjson.JSON;
import com.im.common.util.fastjson.FastJsonConfigUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Barry
 * @date 2022-02-10
 */
@Data
@NoArgsConstructor
public class QyssSmsConfig {
    /**
     * 接口地址，控制台获取
     */
    private String url;

    /**
     * 控制台获取
     */
    private String userId;

    /**
     * 控制台获取
     */
    private String userAccount;

    /**
     * 控制台获取
     */
    private String userPassword;

    public static void main(String[] args) {
        FastJsonConfigUtil.configFastJson();

        QyssSmsConfig config = new QyssSmsConfig();
        config.setUrl("http://zzsms365.com/v2sms.aspx");
        config.setUserId("3477");
        config.setUserAccount("2026749374");
        config.setUserPassword("2026749374");
        System.out.println(JSON.toJSONString(config));
    }
}
