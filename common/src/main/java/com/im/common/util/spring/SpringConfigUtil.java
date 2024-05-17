package com.im.common.util.spring;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.im.common.constant.CommonConstant;
import com.im.common.util.CollectionUtil;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Spring通用配置
 *
 * @author Barry
 * @date 2019/2/20
 */
public class SpringConfigUtil implements WebMvcConfigurer, ApplicationContextAware {
    protected static final Log LOG = LogFactory.get();

    /**
     * 默认解析器 其中locale表示默认语言
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.CHINA);
        return localeResolver;
    }

    /**
     * 设置spring上下文
     *
     * @param applicationContext spring上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 消息转换器
        converters.add(SpringMessageConverterUtil.buildStringConverter());
        converters.add(SpringMessageConverterUtil.buildFastJsonConverter());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 枚举转换器
        registry.addConverterFactory(new EnumConverterFactory());
    }

    /**
     * 静态资源文件映射配置
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 资源文件，使用Spring默认规范
        registry.addResourceHandler("/resources/**").setCachePeriod(0);
        registry.addResourceHandler("/serverstatic/**").addResourceLocations("classpath:/serverstatic/").setCachePeriod(0);
    }

    /**
     * 消息转换器声明为Spring对象
     */
    @Bean
    public HttpMessageConverters customConverters() {
        StringHttpMessageConverter stringConverter = SpringMessageConverterUtil.buildStringConverter();
        FastJsonHttpMessageConverter fastJsonConverter = SpringMessageConverterUtil.buildFastJsonConverter();
        return new HttpMessageConverters(Arrays.asList(stringConverter, fastJsonConverter));
    }

    /**
     * 跨域配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST");
    }

    /**
     * Spring 请求参数验证配置
     *
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        /** 设置validator模式为快速失败返回 */
        postProcessor.setValidator(validator);
        return postProcessor;
    }

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }

    /**
     * 自定义参数注解
     *
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        Set<Class<?>> allSubclasses = ClassUtil.scanPackageBySuper(CommonConstant.ARGUMENT_RESOLVER_PACKAGE, HandlerMethodArgumentResolver.class);
        if (CollectionUtil.isEmpty(allSubclasses)) {
            return;
        }

        try {
            for (Class<?> subclass : allSubclasses) {

                HandlerMethodArgumentResolver resolver = (HandlerMethodArgumentResolver) subclass.newInstance();

                LOG.info("初始化Spring自定义参数注解：{}", resolver.getClass().getName());

                resolvers.add(resolver);
            }
        } catch (InstantiationException e) {
            LOG.error(e, "初始化Spring自定义参数注解异常");
        } catch (IllegalAccessException e) {
            LOG.error(e, "初始化Spring自定义参数注解异常");
        }
    }

    /**
     * 调度配置
     *
     * @return TaskScheduler
     */
    @Bean
    public TaskScheduler scheduledExecutorService() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(32);
        scheduler.setThreadNamePrefix("scheduled-thread-");
        return scheduler;
    }

    /**
     * 多线程配置
     *
     * @return ThreadPoolTaskScheduler
     */
    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(32);
        scheduler.setThreadNamePrefix("threadPoolTaskScheduler-");
        return scheduler;
    }
}