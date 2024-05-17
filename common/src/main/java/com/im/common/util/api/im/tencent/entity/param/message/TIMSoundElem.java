package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemLocation;
import com.im.common.entity.tim.TimMessageElemSound;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 语音消息元素
 *
 * <br>通过服务端集成的 Rest API 接口发送语音消息时，必须填入语音的 Url、UUID、Download_Flag 字段。需保证通过 Url 能下载到对应语音。
 * UUID 字段需填写全局唯一的 String 值，一般填入语音文件的 MD5 值。消息接收者可以通过 V2TIMSoundElem.getUUID() 拿到设置的 UUID 字段，
 * 业务 App 可以用这个字段做语音的区分。Download_Flag 字段必须填2。
 */
@Data
@NoArgsConstructor
public class TIMSoundElem extends TiMsgContent {
    private static final long serialVersionUID = 6349812621020986603L;
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageElemSound.class, TIMSoundElem.class, false);

    public TIMSoundElem(TimMessageElemSound elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 语音下载地址，可通过该 URL 地址直接下载相应语音。
     */
    @JSONField(name = "Url")
    private String url;
    /**
     * 语音的唯一标识，客户端用于索引语音的键值
     */
    @JSONField(name = "UUID")
    private String uuid;
    /**
     * 语音数据大小，单位：字节
     */
    @JSONField(name = "Size")
    private int size;
    /**
     * 语音时长，单位：秒
     */
    @JSONField(name = "Second")
    private int second;
    /**
     * 语音下载方式标记。目前 Download_Flag 取值只能为2，表示可通过Url字段值的 URL 地址直接下载语音。
     */
    @JSONField(name = "Download_Flag")
    private int downloadFlag = 2;

}
