package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 数据备份配置 编辑参数
 *
 * @author Barry
 * @date 2020-01-04
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysBackupConfigEditParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @ApiModelProperty(value = "实时数据保留天数", required = true, position = 2)
    private Integer realKeepDay;

    @ApiModelProperty(value = "备份数据保留天数，小于等于0不处理，注意彩票开奖号码是指条数", position = 3)
    private Integer backupKeepDay;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 4)
    private Integer googleCode;
}
