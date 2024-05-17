package com.im.common.entity.tim;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.param.message.TIMLocationElem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 地理位置消息元素
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageElemLocation extends BaseEntity implements Serializable,TimMessageElem {
    private static final long serialVersionUID = -6144216313468782925L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TIMLocationElem.class, TimMessageElemLocation.class, false);

    public TimMessageElemLocation(TIMLocationElem elem) {
        BEAN_COPIER.copy(elem, this, null);
    }

    /**
     * 地理位置描述信息
     */
    @TableField(value = "description")
    private String desc;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 经度
     */
    private BigDecimal longitude;

}
