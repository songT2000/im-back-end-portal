package com.im.common.mapper;

import com.im.common.entity.UserGroupApiRechargeConfig;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import com.im.common.vo.UserGroupAdminVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 组三方充值配置 Mapper 接口
 *
 * @author Barry
 * @date 2021-04-11
 */
@Repository
public interface UserGroupApiRechargeConfigMapper extends MyBatisPlusMapper<UserGroupApiRechargeConfig> {
    /**
     * 统计每个组有多少三方充值配置
     *
     * @param groupIds 不能为空
     * @return
     */
    List<UserGroupAdminVO> sumCount(@Param("groupIds") Set<Long> groupIds);
}
