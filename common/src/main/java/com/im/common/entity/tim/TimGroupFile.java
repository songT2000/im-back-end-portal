package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 群文件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimGroupFile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6134295624199140650L;
    /**
     * 群组的ID
     */
    private String groupId;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Double size;
    /**
     * 文件后缀
     */
    private String postfix;
}
