package com.im.common.util.mqtt;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.cache.sysconfig.bo.MqttConfigBO;
import com.im.common.response.RestResponse;
import com.im.common.util.ClockUtil;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 阿里云-微消息队列MQTT
 * <p>
 * Demo地址：https://code.aliyun.com/aliware_mqtt/mqtt-demo/tree/master
 *
 * @author Barry
 * @date 2021-03-01
 */
public class AliyunMqtt implements BaseMqtt {
    private static final Log LOG = LogFactory.get();

    /**
     * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
     * <p>
     * QoS（Quality of Service）指代消息传输的服务质量。它包括以下级别：
     * QoS0 代表最多分发一次
     * QoS1 代表至少达到一次
     * QoS2 代表仅分发一次
     */
    private static final int DEFAULT_QOS_LEVEL = 0;

    public static void main(String[] args) {
        // try {
        //     MqttConfigBO config = new MqttConfigBO();
        //     config.setMqttType(MqttTypeEnum.ALIYUN);
        //     config.setAliyunFullAccessAccessKeyId("LTAI5t7oWtmizexKTwvHbFPQ");
        //     config.setAliyunFullAccessAccessKeySecret("csVmG8rjpEhB4WRR90rKlHYx9DScAF");
        //     config.setAliyunInstanceId("mqtt-cn-i7m2cnm5f0a");
        //     config.setAliyunGroupId("GID_EWIN");
        //     config.setAliyunInternetHost("mqtt-cn-i7m2cnm5f0a.mqtt.aliyuncs.com");
        //     // config.setAliyunVpcHost("postintl-sg-25u22im7e01-internal-vpc.mqtt.aliyuncs.com");
        //     config.setAliyunParentTopic("MARKET");
        //     config.setAliyunChildTopic("test");
        //
        //     List<MqttContent> mqttContentList = new ArrayList<>();
        //     mqttContentList.add(new MqttContent(MqttUtil.buildMarketDetailTopic(config), "这是一条公共订阅消息"));
        //     new AliyunMqtt().publish(config, mqttContentList);
        //
        //     // new AliyunMqtt().publishToUser(config, new MqttP2pContent("test2", "这是一条P2P消息"));
        // } catch (Exception exception) {
        //     exception.printStackTrace();
        // }
    }

    @Override
    public RestResponse publish(MqttConfigBO config, MqttContent content) throws Exception {
        return publish(config, CollectionUtil.toList(content));
    }

    @Override
    public RestResponse publish(MqttConfigBO config, List<MqttContent> contentList) throws Exception {
        MqttClient mqttClient = null;

        try {
            // 创建连接
            mqttClient = buildMqttClientAndConnect(config);

            // 发送消息
            for (MqttContent content : contentList) {
                MqttMessage message = new MqttMessage(content.getMessageBytes());
                message.setQos(DEFAULT_QOS_LEVEL);

                mqttClient.publish(content.getTopic(), message);
            }
            return RestResponse.OK;
        } catch (Exception e) {
            LOG.error(e, "发送MQTT消息失败");
            throw e;
        } finally {
            try {
                if (mqttClient != null) {
                    mqttClient.disconnect();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    @Override
    public RestResponse publishToUser(MqttConfigBO config, MqttP2pContent content) throws Exception {
        return publishToUser(config, CollectionUtil.toList(content));
    }

    @Override
    public RestResponse publishToUser(MqttConfigBO config, List<MqttP2pContent> contentList) throws Exception {
        // 创建连接
        MqttClient mqttClient = null;

        try {
            mqttClient = buildMqttClientAndConnect(config);

            /**
             * MQ4IoT支持点对点消息，即如果发送方明确知道该消息只需要给特定的一个设备接收，且知道对端的 clientId，则可以直接发送点对点消息。
             * 点对点消息不需要经过订阅关系匹配，可以简化订阅方的逻辑。点对点消息的 topic 格式规范是  {{parentTopic}}/p2p/{{targetClientId}}
             */
            for (MqttP2pContent content : contentList) {
                final String p2pSendTopic = config.getAliyunParentTopic() + "/p2p/" + MqttUtil.buildUserClientId(config, content.getUsername());

                MqttMessage message = new MqttMessage(content.getMessageBytes());
                message.setQos(DEFAULT_QOS_LEVEL);

                mqttClient.publish(p2pSendTopic, message);
            }
            return RestResponse.OK;
        } catch (Exception e) {
            LOG.error(e, "发送MQTT消息失败");
            throw e;
        } finally {
            try {
                if (mqttClient != null) {
                    mqttClient.disconnect();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    private static MqttClient buildMqttClientAndConnect(MqttConfigBO config) throws Exception {
        // 服务器去连接MQTT也是一个客户端去连接，因此也要生成clientId，随便生成一个就好了，但不要跟用户名重复
        String clientId = MqttUtil.buildUserClientId(config, ClockUtil.nowMillis() + "");

        final MemoryPersistence memoryPersistence = new MemoryPersistence();

        /**
         * 客户端使用的协议和端口必须匹配，具体参考文档 https://help.aliyun.com/document_detail/44866.html?spm=a2c4g.11186623.6.552.25302386RcuYFB
         * 如果是 SSL 加密则设置ssl://endpoint:8883
         */
        // 如果配了VPC就用VPC去访问（服务器和MQTT服务器必须在同一VPC），如果没配就用公网接入点
        String host = StrUtil.isNotBlank(config.getAliyunVpcHost()) ? config.getAliyunVpcHost() : config.getAliyunInternetHost();
        final String serverUrl = "tcp://" + host + ":1883";
        // final String serverUrl = "ssl://" + config.getHost() + ":8883";
        final MqttClient mqttClient = new MqttClient(serverUrl, clientId, memoryPersistence);

        /**
         * 客户端设置好发送超时时间，防止无限阻塞
         */
        mqttClient.setTimeToWait(10 * 1000);

        MqttConnectOptions mqttConnectOptions = buildMqttConnectOptions(config, clientId);
        mqttClient.connect(mqttConnectOptions);

        return mqttClient;
    }

    /**
     * Signature 鉴权模式下构造方法
     *
     * @param config
     * @param clientId
     * @return
     * @throws Exception
     */
    private static MqttConnectOptions buildMqttConnectOptions(MqttConfigBO config, String clientId) {
        // 应用服务器，使用全权限账号
        String accessKey = config.getAliyunFullAccessAccessKeyId();
        String instanceId = config.getAliyunInstanceId();
        String secretKey = config.getAliyunFullAccessAccessKeySecret();

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName("Signature|" + accessKey + "|" + instanceId);
        mqttConnectOptions.setPassword(macSignature(clientId, secretKey).toCharArray());
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        mqttConnectOptions.setConnectionTimeout(10 * 1000);

        return mqttConnectOptions;
    }

    /**
     * @param text      要签名的文本
     * @param secretKey 阿里云MQ secretKey
     * @return 加密后的字符串
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static String macSignature(String text, String secretKey) {
        try {
            Charset charset = Charset.forName("UTF-8");
            String algorithm = "HmacSHA1";
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(secretKey.getBytes(charset), algorithm));
            byte[] bytes = mac.doFinal(text.getBytes(charset));
            return new String(Base64.encodeBase64(bytes), charset);
        } catch (Exception e) {
            LOG.error(e, "生成阿里云签名出错");
            return null;
        }
    }
}
