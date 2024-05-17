package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 国际化表
 *
 * @author Barry
 * @date 2019-10-22
 */
@Data
@NoArgsConstructor
public class I18nTranslate extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6376901548603666038L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 分组编码
     **/
    @TableField("`group`")
    private String group;

    /**
     * key
     **/
    @TableField("`key`")
    private String key;

    /**
     * 国际化语种编码,sys_language_config表里的code值
     **/
    private String languageCode;

    /**
     * 翻译
     **/
    @TableField("`value`")
    private String value;
}
