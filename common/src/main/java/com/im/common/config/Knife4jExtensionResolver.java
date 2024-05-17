package com.im.common.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownChildren;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownFile;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtension;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiMarkdownExtension;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiSettingExtension;
import com.im.common.util.CollectionUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import springfox.documentation.service.VendorExtension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 稍微改一点点com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver
 *
 * @author Barry
 * @date 2021-02-18
 */
public class Knife4jExtensionResolver {
    private static final Log LOG = LogFactory.get();
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private final Map<String, List<OpenApiExtendMarkdownFile>> markdownFileMaps = new HashMap();
    private final OpenApiExtendSetting setting;
    private final List<MarkdownProperty> markdownProperties;

    public void start() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolver method start...");
        }

        if (CollectionUtil.isNotEmpty(this.markdownProperties)) {
            Iterator var1 = this.markdownProperties.iterator();

            label64:
            while(true) {
                MarkdownProperty markdownProperty;
                do {
                    do {
                        if (!var1.hasNext()) {
                            break label64;
                        }

                        markdownProperty = (MarkdownProperty)var1.next();
                    } while(!StrUtil.isNotBlank(markdownProperty.getName()));
                } while(!StrUtil.isNotBlank(markdownProperty.getLocations()));

                String swaggerGroupName = StrUtil.isNotBlank(markdownProperty.getGroup()) ? markdownProperty.getGroup() : "default";
                OpenApiExtendMarkdownFile openApiExtendMarkdownFile = new OpenApiExtendMarkdownFile();
                openApiExtendMarkdownFile.setName(markdownProperty.getName());
                List<OpenApiExtendMarkdownChildren> allChildrenLists = new ArrayList();
                String[] locations = markdownProperty.getLocations().split(";");
                if (!CollectionUtils.isEmpty(locations)) {
                    String[] var7 = locations;
                    int var8 = locations.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        String location = var7[var9];
                        if (StrUtil.isNotBlank(location)) {
                            List<OpenApiExtendMarkdownChildren> childrenList = this.readLocations(location);
                            if (CollectionUtils.isNotEmpty(childrenList)) {
                                allChildrenLists.addAll(childrenList);
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(allChildrenLists)) {
                    openApiExtendMarkdownFile.setChildren(allChildrenLists);
                }

                if (this.markdownFileMaps.containsKey(swaggerGroupName)) {
                    ((List)this.markdownFileMaps.get(swaggerGroupName)).add(openApiExtendMarkdownFile);
                } else {
                    this.markdownFileMaps.put(swaggerGroupName, CollectionUtils.newArrayList(new OpenApiExtendMarkdownFile[]{openApiExtendMarkdownFile}));
                }
            }
        }

        if (this.setting != null && this.setting.isEnableHomeCustom() && StrUtil.isNotBlank(this.setting.getHomeCustomLocation())) {
            String content = this.readCustomHome(this.setting.getHomeCustomLocation());
            this.setting.setHomeCustomLocation(content);
        }

    }

    private String readCustomHome(String customHomeLocation) {
        String customHomeContent = "";

        try {
            Resource[] resources = this.resourceResolver.getResources(customHomeLocation);
            if (resources != null && resources.length > 0) {
                Resource resource = resources[0];
                customHomeContent = new String(CommonUtils.readBytes(resource.getInputStream()), "UTF-8");
            }
        } catch (Exception var5) {
            LOG.warn("(Ignores) Failed to read CustomeHomeLocation Markdown files,Error Message:{} ", var5.getMessage());
        }

        return customHomeContent;
    }

    private List<OpenApiExtendMarkdownChildren> readLocations(String locations) {
        try {
            List<OpenApiExtendMarkdownChildren> openApiExtendMarkdownChildrenList = new ArrayList();
            Resource[] resources = this.resourceResolver.getResources(locations);
            if (resources != null && resources.length > 0) {
                Resource[] var4 = resources;
                int var5 = resources.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Resource resource = var4[var6];
                    OpenApiExtendMarkdownChildren markdownFile = this.readMarkdownChildren(resource);
                    if (markdownFile != null) {
                        openApiExtendMarkdownChildrenList.add(markdownFile);
                    }
                }

                return openApiExtendMarkdownChildrenList;
            }
        } catch (Exception var9) {
            LOG.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", var9.getMessage());
        }

        return null;
    }

    private OpenApiExtendMarkdownChildren readMarkdownChildren(Resource resource) {
        try {
            if (resource != null) {
                OpenApiExtendMarkdownChildren markdownFile = new OpenApiExtendMarkdownChildren();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("read file:" + resource.getFilename());
                }

                if (resource.getFilename().toLowerCase().endsWith(".md")) {
                    BufferedReader reader = null;

                    OpenApiExtendMarkdownChildren var9;
                    try {
                        reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));
                        String le = null;
                        String title = resource.getFilename();
                        String reg = "#{1,3}\\s{1}(.*)";
                        Pattern pattern = Pattern.compile(reg, 2);
                        Matcher matcher = null;
                        if ((le = reader.readLine()) != null) {
                            matcher = pattern.matcher(le);
                            if (matcher.matches()) {
                                title = matcher.group(1);
                            }
                        }

                        markdownFile.setTitle(title);
                        markdownFile.setContent(new String(CommonUtils.readBytes(resource.getInputStream()), "UTF-8"));
                        var9 = markdownFile;
                    } catch (Exception var14) {
                        LOG.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", var14.getMessage());
                        return null;
                    } finally {
                        CommonUtils.closeQuiltly(reader);
                    }

                    return var9;
                }
            }
        } catch (Exception var16) {
            LOG.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", var16.getMessage());
        }

        return null;
    }

    public List<VendorExtension> buildExtensions(String groupName) {
        String swaggerGroupName = StrUtil.isNotBlank(groupName) ? groupName : "default";
        OpenApiExtension openApiExtension = new OpenApiExtension("x-openapi");
        openApiExtension.addProperty(new OpenApiSettingExtension(this.setting));
        openApiExtension.addProperty(new OpenApiMarkdownExtension((List)this.markdownFileMaps.get(swaggerGroupName)));
        List<VendorExtension> vendorExtensions = new ArrayList();
        vendorExtensions.add(openApiExtension);
        return vendorExtensions;
    }

    public List<VendorExtension> buildSettingExtensions() {
        OpenApiExtension openApiExtension = new OpenApiExtension("x-openapi");
        openApiExtension.addProperty(new OpenApiSettingExtension(this.setting));
        List<VendorExtension> vendorExtensions = new ArrayList();
        vendorExtensions.add(openApiExtension);
        return vendorExtensions;
    }

    public Knife4jExtensionResolver(OpenApiExtendSetting setting, List<MarkdownProperty> markdownProperties) {
        this.setting = setting;
        this.markdownProperties = markdownProperties;

        this.start();
    }
}
