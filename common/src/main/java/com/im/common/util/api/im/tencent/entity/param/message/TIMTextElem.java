package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemSound;
import com.im.common.entity.tim.TimMessageElemText;
import com.im.common.util.fastjson.EncryptSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 文本消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TIMTextElem extends TiMsgContent {

    private static final long serialVersionUID = 6846114123329813495L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageElemText.class, TIMTextElem.class, false);

    public TIMTextElem(TimMessageElemText elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 消息内容
     */
    @JSONField(name = "Text",serializeUsing = EncryptSerializer.class)
    private String text;
}
