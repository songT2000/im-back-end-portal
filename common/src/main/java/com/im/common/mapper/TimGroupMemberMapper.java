package com.im.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import com.im.common.vo.TimGroupMemberVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群组成员 Mapper 接口
 */
@Repository
public interface TimGroupMemberMapper extends MyBatisPlusMapper<TimGroupMember> {

    List<TimGroupMemberVO> pageVOForAdmin(Page page, @Param("groupId") String groupId, @Param("username") String username);

}
