package com.im.common.vo;

import com.im.common.entity.SensitiveWord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

@Data
@NoArgsConstructor
@ApiModel
public class SensitiveWordVo {

    private static final BeanCopier BEAN_COPIER = BeanCopier.create(SensitiveWord.class, SensitiveWordVo.class, false);
    /**
     * 敏感词
     */
    @ApiModelProperty(value = "敏感词")
    private String word;

    public SensitiveWordVo(SensitiveWord e) {
        BEAN_COPIER.copy(e, this, null);
    }


}
