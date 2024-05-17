package com.im.common.util.mybatis.page;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页参数父类，需要分页的参数类继承此类，已经做了参数校验
 *
 * @author Barry
 * @date 2018/6/17
 */
@Data
public abstract class AbstractPageParam<T> {
    /**
     * 当前页
     **/
    @NotNull(message = "RSP_MSG.PAGE_PARAM_ERROR#I18N")
    @Min(value = 1, message = "RSP_MSG.PAGE_PARAM_ERROR#I18N")
    @ApiModelProperty(value = "起始页", required = true)
    protected Long current = 1L;

    /**
     * 每页显示条数，默认 10
     **/
    @NotNull(message = "RSP_MSG.PAGE_PARAM_ERROR#I18N")
    @Min(value = 1, message = "RSP_MSG.PAGE_PARAM_ERROR#I18N")
    @Max(value = 100, message = "RSP_MSG.PAGE_PARAM_ERROR#I18N")
    @ApiModelProperty(value = "每页大小", required = true)
    protected Long size = 10L;

    /**
     * 构造查询条件,一定会有数据返回
     *
     * @return mybatis-plus分页查询条件
     */
    public Page<T> toPage() {
        Page<T> page = new Page(this.current, this.size);

        return page;
    }

    /**
     * 获取mybatis查询wrapper，子类实现该方法，返回null则不查询，返回其它则查询
     *
     * @param wrapperParam 封装wrapper时需要的参数
     * @return 返回null则不查询，返回其它则查询
     */
    public abstract Wrapper<T> toQueryWrapper(Object wrapperParam);

    /**
     * 转到下一页
     */
    public void setNextPage() {
        this.current++;
    }
}
