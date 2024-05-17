package com.im.common.util.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.mybatis.page.PageVO;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


/**
 * 继承mybatis-plus服务父类，加入自定义方法
 *
 * @author Barry
 * @date 2018/6/25
 */
public class MyBatisPlusServiceImpl<M extends MyBatisPlusMapper<T>, T> extends ServiceImpl<M, T> implements MyBatisPlusService<T> {
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public T getByIdNotDeleted(Serializable id) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("is_deleted", false);

        return getOne(wrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public T getByIdDeleted(Serializable id) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("is_deleted", true);

        return getOne(wrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public T getByIdEnabled(Serializable id) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("is_enabled", true);

        return getOne(wrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public T getByIdDisabled(Serializable id) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("is_enabled", false);

        return getOne(wrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isExistByIdNotDeleted(Serializable id) {
        T obj = getByIdNotDeleted(id);
        return Optional.ofNullable(obj).isPresent();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isExistById(Serializable id) {
        T obj = getById(id);
        return Optional.ofNullable(obj).isPresent();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PageVO<T> page(AbstractPageParam<T> pageParam, Object wrapperParam) {
        // 分页条件
        Page<T> page = pageParam.toPage();

        // 查询条件
        Wrapper<T> wrapper = pageParam.toQueryWrapper(wrapperParam);

        // 查询分页
        IPage<T> resultPage = page(page, wrapper);

        // 转换分页查询结果
        PageVO<T> pageVO = new PageVO<>(resultPage);

        return pageVO;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <R> PageVO<R> pageVO(AbstractPageParam<T> pageParam, Object wrapperParam, Function<T, R> mapper) {
        // 获取子类定义的查询条件
        Wrapper<T> wrapper = pageParam.toQueryWrapper(wrapperParam);

        // 查询分页
        return pageVoByWrapper(pageParam, wrapper, mapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <R> PageVO<R> pageVoByWrapper(AbstractPageParam<T> pageParam, Wrapper<T> wrapper, Function<T, R> mapper) {
        if (wrapper == null) {
            return new PageVO<>(pageParam);
        }

        // 分页查询条件
        Page<T> queryPage = pageParam.toPage();

        // 查询分页数据
        IPage<T> resultPage = page(queryPage, wrapper);

        // 转换分页查询结果
        PageVO<R> pageVO = new PageVO<>(resultPage, mapper);

        return pageVO;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<T> list(LambdaQueryWrapper<T> queryWrapper, int limit) {
        if (limit <= 0) {
            return new ArrayList<>();
        }

        queryWrapper.last("limit " + limit);
        return list(queryWrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<T> listByNotDeleted() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", false);

        return list(wrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<T> listByEnabled() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("is_enabled", true);

        return list(wrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<T> listByDisabled() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("is_enabled", false);

        return list(wrapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <R> List<R> listVO(Function<T, R> mapper) {
        List<T> list = list();
        return CollectionUtil.toList(list, mapper);
    }

    @Override
    public <R> List<R> listVO(Wrapper<T> queryParam, Function<T, R> mapper) {
        if (queryParam == null) {
            return new ArrayList<>();
        }
        List<T> list = list(queryParam);
        return CollectionUtil.toList(list, mapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <R> List<R> listVoByNotDeleted(Function<T, R> mapper) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", false);

        List<T> list = list(wrapper);
        return CollectionUtil.toList(list, mapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <R> List<R> listVoByEnabled(Function<T, R> mapper) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("is_enabled", true);

        List<T> list = list(wrapper);
        return CollectionUtil.toList(list, mapper);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <R> List<R> listVoByDisabled(Function<T, R> mapper) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("is_enabled", false);

        List<T> list = list(wrapper);
        return CollectionUtil.toList(list, mapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean fastSaveBatch(List<T> list, int batchSize) {
        if (CollectionUtil.isEmpty(list)) {
            return true;
        }

        if (list.size() <= batchSize) {
            return SqlHelper.retBool(baseMapper.insertBatchSomeColumn(list));
        }

        for (int fromIdx = 0, endIdx = batchSize; ; fromIdx += batchSize, endIdx += batchSize) {
            if (endIdx > list.size()) {
                endIdx = list.size();
            }
            baseMapper.insertBatchSomeColumn(list.subList(fromIdx, endIdx));
            if (endIdx == list.size()) {
                return true;
            }
        }
    }
}
