package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统公告
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class SysNotice extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2514707415218146184L;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告简介
     */
    private String simpleContent;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示
     **/
    @TableField("is_show")
    private Boolean showing;

    /**
     * 是否置顶
     **/
    @TableField("is_top")
    private Boolean top;

    /**
     * 语言编码
     **/
    private String languageCode;
}
