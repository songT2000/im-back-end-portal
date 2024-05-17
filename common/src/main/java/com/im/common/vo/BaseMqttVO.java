package com.im.common.vo;

import com.im.common.cache.sysconfig.bo.MqttMessageTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT VO
 *
 * @author Barry
 * @date 2019/11/19
 */
@Data
@NoArgsConstructor
@ApiModel
public abstract class BaseMqttVO<T> {
    @ApiModelProperty(value = "消息类型", position = 1)
    protected MqttMessageTypeEnum messageType;

    @ApiModelProperty(value = "消息内容", position = 2)
    protected T data;
}
