package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.param.message.TIMTextElem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;

/**
 * 文本消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageElemText extends BaseEntity implements Serializable,TimMessageElem {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TIMTextElem.class, TimMessageElemText.class, false);

    public TimMessageElemText(TIMTextElem elem) {
        BEAN_COPIER.copy(elem, this, null);
    }

    private static final long serialVersionUID = -8096217834756957908L;
    /**
     * 消息内容
     */
    private String text;
}
