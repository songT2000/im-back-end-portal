package com.im.common.entity.tim;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.param.message.TIMCustomElem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;

/**
 * 自定义消息元素
 */
@Data
@NoArgsConstructor
public class TimMessageElemCustom extends BaseEntity implements Serializable,TimMessageElem {
    private static final long serialVersionUID = 991016529538578171L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TIMCustomElem.class, TimMessageElemCustom.class, false);

    public TimMessageElemCustom(TIMCustomElem elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 自定义消息数据。
     */
    @TableField(value = "custom_data")
    private String data;
    /**
     * 自定义消息描述信息。
     */
    @TableField(value = "description")
    private String description;
    /**
     * 扩展字段。
     */
    private String ext;
    /**
     * 自定义 APNs 推送铃音
     */
    private String sound;
}
