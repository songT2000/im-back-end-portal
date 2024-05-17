package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报表配置
 * <p>
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
public class ReportConfigBO extends BaseSysConfigBO {
    /**
     * 前台用户最多可见近几天数据，为空或小于等于0不限制
     **/
    private Long portalMaxQueryPastDay;

    /**
     * 报表偏移时间，格式00:00:00表示0点到0点为1天，为空默认0点到0点
     **/
    private String offsetTime;
}
