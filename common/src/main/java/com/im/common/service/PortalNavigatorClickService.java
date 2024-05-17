package com.im.common.service;

import com.im.common.entity.PortalNavigatorClick;
import com.im.common.entity.PortalNavigatorStatistic;
import com.im.common.param.PortalNavigatorStatisticParam;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.PortalNavigatorClickVO;

import java.util.List;

/**
 * 前台导航点击 服务类
 *
 * @author mozzie
 * @date 2022-04-08
 */
public interface PortalNavigatorClickService extends MyBatisPlusService<PortalNavigatorClick> {

    List<PortalNavigatorStatistic> queryStatistic(PortalNavigatorStatisticParam param);

    PageVO<PortalNavigatorClickVO> queryDetail(PortalNavigatorStatisticParam param);

}
