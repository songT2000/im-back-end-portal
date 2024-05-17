package com.im.common.entity.enums;

public enum TiMsgTypeEnum implements BaseEnum {
    TIMTextElem("TIMTextElem", "文本消息"),
    TIMLocationElem("TIMLocationElem", "地理位置消息"),
    TIMFaceElem("TIMFaceElem", "表情消息"),
    TIMCustomElem("TIMCustomElem", "自定义消息，当接收方为 iOS 系统且应用处在后台时，此消息类型可携带除文本以外的字段到 APNs。一条组合消息中只能包含一个 TIMCustomElem 自定义消息元素"),
    TIMSoundElem("TIMSoundElem", "语音消息"),
    TIMImageElem("TIMImageElem", "图像消息"),
    TIMFileElem("TIMFileElem", "文件消息"),
    TIMVideoFileElem("TIMVideoFileElem", "视频消息"),
    ;

    private String val;
    private String str;

    TiMsgTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    @Override
    public String getVal() {
        return val;
    }

    @Override
    public String getStr() {
        return str;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
