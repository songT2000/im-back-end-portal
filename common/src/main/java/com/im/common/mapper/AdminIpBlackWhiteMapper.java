package com.im.common.mapper;

import com.im.common.entity.AdminIpBlackWhite;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;


/**
 * 后台黑白名单
 *
 * @author max.stark
 */
@Repository
public interface AdminIpBlackWhiteMapper extends MyBatisPlusMapper<AdminIpBlackWhite> {
}
