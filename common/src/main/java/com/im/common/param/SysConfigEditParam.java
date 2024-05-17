package com.im.common.param;

import com.im.common.entity.enums.SysConfigGroupEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统配置更新参数
 *
 * @author Daniel
 * @date 2019/11/29
 */
@Data
@NoArgsConstructor
@ApiModel
public class  SysConfigEditParam {
    @NotNull
    @ApiModelProperty(value = "配置组", required = true)
    private SysConfigGroupEnum group;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty("谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌")
    private Integer googleCode;

    @NotNull
    @ApiModelProperty(value = "配置列表", required = true)
    private List<Config> list;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Config {
        @NotBlank
        @ApiModelProperty(value = "配置项", required = true)
        private String item;

        @ApiModelProperty(value = "配置值")
        private String value;
    }
}
