package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemCustom;
import com.im.common.entity.tim.TimMessageElemFace;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 表情消息元素
 */
@Data
@NoArgsConstructor
public class TIMFaceElem extends TiMsgContent {
    private static final long serialVersionUID = -9127011808340431637L;
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageElemFace.class, TIMFaceElem.class, false);

    public TIMFaceElem(TimMessageElemFace elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 表情索引，用户自定义
     */
    @JSONField(name = "Index")
    private int index;
    /**
     * 额外数据
     */
    @JSONField(name = "Data")
    private String data;
}
