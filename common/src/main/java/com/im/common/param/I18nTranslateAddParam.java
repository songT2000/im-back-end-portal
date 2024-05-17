package com.im.common.param;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 编辑翻译
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
public class I18nTranslateAddParam {
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

    @NotNull
    @Size(min = 1, max = 50)
    @Valid
    private List<Data> data;

    @lombok.Data
    @NoArgsConstructor
    public static class Data {
        /**
         * 国际化语种编码,sys_language_config表里的code值
         **/
        @NotBlank
        private String languageCode;

        /**
         * 翻译
         */
        @NotBlank
        private String value;
    }
}
