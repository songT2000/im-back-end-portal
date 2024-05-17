package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据备份配置
 *
 * @author Barry
 * @date 2020-01-02
 */
@Data
@NoArgsConstructor
public class SysBackupConfig extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = -2830769173516835173L;

    /**
     * 实时表名
     */
    private String realTableName;

    /**
     * 备份表名，为空表示没有备份，直接删除数据
     */
    private String backupTableName;

    /**
     * 实时数据保留天数
     */
    private Integer realKeepDay;

    /**
     * 备份数据保留天数
     */
    private Integer backupKeepDay;

    /**
     * 备注
     */
    private String remark;
}
