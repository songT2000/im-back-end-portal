package com.im.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.entity.AdminUser;
import com.im.common.param.AdminUserPageParam;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 后台管理用户表 Mapper 接口
 *
 * @author Barry
 * @date 2019-11-06
 */
@Repository
public interface AdminUserMapper extends MyBatisPlusMapper<AdminUser> {
    /**
     * 查询所有管理员用户列表，可以指定只查哪些角色
     * <p>
     * 由于mybatis-plus多表查询时不能返回page对象，这里只能返回list，查询成功后，mybatis-plus会将结果回写到page参数中
     *
     * @param page    分页对象，mybatis-plus会自动将分页的结果回写到该对象中
     * @param param   查询参数
     * @param roleIds 如果为空该参数不生效，即是不过滤角色
     * @return 已分页的列表
     */
    List<AdminUser> pageLowerUsers(Page page,
                                   @Param("param") AdminUserPageParam param,
                                   @Param("roleIds") Collection<Long> roleIds);
}
