package com.im.common.entity.enums;

import lombok.Getter;

@Getter
public enum MsgTypeEnum implements BaseEnum {
    TIMTextElem("TIMTextElem","文本消息"),
    TIMLocationElem("TIMLocationElem","地理位置消息"),
    TIMFaceElem("TIMFaceElem","表情消息"),
    TIMCustomElem("TIMCustomElem","自定义消息"),
    TIMSoundElem("TIMSoundElem","语音消息"),
    TIMImageElem("TIMImageElem","图像消息"),
    TIMFileElem("TIMFileElem","文件消息"),
    TIMVideoFileElem("TIMVideoFileElem","视频消息"),
    ;

    MsgTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    private String val;
    private String str;

    public static MsgTypeEnum getByValue(String value){
        MsgTypeEnum typeEnum = null;
        for (MsgTypeEnum tiMsgTypeEnum : MsgTypeEnum.values()) {
            if(tiMsgTypeEnum.getValue().equals(value)){
                typeEnum = tiMsgTypeEnum;
                break;
            }
        }
        return typeEnum;
    }

}
