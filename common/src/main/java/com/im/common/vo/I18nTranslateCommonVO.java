package com.im.common.vo;

import com.im.common.entity.I18nTranslate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 国际化表
 *
 * @author Barry
 * @date 2019-10-22
 */
@Data
@NoArgsConstructor
public class I18nTranslateCommonVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(I18nTranslate.class, I18nTranslateCommonVO.class, false);

    public I18nTranslateCommonVO(I18nTranslate config) {
        BEAN_COPIER.copy(config, this, null);
    }

    private Long id;
    private String group;
    private String key;
    private String languageCode;
    private String value;
}
