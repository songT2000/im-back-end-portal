package com.im.common.util.mybatis.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.util.CollectionUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

/**
 * 分页结果
 *
 * @author Barry
 * @date 2018/6/17
 */
@Data
@ApiModel
public class PageVO<R> {
    /**
     * 总记录数
     **/
    @JSONField(ordinal = 1)
    @ApiModelProperty(value = "总记录数", position = 1)
    private Long total;

    /**
     * 总页数
     **/
    @JSONField(ordinal = 2)
    @ApiModelProperty(value = "总页数", position = 2)
    private Long pages;

    /**
     * 当前页
     **/
    @JSONField(ordinal = 3)
    @ApiModelProperty(value = "当前页", position = 3)
    private Long current;

    /**
     * 每页数
     **/
    @ApiModelProperty(value = "每页数", position = 4)
    private Long size;

    /**
     * 记录列表
     **/
    @JSONField(ordinal = 4)
    @ApiModelProperty(value = "记录列表", position = 5)
    private List<R> records;

    public PageVO() {
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page 可为空
     */
    public <T> PageVO(AbstractPageParam<T> page) {
        if (page != null) {
            this.total = 0L;
            this.pages = 0L;
            this.current = page.getCurrent();
            this.size = page.getSize();
        }
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page 可为空
     */
    public PageVO(Page<R> page) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = page.getRecords();
        }
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page 可为空
     */
    public <T> PageVO(Page<T> page, List<R> records) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = records;
        }
    }

    public <T> PageVO(AbstractPageParam<T> param, long total, List<R> records) {
        long pages;
        if (total == 0) {
            pages = 0L;
        } else {
            pages = total / param.getSize();
            if (total % param.getSize() != 0) {
                pages++;
            }
        }

        this.total = total;
        this.pages = pages;
        this.current = param.getCurrent();
        this.size = param.getSize();
        this.records = records;
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page 可为空
     */
    public PageVO(PageVO<R> page) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = page.getRecords();
        }
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page   可为空
     * @param mapper 转换对象
     */
    public <T> PageVO(IPage<T> page, Function<T, R> mapper) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = CollectionUtil.toList(page.getRecords(), mapper);
        }
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page    不可为空
     * @param records 数据
     * @param mapper  转换对象
     * @param <T>
     */
    public <T> PageVO(IPage<?> page, List<T> records, Function<T, R> mapper) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = CollectionUtil.toList(records, mapper);
        }
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page    不可为空
     * @param records 数据
     */
    public PageVO(IPage<?> page, List<R> records) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = records;
        }
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page   不可为空
     * @param mapper 转换对象
     * @param <T>
     */
    public <T> PageVO(PageVO<T> page, Function<T, R> mapper) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = CollectionUtil.toList(page.getRecords(), mapper);
        }
    }

    /**
     * 将mybatis-plus page对象转换成简易对象
     *
     * @param page 可为空
     */
    public PageVO(IPage<R> page) {
        if (page != null) {
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.current = page.getCurrent();
            this.size = page.getSize();
            this.records = page.getRecords();
        }
    }

    /**
     * 是否还有下一页
     *
     * @return boolean
     */
    public boolean hasMore() {
        return current >= pages;
    }
}
