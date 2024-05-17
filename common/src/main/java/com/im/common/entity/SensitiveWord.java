package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 敏感词
 */
@Data
@NoArgsConstructor
public class SensitiveWord extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8410111023606757154L;

    /**
     * 敏感词
     **/
    private String word;

    public SensitiveWord(String word) {
        this.word = word;
    }
}
