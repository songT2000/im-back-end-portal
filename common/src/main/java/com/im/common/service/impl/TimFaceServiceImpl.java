package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.common.cache.base.CacheProxy;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.tim.TimFace;
import com.im.common.entity.tim.TimFaceItem;
import com.im.common.mapper.TimFaceItemMapper;
import com.im.common.mapper.TimFaceMapper;
import com.im.common.param.TimFaceAddParam;
import com.im.common.param.TimFaceItemAddParam;
import com.im.common.param.TimFaceItemDeleteParam;
import com.im.common.response.RestResponse;
import com.im.common.service.TimFaceService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimFaceServiceImpl extends MyBatisPlusServiceImpl<TimFaceMapper, TimFace> implements TimFaceService {
    private TimFaceItemMapper timFaceItemMapper;
    private CacheProxy cacheProxy;

    @Autowired
    public void setTimFaceItemMapper(TimFaceItemMapper timFaceItemMapper) {
        this.timFaceItemMapper = timFaceItemMapper;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<TimFace> getAll() {
        List<TimFace> timFaces = list();
        if (CollectionUtil.isNotEmpty(timFaces)) {
            List<TimFaceItem> timFaceItems = timFaceItemMapper.selectList(new QueryWrapper<>());
            for (TimFace timFace : timFaces) {
                List<TimFaceItem> collect = timFaceItems.stream().filter(p -> p.getTimFaceId().equals(timFace.getId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collect)) {
                    timFace.setItems(collect);
                }
            }
        }
        return timFaces;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse delete(Long id) {
        removeById(id);
        LambdaQueryWrapper<TimFaceItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimFaceItem::getTimFaceId, id);
        timFaceItemMapper.delete(queryWrapper);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_FACE);
        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse add(TimFaceAddParam param) {

        TimFace timFace = new TimFace();
        timFace.setFaceName(param.getFaceName());
        timFace.setChatPanelIcon(param.getChatPanelIcon());
        save(timFace);

        List<String> faceUrlList = param.getFaceUrlList();

        for (int i = 0; i < faceUrlList.size(); i++) {
            TimFaceItem faceItem = new TimFaceItem();
            faceItem.setFaceUrl(faceUrlList.get(i));
            faceItem.setTimFaceId(timFace.getId());
            faceItem.setFaceIndex(i + 1);
            timFaceItemMapper.insert(faceItem);
        }
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_FACE);
        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteItem(TimFaceItemDeleteParam param) {
        LambdaQueryWrapper<TimFaceItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimFaceItem::getTimFaceId, param.getTimFaceId())
                .orderByDesc(TimFaceItem::getFaceIndex);
        List<TimFaceItem> itemList = timFaceItemMapper.selectList(queryWrapper);
        Optional<TimFaceItem> any = itemList.stream().filter(p -> p.getFaceUrl().equals(param.getFaceUrl())).findAny();
        any.ifPresent(timFaceItem -> timFaceItemMapper.deleteById(timFaceItem.getId()));

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_FACE);
        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addItem(TimFaceItemAddParam param) {

        TimFaceItem faceItem = new TimFaceItem();
        faceItem.setFaceUrl(param.getFaceUrl());
        faceItem.setTimFaceId(param.getTimFaceId());
        faceItem.setFaceIndex(CommonConstant.INT_1);

        LambdaQueryWrapper<TimFaceItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimFaceItem::getTimFaceId, param.getTimFaceId())
                .orderByDesc(TimFaceItem::getFaceIndex);
        List<TimFaceItem> itemList = timFaceItemMapper.selectList(queryWrapper);

        if (CollectionUtil.isNotEmpty(itemList)) {
            faceItem.setFaceIndex(itemList.get(0).getFaceIndex() + 1);
        }
        timFaceItemMapper.insert(faceItem);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.TIM_FACE);
        return RestResponse.OK;
    }
}
