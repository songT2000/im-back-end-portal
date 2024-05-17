package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.SysBackupConfig;
import com.im.common.util.mybatis.page.AbstractPageParam;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据备份表分页参数
 *
 * @author Barry
 * @date 2020-01-04
 */
@Data
@NoArgsConstructor
public class SysBackupConfigPageParam extends AbstractPageParam<SysBackupConfig> {
    @Override
    public Wrapper<SysBackupConfig> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<SysBackupConfig> wrapper = new LambdaQueryWrapper<>();

        wrapper.orderByDesc(SysBackupConfig::getId);

        return wrapper;
    }
}
