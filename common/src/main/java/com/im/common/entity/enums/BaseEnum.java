package com.im.common.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 所有枚举继承此类
 *
 * @author Barry
 * @date 2020-06-10
 */
public interface BaseEnum extends IEnum<String> {
    /**
     * 实际值
     *
     * @return
     */
    String getVal();

    /**
     * 显示值
     *
     * @return
     */
    String getStr();

    /**
     * 实际值
     *
     * @return 实际值
     */
    @Override
    default String getValue() {
        return getVal();
    }
}
