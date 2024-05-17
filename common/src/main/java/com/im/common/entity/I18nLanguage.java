package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统国际化语种配置表
 *
 * @author Barry
 * @date 2019-10-22
 */
@Data
@NoArgsConstructor
public class I18nLanguage extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = 1632883186099543732L;

    /**
     * 语言名称，各国以各国翻译显示
     **/
    private String name;

    /**
     * 国际化语言编码
     */
    private String code;

    /**
     * 排序号
     */
    private Integer sort;
}
