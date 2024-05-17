package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.AdminConfigBO;
import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 后台配置
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
public class AdminConfigAdminVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(AdminConfigBO.class, AdminConfigAdminVO.class, false);

    public AdminConfigAdminVO(AdminConfigBO adminConfig) {
        BEAN_COPIER.copy(adminConfig, this, null);
    }

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
}
