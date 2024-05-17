package com.im.common.util.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * mapper 父类，放一些公共的方法，注意这个类不要让 mp 扫描到！！
 *
 * @author Barry
 * @date 2018/10/4
 */
public interface MyBatisPlusMapper<T> extends BaseMapper<T> {
    /**
     * 全量插入,等价于insert
     * <p/>
     * 但有个缺陷，如果个别字段在 entity 里为 null 但是数据库中有配置默认值, insert 后数据库字段是为 null 而不是默认值
     * {@link InsertBatchSomeColumn}
     *
     * @param entityList
     * @return
     */
    int insertBatchSomeColumn(List<T> entityList);
}
