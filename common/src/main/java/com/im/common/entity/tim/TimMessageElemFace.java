package com.im.common.entity.tim;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.param.message.TIMFaceElem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;

/**
 * 表情消息元素
 */
@Data
@NoArgsConstructor
public class TimMessageElemFace extends BaseEntity implements Serializable,TimMessageElem {
    private static final long serialVersionUID = -5005865291711148699L;
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TIMFaceElem.class, TimMessageElemFace.class, false);

    public TimMessageElemFace(TIMFaceElem elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 表情索引，用户自定义
     */
    @TableField("`index`")
    private int index;
    /**
     * 额外数据
     */
    @TableField("`data`")
    private String data;
}
