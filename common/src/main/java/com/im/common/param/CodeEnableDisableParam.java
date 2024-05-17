package com.im.common.param;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 启用/禁用
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
public class CodeEnableDisableParam {
    /**
     * 编码
     **/
    @NotBlank
    private String code;

    /**
     * 启用/禁用
     **/
    @NotNull
    private Boolean enable;
}
