package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * 报表配置解析
 *
 * @author Barry
 * @date 2018/6/8
 */
@SysConfigResolverGroup(SysConfigGroupEnum.REPORT)
public class ReportConfigResolver implements SysConfigResolver<ReportConfigBO> {
    @Override
    public ReportConfigBO resolve(List<SysConfig> sysConfigs) {
        ReportConfigBO reportConfig = new ReportConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(reportConfig, sysConfig));

        return reportConfig;
    }

    @Override
    public ReportConfigBO getDefault() {
        return new ReportConfigBO();
    }

    private void resolveSingle(ReportConfigBO reportConfig, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "PORTAL_MAX_QUERY_PAST_DAY":
                reportConfig.setPortalMaxQueryPastDay(Convert.toLong(value, 3L));
                break;
            case "OFFSET_TIME":
                reportConfig.setOffsetTime(StrUtil.trim(value));
                break;
            default:
                break;
        }
    }
}
