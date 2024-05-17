package com.im.common.util.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.mybatis.page.PageVO;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * 继承mybatis-plus服务父类，加入自定义方法
 *
 * @author Barry
 * @date 2018/6/25
 */
public interface MyBatisPlusService<T> extends IService<T> {
    /**
     * 通过ID获取未删除的对象，匹配id=id和is_deleted=false数据
     *
     * @param id ID
     * @return
     */
    T getByIdNotDeleted(Serializable id);

    /**
     * 通过ID获取已删除的对象，匹配id=id和is_deleted=true数据
     *
     * @param id ID
     * @return
     */
    T getByIdDeleted(Serializable id);

    /**
     * 通过ID获取未删除的对象，匹配id=id和is_enabled=true数据
     *
     * @param id ID
     * @return
     */
    T getByIdEnabled(Serializable id);

    /**
     * 通过ID获取已删除的对象，匹配id=id和is_enabled=false数据
     *
     * @param id ID
     * @return
     */
    T getByIdDisabled(Serializable id);

    /**
     * 通过ID获取未删除的对象是否存在，匹配id=id和is_deleted=false数据
     *
     * @param id ID
     * @return
     */
    boolean isExistByIdNotDeleted(Serializable id);

    /**
     * 通过ID获取对象是否存在，匹配id=id数据
     *
     * @param id ID
     * @return
     */
    boolean isExistById(Serializable id);

    /**
     * 分页查询
     *
     * @param pageParam    分页参数
     * @param wrapperParam 组装wrapper时的传入参数
     * @return IPage
     */
    PageVO<T> page(AbstractPageParam<T> pageParam, Object wrapperParam);

    /**
     * 分页查询，返回自定义VO对象，仅支持单表
     * 当pageParam.getWrapper返回空时，不会进行查询
     *
     * @param pageParam 分页对象
     * @param mapper    将对象转换为另一个对象
     * @return 一定会返回对象，不会返回null
     */
    default <R> PageVO<R> pageVO(AbstractPageParam<T> pageParam, Function<T, R> mapper) {
        return pageVO(pageParam, null, mapper);
    }

    /**
     * 分页查询，返回自定义VO对象，仅支持单表
     * 当pageParam.getWrapper返回空时，不会进行查询
     *
     * @param pageParam    分页对象
     * @param wrapperParam 组装wrapper时的传入参数
     * @param mapper       将对象转换为另一个对象
     * @return 一定会返回对象，不会返回null
     */
    <R> PageVO<R> pageVO(AbstractPageParam<T> pageParam, Object wrapperParam, Function<T, R> mapper);

    /**
     * 分页查询，返回自定义VO对象，仅支持单表
     * wrapper为空时，不会进行查询
     * 参数pageParam.getWrapper会失效，会使用参数wrapper作为替代
     * 子类可以先调用pageParam.getWrapper获取wrapper后再添加想要的条件，再作为该方法参数
     *
     * @param pageParam 分页对象
     * @param wrapper   查询条件
     * @param mapper    将对象转换为另一个对象
     * @return 一定会返回对象，不会返回null
     */
    <R> PageVO<R> pageVoByWrapper(AbstractPageParam<T> pageParam, Wrapper<T> wrapper, Function<T, R> mapper);

    /**
     * <p>
     * 查询列表
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param limit        最多查询条数
     * @return
     */
    List<T> list(LambdaQueryWrapper<T> queryWrapper, int limit);


    /**
     * 查询未删除的列表，匹配is_deleted=false数据
     *
     * @return
     */
    List<T> listByNotDeleted();

    /**
     * 匹配is_enabled=true数据
     *
     * @return List
     */
    List<T> listByEnabled();

    /**
     * 匹配is_enabled=false数据
     *
     * @return List
     */
    List<T> listByDisabled();

    /**
     * 查询所有数据，并转为vo
     *
     * @param mapper 转换映射
     * @param <R>
     * @return List
     */
    <R> List<R> listVO(Function<T, R> mapper);

    /**
     * 查询所有数据，并转为vo
     *
     * @param queryParam 查询条件
     * @param mapper     转换映射
     * @param <R>
     * @return List
     */
    <R> List<R> listVO(Wrapper<T> queryParam, Function<T, R> mapper);

    /**
     * 匹配is_deleted=false数据
     *
     * @param mapper 转换映射
     * @param <R>
     * @return List
     */
    <R> List<R> listVoByNotDeleted(Function<T, R> mapper);

    /**
     * 匹配is_enabled=true数据
     *
     * @param mapper 转换映射
     * @param <R>
     * @return List
     */
    <R> List<R> listVoByEnabled(Function<T, R> mapper);

    /**
     * 匹配is_enabled=true数据
     *
     * @param mapper 转换映射
     * @param <R>
     * @return List
     */
    <R> List<R> listVoByDisabled(Function<T, R> mapper);

    /**
     * 快速批量插入，这个方法比saveBatch要快一点点，测试是10万条账变，saveBatch要10秒左右，fastSaveBatch是9秒
     * <p/>
     * 但有个缺陷，如果个别字段在 entity 里为 null 但是数据库中有配置默认值, insert 后数据库字段是为 null 而不是默认值
     * <p/>
     * 如果要调用这个方法，要确保所有字段都设置值
     * <p/>
     * 如果要追求更高的性能，请使用JdbcTemplate的batchUpdate，但要注意batchSize
     * 注意把mysql的max_allowed_packet调大,set global max_allowed_packet = 1073741824;（这是1G，这只是临时调，重启后会失效，也要在配置文件里调）
     * 或者把batchSize调小一点
     * <p/>
     * 详见{@link InsertBatchSomeColumn}
     *
     * @param list
     * @return
     */
    default boolean fastSaveBatch(List<T> list) {
        return fastSaveBatch(list, DEFAULT_BATCH_SIZE);
    }

    /**
     * 快速批量插入，这个方法比saveBatch要快一点点，测试是10万条账变，saveBatch要10秒左右，fastSaveBatch是9秒
     * <p/>
     * 但有个缺陷，如果个别字段在 entity 里为 null 但是数据库中有配置默认值, insert 后数据库字段是为 null 而不是默认值
     * <p/>
     * 如果要调用这个方法，要确保所有字段都设置值
     * <p/>
     * 如果要追求更高的性能，请使用JdbcTemplate的batchUpdate，但要注意batchSize
     * 注意把mysql的max_allowed_packet调大,set global max_allowed_packet = 1073741824;（这是1G，这只是临时调，重启后会失效，也要在配置文件里调）
     * 或者把batchSize调小一点
     * <p/>
     * 详见{@link InsertBatchSomeColumn}
     *
     * @param list
     * @param batchSize
     * @return
     */
    boolean fastSaveBatch(List<T> list, int batchSize);
}
