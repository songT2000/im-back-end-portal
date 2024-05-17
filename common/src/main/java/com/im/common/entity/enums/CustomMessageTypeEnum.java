package com.im.common.entity.enums;

import lombok.Getter;

/**
 * 自定义消息类型
 */
@Getter
public enum CustomMessageTypeEnum implements BaseEnum {
    VIDEO("video", "视频消息"),
    IMAGE("image", "图像消息"),
    SOUND("sound", "语音消息"),
    FILE("file", "文件消息"),
    RED_PACKET_C2C("red_packet_c2c", "个人红包消息"),
    RED_PACKET_C2C_RECEIVE("red_packet_c2c_receive", "个人红包领取消息"),
    RED_PACKET_C2C_REFUND("red_packet_c2c_refund", "个人红包退回消息"),
    RED_PACKET_C2C_EXPIRED("red_packet_c2c_expired", "个人红包过期消息"),
    RED_PACKET_GROUP("red_packet_group", "群组红包消息"),
    RED_PACKET_GROUP_RECEIVE("red_packet_group_receive", "群组红包领取消息"),
    RED_PACKET_GROUP_EXPIRED("red_packet_group_expired", "群组红包过期消息"),
    TEXT_LINK("text_link", "文本链接消息"),
    GROUP_SETTING("group_setting", "群组设置变更"),
    GROUP_CREATE("group_create", "创建群组"),
    ;

    CustomMessageTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    private String val;
    private String str;

}
