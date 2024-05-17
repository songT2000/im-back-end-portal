package com.im.common.config;

import com.fasterxml.classmate.ResolvedType;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.google.common.base.Optional;
import com.im.common.entity.enums.BaseEnum;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;
import com.im.common.util.url.RequestWebsite;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.Annotations;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2通用配置
 *
 * @author Barry
 * @date 2020-07-05
 */
public abstract class SwaggerConfigUtil implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        // 把枚举的说明改成参考数据字典XXX

        Optional<ApiModelProperty> annotation = Optional.absent();

        if (context.getAnnotatedElement().isPresent()) {
            ApiModelProperty apiModelProperty = ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()).get();
            annotation = annotation.or(Optional.fromNullable(apiModelProperty));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            ApiModelProperty apiModelProperty = Annotations.findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class).get();
            annotation = annotation.or(Optional.fromNullable(apiModelProperty));
        }
        final Class<?> rawPrimaryType = context.getBeanPropertyDefinition().get().getRawPrimaryType();

        // 过滤得到目标类型
        if (annotation.isPresent() && BaseEnum.class.isAssignableFrom(rawPrimaryType)) {

            // 文档中的显示描述
            final List<String> displayValues = new ArrayList<>();
            displayValues.add("参考数据字典" + rawPrimaryType.getSimpleName());

            final AllowableListValues allowableListValues = new AllowableListValues(displayValues, rawPrimaryType.getTypeName());

            // 实际值
            final ResolvedType resolvedType = context.getResolver().resolve(String.class);
            context.getBuilder().allowableValues(allowableListValues).type(resolvedType);
        }

        // 过滤得到目标类型
        if (annotation.isPresent() && I18nString.class.getName().equals(rawPrimaryType.getName())) {
            // 实际值
            final ResolvedType resolvedType = context.getResolver().resolve(String.class);
            context.getBuilder().type(resolvedType);
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    /**
     * 文档分组名称，比如前台接口文档
     *
     * @return
     */
    public abstract String getGroupName();

    /**
     * 接口位置
     *
     * @return
     */
    public abstract String getBasePackage();

    /**
     * 创建API文档
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        final String groupName = getGroupName();
        final String basePackage = getBasePackage();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title(groupName).build())
                .groupName(groupName)
                // 忽略接口参数里自定义注解
                .ignoredParameterTypes(RequestIp.class)
                .ignoredParameterTypes(RequestWebsite.class)
                .select()
                // 这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                // knife4j自定义设置
                .extensions(buildSettingExtensions());
        return docket;
    }

    private List<VendorExtension> buildSettingExtensions() {
        OpenApiExtendSetting setting = new OpenApiExtendSetting();
        setting.setEnableSwaggerModels(false);
        setting.setEnableVersion(true);
        setting.setEnableDebug(false);
        setting.setEnableGroup(true);
        setting.setEnableFooter(false);
        setting.setEnableFooterCustom(true);
        setting.setFooterCustomContent("Life is a fucking movie | [麒麟团队](javascript:;)");
        setting.setEnableHomeCustom(true);
        setting.setHomeCustomLocation("classpath:api/doc.md");
        setting.setEnableDocumentManage(false);

        Knife4jExtensionResolver resolver = new Knife4jExtensionResolver(setting, null);
        return resolver.buildSettingExtensions();
    }
}
