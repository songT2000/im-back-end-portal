package com.im.common.cache.sysconfig.resolver;

import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.MqttConfigBO;
import com.im.common.cache.sysconfig.bo.MqttTypeEnum;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.EnumUtil;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * MQTT微消息队列
 *
 * @author Barry
 * @date 2019/10/26
 */
@SysConfigResolverGroup(SysConfigGroupEnum.MQTT)
public class MqttConfigResolver implements SysConfigResolver<MqttConfigBO> {
    @Override
    public MqttConfigBO resolve(List<SysConfig> sysConfigs) {
        MqttConfigBO config = new MqttConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        return config;
    }

    @Override
    public MqttConfigBO getDefault() {
        return new MqttConfigBO();
    }

    private void resolveSingle(MqttConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "MQTT_TYPE":
                config.setMqttType(EnumUtil.valueOfIEnum(MqttTypeEnum.class, value));
                break;
            case "ALIYUN_FULL_ACCESS_ACCESS_KEY_ID":
                config.setAliyunFullAccessAccessKeyId(StrUtil.trim(value));
                break;
            case "ALIYUN_FULL_ACCESS_ACCESS_KEY_KEY_SECRET":
                config.setAliyunFullAccessAccessKeySecret(StrUtil.trim(value));
                break;
            case "ALIYUN_READ_ONLY_ACCESS_KEY_ID":
                config.setAliyunReadOnlyAccessKeyId(StrUtil.trim(value));
                break;
            case "ALIYUN_READ_ONLY_ACCESS_KEY_KEY_SECRET":
                config.setAliyunReadOnlyAccessKeySecret(StrUtil.trim(value));
                break;
            case "ALIYUN_INSTANCE_ID":
                config.setAliyunInstanceId(StrUtil.trim(value));
                break;
            case "ALIYUN_GROUP_ID":
                config.setAliyunGroupId(StrUtil.trim(value));
                break;
            case "ALIYUN_INTERNET_HOST":
                config.setAliyunInternetHost(StrUtil.trim(value));
                break;
            case "ALIYUN_VPC_HOST":
                config.setAliyunVpcHost(StrUtil.trim(value));
                break;
            case "ALIYUN_PARENT_TOPIC":
                config.setAliyunParentTopic(StrUtil.trim(value));
                break;
            case "ALIYUN_CHILD_TOPIC":
                config.setAliyunChildTopic(StrUtil.trim(value));
                break;
            default:
                break;
        }
    }
}
