package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.param.message.TIMSoundElem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;

/**
 * 语音消息元素
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageElemSound extends BaseEntity implements Serializable,TimMessageElem {
    private static final long serialVersionUID = -4152646671931125464L;
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TIMSoundElem.class, TimMessageElemSound.class, false);

    public TimMessageElemSound(TIMSoundElem elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 语音下载地址，可通过该 URL 地址直接下载相应语音。
     */
    private String url;
    /**
     * 语音的唯一标识，客户端用于索引语音的键值
     */
    private String uuid;
    /**
     * 语音数据大小，单位：字节
     */
    private int size;
    /**
     * 语音时长，单位：秒
     */
    private int second;

}
