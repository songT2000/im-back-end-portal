package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台配置
 * <p>
 * 请注意，如果在这里新增字段，必须在AdminConfigResolver#getDefault()中加入相应的默认值
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
public class AdminConfigBO extends BaseSysConfigBO {
    /**
     * 后台管理系统名称，显示在左上角和浏览器的名称
     **/
    private String name;

    /**
     * 后台管理系统LOGO，显示在左上角，文件必须放在asserts目录下
     **/
    private String logo;

    /**
     * 后台管理系统Favicon，显示在浏览器页签里，文件必须放在asserts目录下
     **/
    private String favicon;

    /**
     * 登录密码错误次数后将禁止登录
     */
    private Integer loginPwdErrorTimes;

    /**
     * 免登录分钟，一次登录，后续多久时间内无需要再次登录，30~99999999
     **/
    private Long loginExpireMinutes;
}
