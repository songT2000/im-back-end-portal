package com.im.common.util.mqtt;

import com.im.common.cache.sysconfig.bo.MqttConfigBO;
import com.im.common.cache.sysconfig.bo.MqttTypeEnum;
import com.im.common.response.RestResponse;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * MQTT工具类，用来向用户推送注单中奖信息/推送最新开奖号码/聊天室等
 *
 * @author Barry
 * @date 2021-03-01
 */
public class MqttUtil {
    /**
     * 推送主题消息，推送到某个主题下所有订阅了的客户端
     *
     * @param config  配置
     * @param content 内容
     * @return
     * @throws Exception
     */
    public static RestResponse publish(MqttConfigBO config, MqttContent content) throws Exception {
        BaseMqtt client = getClient(config);
        return client.publish(config, content);
    }

    /**
     * 批量推送主题消息，推送到某个主题下所有订阅了的客户端
     *
     * @param config      配置
     * @param contentList 内容
     * @return
     * @throws Exception
     */
    public static RestResponse publish(MqttConfigBO config, List<MqttContent> contentList) throws Exception {
        BaseMqtt client = getClient(config);
        return client.publish(config, contentList);
    }

    /**
     * 推送P2P消息，推送到某个指定某户
     *
     * @param config  配置
     * @param content 内容
     * @return
     * @throws Exception
     */
    public static RestResponse publishToUser(MqttConfigBO config, MqttP2pContent content) throws Exception {
        BaseMqtt client = getClient(config);
        return client.publishToUser(config, content);
    }

    /**
     * 推送P2P消息，推送到某个指定某户
     *
     * @param config      配置
     * @param contentList 内容
     * @return
     * @throws Exception
     */
    public static RestResponse publishToUser(MqttConfigBO config, List<MqttP2pContent> contentList) throws Exception {
        BaseMqtt client = getClient(config);
        return client.publishToUser(config, contentList);
    }

    private static BaseMqtt getClient(MqttConfigBO config) {
        if (config.getMqttType() == MqttTypeEnum.ALIYUN) {
            return new AliyunMqtt();
        }
        return null;
    }

    /**
     * 父订阅主题
     *
     * @param config
     * @return
     */
    public static String buildParentTopic(MqttConfigBO config) {
        return StrUtil.format("{}/{}", config.getAliyunParentTopic(), config.getAliyunChildTopic());
    }

    public static String buildUserClientId(MqttConfigBO config, String username) {
        return StrUtil.format("{}@@@{}_{}_{}", config.getAliyunGroupId(), config.getAliyunParentTopic(), config.getAliyunChildTopic(), username);
    }

    // /**
    //  * 市场详情 {aliyunParentTopic}/{aliyunChildTopic}/market/detail
    //  *
    //  * @param config
    //  * @return
    //  */
    // public static String buildMarketDetailTopic(MqttConfigBO config) {
    //     return StrUtil.format("{}/{}", buildParentTopic(config), "market/detail");
    // }
    //
    // /**
    //  * 市场K线，{aliyunParentTopic}/{aliyunChildTopic}/market/kline/{vcCode}/{period}
    //  *
    //  * @param config
    //  * @return
    //  */
    // public static String buildMarketKlineTopic(MqttConfigBO config, String vcCode, KlinePeriodEnum period) {
    //     return StrUtil.format("{}/{}/{}/{}", buildParentTopic(config), "market/kline", vcCode, period.getVal());
    // }
    //
    // /**
    //  * 成交明细，{aliyunParentTopic}/{aliyunChildTopic}/market/trade/{vcCode}
    //  *
    //  * @param config
    //  * @return
    //  */
    // public static String buildMarketTradeTopic(MqttConfigBO config, String vcCode) {
    //     return StrUtil.format("{}/{}/{}", buildParentTopic(config), "market/trade", vcCode);
    // }
}
