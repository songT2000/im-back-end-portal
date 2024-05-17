package com.im.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.common.entity.tim.TimOperationStatistic;
import com.im.common.mapper.TimOperationStatisticMapper;
import com.im.common.response.RestResponse;
import com.im.common.service.TimOperationStatisticService;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.api.im.tencent.entity.result.operation.TiAppOperationItem;
import com.im.common.util.api.im.tencent.entity.result.operation.TiAppOperationResult;
import com.im.common.util.api.im.tencent.service.rest.TiAppOperationService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimOperationStatisticServiceImpl
        extends MyBatisPlusServiceImpl<TimOperationStatisticMapper, TimOperationStatistic>
        implements TimOperationStatisticService {

    private TiAppOperationService tiAppOperationService;

    @Autowired
    public void setTiAppOperationService(TiAppOperationService tiAppOperationService) {
        this.tiAppOperationService = tiAppOperationService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {
        RestResponse<TiAppOperationResult> restResponse = tiAppOperationService.queryOperation();
        if(!restResponse.getSuccess()){
            //获取失败
            return;
        }

        List<TiAppOperationItem> results = restResponse.getData().getResults();
        if(CollUtil.isNotEmpty(results)){
            List<TimOperationStatistic> operationStatistics = results.stream().map(TimOperationStatistic::new).
                    sorted(Comparator.comparing(TimOperationStatistic::getStatisticDate)).collect(Collectors.toList());
            //删除近30天的统计数据
            LambdaQueryWrapper<TimOperationStatistic> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ge(TimOperationStatistic::getStatisticDate, operationStatistics.get(0).getStatisticDate());
            remove(queryWrapper);
            //保存最新数据
            saveBatch(operationStatistics);
        }
    }
}
