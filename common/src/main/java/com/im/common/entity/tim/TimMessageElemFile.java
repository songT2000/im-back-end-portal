package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.param.message.TIMFileElem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;

/**
 * 文件消息元素
 */
@Data
@NoArgsConstructor
public class TimMessageElemFile extends BaseEntity implements Serializable,TimMessageElem {

    private static final long serialVersionUID = -3962618343914234291L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TIMFileElem.class, TimMessageElemFile.class, false);

    public TimMessageElemFile(TIMFileElem elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 文件下载地址，可通过该 URL 地址直接下载相应文件
     */
    private String url;
    /**
     * 文件的唯一标识，客户端用于索引文件的键值
     */
    private String uuid;
    /**
     * 文件数据大小，单位：字节
     */
    private Integer fileSize;
    /**
     * 文件名称
     */
    private String fileName;
}
