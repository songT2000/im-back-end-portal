package com.im.common.service;

import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.response.RestResponse;
import com.im.common.param.SysConfigEditParam;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

import java.util.List;

/**
 * 系统配置表 服务类
 *
 * @author Barry
 * @date 2018/6/8
 */
public interface SysConfigService extends MyBatisPlusService<SysConfig> {
    /**
     * 列出所有可编辑的配置
     *
     * @param advance 是否高级配置
     * @return List<SysConfigVO>
     */
    List<SysConfig> listEditable(boolean advance);

    /**
     * 更新系统配置
     *
     * @param sessionUser 当前登录用户
     * @param param    系统配置更新参数
     * @param advanced 是否高级配置
     * @return AdminRestResponse, OK表示成功，data没有数据
     */
    RestResponse edit(AdminSessionUser sessionUser, SysConfigEditParam param, boolean advanced);

    /**
     * 根据配置组和配置项获取配置
     *
     * @param group 配置组
     * @param item  配置项
     * @return SysConfig
     */
    SysConfig getByGroupAndItem(SysConfigGroupEnum group, String item);

    /**
     * 根据配置组和配置item更新配置
     *
     * @param group 配置组
     * @param item  配置item
     * @param value 更新值
     */
    void saveOrUpdateItem(SysConfigGroupEnum group, String item, String value);
}
