package com.im.common.mapper;

import com.im.common.entity.UserGroupUser;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import com.im.common.vo.UserGroupAdminVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 用户组内用户 Mapper 接口
 *
 * @author Barry
 * @date 2021-04-11
 */
@Repository
public interface UserGroupUserMapper extends MyBatisPlusMapper<UserGroupUser> {
    /**
     * 统计每个组有多少用户
     *
     * @param groupIds 不能为空
     * @return
     */
    List<UserGroupAdminVO> sumCount(@Param("groupIds") Set<Long> groupIds);
}
