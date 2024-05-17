package com.im.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.im.common.util.mybatis.typehandler.fill.MybatisMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * MyBatis-Plus配置
 *
 * @author Barry
 * @date 2018/5/22
 */
@Configuration
public class MyBatisPlusConfig {
    /**
     * 分页拦截器
     *
     * @return PaginationInnerInterceptor
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        // 分页最大条数，设置这个值可以防止接口恶意查询查询过大的条数
        final long maxLimit = 200L;
        interceptor.setMaxLimit(maxLimit);
        interceptor.setDbType(DbType.MYSQL);
        return interceptor;
    }

    /**
     * 自动填充字段
     *
     * @return AutoFillMetaObjectHandler
     */
    @Bean
    public MybatisMetaObjectHandler autoFillMetaObjectHandler() {
        return new MybatisMetaObjectHandler();
    }

    /**
     * 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页拦截器
        interceptor.addInnerInterceptor(paginationInnerInterceptor());

        return interceptor;
    }

    @Bean
    public MyBatisPlusInjector sqlInjector() {
        // 自定义sql注入器
        return new MyBatisPlusInjector();
    }

    public static class MyBatisPlusInjector extends DefaultSqlInjector {
        @Override
        public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
            List<AbstractMethod> methodList = super.getMethodList(mapperClass);
            // 这个批量更新插件比ServiceImpl的速度稍微快一点
            // 测试是插入10万条账变，内置saveBatch方法是多条insert语句，批量提交，耗时10秒左右
            // fastSaveBatch耗时9秒左右
            methodList.add(new InsertBatchSomeColumn());
            return methodList;
        }
    }
}
