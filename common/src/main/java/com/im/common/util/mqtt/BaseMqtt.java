package com.im.common.util.mqtt;

import com.im.common.cache.sysconfig.bo.MqttConfigBO;
import com.im.common.response.RestResponse;

import java.util.List;

/**
 * 抽象Mqtt能力
 *
 * @author Barry
 * @date 2021-03-01
 */
public interface BaseMqtt {
    /**
     * 推送主题消息，推送到某个主题下所有订阅了的客户端
     *
     * @param config  MQTT配置
     * @param content 发送内容
     * @return
     * @throws Exception
     */
    RestResponse publish(MqttConfigBO config, MqttContent content) throws Exception;

    /**
     * 批量推送主题消息，推送到某个主题下所有订阅了的客户端
     *
     * @param config      MQTT配置
     * @param contentList 发送内容
     * @return
     * @throws Exception
     */
    RestResponse publish(MqttConfigBO config, List<MqttContent> contentList) throws Exception;

    /**
     * 推送P2P消息，推送到某个指定某户
     *
     * @param config  MQTT配置
     * @param content 发送内容
     * @return
     * @throws Exception
     */
    RestResponse publishToUser(MqttConfigBO config, MqttP2pContent content) throws Exception;

    /**
     * 批量推送P2P消息，推送到某个指定某户
     *
     * @param config      MQTT配置
     * @param contentList 发送内容
     * @return
     * @throws Exception
     */
    RestResponse publishToUser(MqttConfigBO config, List<MqttP2pContent> contentList) throws Exception;
}
