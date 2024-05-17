package com.im.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.PortalNavigatorClick;
import com.im.common.entity.PortalNavigatorStatistic;
import com.im.common.mapper.PortalNavigatorClickMapper;
import com.im.common.param.PortalNavigatorStatisticParam;
import com.im.common.service.PortalNavigatorClickService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.PortalNavigatorClickVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortalNavigatorClickServiceImpl
        extends MyBatisPlusServiceImpl<PortalNavigatorClickMapper, PortalNavigatorClick>
        implements PortalNavigatorClickService {

    private PortalUserCache portalUserCache;

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    public List<PortalNavigatorStatistic> queryStatistic(PortalNavigatorStatisticParam param) {
        return this.baseMapper.queryStatistic(param);
    }

    @Override
    public PageVO<PortalNavigatorClickVO> queryDetail(PortalNavigatorStatisticParam param) {
        Page<PortalNavigatorClick> page = param.toPage();
        List<PortalNavigatorClick> list = this.baseMapper.queryDetail(page,param);
        if (CollectionUtil.isEmpty(list)) {
            return new PageVO<>(param);
        }
        List<PortalNavigatorClick> records = CollectionUtil.distinctBy(list, PortalNavigatorClick::getUserId);
        List<PortalNavigatorClickVO> results = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(records)){
            for (PortalNavigatorClick portalNavigatorClick : records) {
                PortalNavigatorClickVO vo = new PortalNavigatorClickVO();
                vo.setAvatar(portalUserCache.getAvatarByIdFromLocal(portalNavigatorClick.getUserId()));
                vo.setNickname(portalUserCache.getNicknameByIdFromLocal(portalNavigatorClick.getUserId()));
                vo.setUsername(portalUserCache.getUsernameByIdFromLocal(portalNavigatorClick.getUserId()));
                vo.setCreateTime(portalNavigatorClick.getCreateTime());
                results.add(vo);
            }
        }
        // 手动封装分页
        return new PageVO<>(page,results);
    }


}
