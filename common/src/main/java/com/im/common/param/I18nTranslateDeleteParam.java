package com.im.common.param;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 删除翻译
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
public class I18nTranslateDeleteParam {
    /**
     * 分组编码
     **/
    @NotBlank
    private String group;

    /**
     * key
     **/
    @NotBlank
    private String key;
}
