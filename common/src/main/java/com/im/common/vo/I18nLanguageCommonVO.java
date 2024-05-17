package com.im.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.I18nLanguage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 国际化语言国家编码Vo
 *
 * @author Barry
 * @date 2019-10-22
 */
@Data
@NoArgsConstructor
@ApiModel
public class I18nLanguageCommonVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(I18nLanguage.class, I18nLanguageCommonVO.class, false);

    public I18nLanguageCommonVO(I18nLanguage config) {
        BEAN_COPIER.copy(config, this, null);
    }

    /**
     * 备注
     **/
    @ApiModelProperty("名称")
    private String name;

    /**
     * 国际化语言编码-按照i18n国际化Local里的规则来定义值
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 是否启用true启用，false禁用
     **/
    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
