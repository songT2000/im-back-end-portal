package com.im.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.entity.PortalUser;
import com.im.common.param.PortalUserPageAdminParam;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import com.im.common.vo.PortalUserAdminVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户表 Mapper 接口
 *
 * @author Barry
 * @date 2021-12-11
 */
@Repository
public interface PortalUserMapper extends MyBatisPlusMapper<PortalUser> {
    /**
     * 增减余额，可以扣成负数
     *
     * @param userId        用户ID
     * @param amount        金额，负数就是扣钱
     * @param allowToNegate 是否允许扣成负数
     * @return
     */
    boolean addBalance(@Param("userId") long userId,
                       @Param("amount") BigDecimal amount,
                       @Param("allowToNegate") boolean allowToNegate);

    /**
     * 查询所有管理员用户列表，可以指定只查哪些角色
     * <p>
     * 由于mybatis-plus多表查询时不能返回page对象，这里只能返回list，查询成功后，mybatis-plus会将结果回写到page参数中
     *
     * @param page  分页对象，mybatis-plus会自动将分页的结果回写到该对象中
     * @param param 查询参数
     * @return 已分页的列表
     */
    List<PortalUserAdminVO> pageVOForAdmin(Page page, @Param("param") PortalUserPageAdminParam param);
}
