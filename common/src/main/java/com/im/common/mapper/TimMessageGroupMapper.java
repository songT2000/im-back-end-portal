package com.im.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.param.TimC2cMessagePortalPageParam;
import com.im.common.param.TimGroupMessagePortalPageParam;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimMessageGroupMapper extends MyBatisPlusMapper<TimMessageGroup> {

    /**
     * 前台查询群聊记录
     * <p>
     * 由于mybatis-plus多表查询时不能返回page对象，这里只能返回list，查询成功后，mybatis-plus会将结果回写到page参数中
     *
     * @param page  分页对象，mybatis-plus会自动将分页的结果回写到该对象中
     * @param param 查询参数
     * @return 已分页的列表
     */
    List<TimMessageGroup> pageVOForPortal(Page page, @Param("param") TimGroupMessagePortalPageParam param);
}
