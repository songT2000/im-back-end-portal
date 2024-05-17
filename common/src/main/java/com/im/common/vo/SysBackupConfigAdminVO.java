package com.im.common.vo;

import com.im.common.entity.SysBackupConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 数据备份配置表VO
 *
 * @author Barry
 * @date 2020-01-04
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysBackupConfigAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(SysBackupConfig.class, SysBackupConfigAdminVO.class, false);

    public SysBackupConfigAdminVO(SysBackupConfig sysBackupConfig) {
        BEAN_COPIER.copy(sysBackupConfig, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "实时数据表名", position = 2)
    private String realTableName;

    @ApiModelProperty(value = "实时数据保留天数，注意彩票开奖号码是指条数，且彩票开奖号码只会处理已开奖的", position = 3)
    private Integer realKeepDay;

    @ApiModelProperty(value = "备份数据表名，为空表示没有备份，直接删除数据", position = 4)
    private String backupTableName;

    @ApiModelProperty(value = "备份数据保留天数，小于等于0不处理，注意彩票开奖号码是指条数，且彩票开奖号码只会处理已开奖的", position = 5)
    private Integer backupKeepDay;

    @ApiModelProperty(value = "是否启用", position = 6)
    private Boolean enabled;

    @ApiModelProperty(value = "备注", position = 7)
    private String remark;
}
