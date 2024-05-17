package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemFile;
import com.im.common.entity.tim.TimMessageElemLocation;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;

/**
 * 地理位置消息元素
 */
@Data
public class TIMLocationElem extends TiMsgContent {

    private static final long serialVersionUID = 7951866372162711955L;
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageElemLocation.class, TIMLocationElem.class, false);

    public TIMLocationElem(TimMessageElemLocation elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 地理位置描述信息
     */
    @JSONField(name = "Desc")
    private String desc;
    /**
     * 纬度
     */
    @JSONField(name = "Latitude")
    private BigDecimal latitude;
    /**
     * 经度
     */
    @JSONField(name = "Longitude")
    private BigDecimal longitude;

}
