package com.im.common.vo;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.sysconfig.bo.MqttConfigBO;
import com.im.common.cache.sysconfig.bo.MqttTypeEnum;
import com.im.common.util.StrUtil;
import com.im.common.util.mqtt.AliyunMqtt;
import com.im.common.util.mqtt.MqttUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT配置，需要用户登录后根据配置来生成配置
 *
 * @author Barry
 * @date 2021-02-18
 */
@Data
@NoArgsConstructor
@ApiModel
public class MqttConfigUserVO {
    /**
     * @param config
     * @param sessionUser 当前登录用户
     */
    public MqttConfigUserVO(MqttConfigBO config, PortalSessionUser sessionUser) {

        if (config.getMqttType() == MqttTypeEnum.ALIYUN) {
            // 生成给客户端的用只读账号，也不用再去搞什么token了，麻烦得很

            String clientId = MqttUtil.buildUserClientId(config, sessionUser.getUsername());
            this.clientId = clientId;
            this.clientId = StrUtil.rsaPublicEncryptApiData(this.clientId);

            this.host = config.getAliyunInternetHost();
            this.host = StrUtil.rsaPublicEncryptApiData(this.host);

            this.username = StrUtil.format("Signature|{}|{}", config.getAliyunReadOnlyAccessKeyId(), config.getAliyunInstanceId());
            this.username = StrUtil.rsaPublicEncryptApiData(this.username);

            this.password = AliyunMqtt.macSignature(clientId, config.getAliyunReadOnlyAccessKeySecret());
            this.password = StrUtil.rsaPublicEncryptApiData(this.password);

            this.parentTopic = MqttUtil.buildParentTopic(config);
        }
    }

    public static void main(String[] args) {
        MqttConfigBO config = new MqttConfigBO();
        config.setMqttType(MqttTypeEnum.ALIYUN);
        config.setAliyunReadOnlyAccessKeyId("LTAI4G66xBKxNZbxyYhanoFo");
        config.setAliyunReadOnlyAccessKeySecret("qHuEOkiboqGnoEgAZEo5udqUoCUrcG");
        config.setAliyunInstanceId("postintl-sg-25u22im7e01");
        config.setAliyunGroupId("GID_qilin");
        config.setAliyunInternetHost("postintl-sg-25u22im7e01.mqtt.aliyuncs.com");
        // config.setAliyunVpcHost("postintl-sg-25u22im7e01-internal-vpc.mqtt.aliyuncs.com");
        config.setAliyunParentTopic("qilin");
        config.setAliyunChildTopic("test");

        PortalSessionUser sessionUser = new PortalSessionUser();
        sessionUser.setUsername("test2");

        MqttConfigUserVO configVO = new MqttConfigUserVO(config, sessionUser);
        System.out.println(JSON.toJSONString(configVO));
    }

    @ApiModelProperty(value = "客户端ID，服务器加密后返回，客户端需解密", position = 1)
    private String clientId;

    @ApiModelProperty(value = "接入点域名，服务器加密后返回，客户端需解密", position = 2)
    private String host;

    @ApiModelProperty(value = "连接用户名，服务器加密后返回，客户端需解密", position = 3)
    private String username;

    @ApiModelProperty(value = "连接密码，服务器加密后返回，客户端需解密", position = 4)
    private String password;

    @ApiModelProperty(value = "父主题，市场详情={parentTopic}/market/detail，市场K线={parentTopic}/market/kline/{vcCode}/{period}", position = 5)
    private String parentTopic;
}
