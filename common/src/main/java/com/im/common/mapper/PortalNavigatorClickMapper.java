package com.im.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.entity.PortalNavigatorClick;
import com.im.common.entity.PortalNavigatorStatistic;
import com.im.common.param.PortalNavigatorStatisticParam;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PortalNavigatorClickMapper extends MyBatisPlusMapper<PortalNavigatorClick> {

    List<PortalNavigatorStatistic> queryStatistic(PortalNavigatorStatisticParam param);

    List<PortalNavigatorClick> queryDetail(Page page,@Param("param") PortalNavigatorStatisticParam param);
}
