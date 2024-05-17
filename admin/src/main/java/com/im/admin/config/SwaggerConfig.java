package com.im.admin.config;

import com.im.admin.controller.BaseController;
import com.im.common.config.SwaggerConfigUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Swagger2配置
 *
 * @author Barry
 * @date 2020-07-05
 */
@Configuration
@EnableSwagger2WebMvc
@ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
public class SwaggerConfig extends SwaggerConfigUtil {
    @Override
    public String getGroupName() {
        return "后台接口文档";
    }

    @Override
    public String getBasePackage() {
        return BaseController.class.getPackage().getName();
    }
}
