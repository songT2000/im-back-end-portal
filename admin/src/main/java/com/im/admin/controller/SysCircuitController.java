package com.im.admin.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.StringUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.SysCircuit;
import com.im.common.entity.enums.SysCircuitTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.SysCircuitService;
import com.im.common.util.OrderUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.SysCircuitAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台用户controller
 *
 * @author max.stark
 */
@RestController
@Api(tags = "系统线路相关接口")
public class SysCircuitController extends BaseController {

    private SysCircuitService sysCircuitService;

    @Autowired
    public void setSysCircuitService(SysCircuitService sysCircuitService) {
        this.sysCircuitService = sysCircuitService;
    }

    @RequestMapping(value = ApiUrl.SYS_CIRCUIT_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    @ApiOperationSupport(order = 1)
    public RestResponse<PageVO<SysCircuitAdminVO>> sysInformationPage(@RequestBody @Valid SysCircuitPageAdminParam param) {
        PageVO<SysCircuitAdminVO> page = sysCircuitService.pageVO(param, SysCircuitAdminVO::new);
        return ok(page);
    }

    @RequestMapping(value = ApiUrl.SYS_CIRCUIT_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_CIRCUIT_ADD)
    @ApiOperation("新增")
    @ApiOperationSupport(order = 2)
    public RestResponse sysInformationAdd(@RequestBody @Valid SysCircuitAddAdminParam param) {
        return sysCircuitService.addForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.SYS_CIRCUIT_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_CIRCUIT_EDIT)
    @ApiOperation("编辑")
    @ApiOperationSupport(order = 3)
    public RestResponse sysInformationEdit(@RequestBody @Valid SysCircuitEditAdminParam param) {
        return sysCircuitService.editForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.SYS_CIRCUIT_ENABLE_DISABLE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_CIRCUIT_ENABLE_DISABLE)
    @ApiOperation("启/禁")
    @ApiOperationSupport(order = 4)
    public RestResponse sysBannerEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return sysCircuitService.enableDisableForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.SYS_CIRCUIT_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_CIRCUIT_DELETE)
    @ApiOperation("删除")
    @ApiOperationSupport(order = 4)
    public RestResponse sysInformationDelete(@RequestBody @Valid IdParam param) {
        return sysCircuitService.deleteForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.SYS_CIRCUIT_EXPORT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_CIRCUIT_EXPORT)
    @ApiOperation("导出")
    @ApiOperationSupport(order = 5)
    public void userBillExport(HttpServletResponse response) {
        List<SysCircuit> sysCircuits = sysCircuitService.listByEnabled();
        String originalFilename = "";  // 拼接文件完整路径
        if (sysCircuits != null && sysCircuits.size() > 0 && !StringUtils.isNullOrEmpty(sysCircuits.get(0).getThirdPartyAddress())) {
            originalFilename = sysCircuits.get(0).getThirdPartyAddress().substring(sysCircuits.get(0).getThirdPartyAddress().lastIndexOf("/"));
        } else {
           originalFilename = "/" + OrderUtil.orderNumberToMs() + ".json";
        }
        File excelFile = new File(originalFilename);
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            if (!excelFile.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                excelFile.getParentFile().mkdirs();
            }
            if (excelFile.exists()) { // 如果已存在,删除旧文件
                excelFile.delete();
            }
            excelFile.createNewFile();

            // 格式化json字符串
            Map<String, Object> map = new HashMap<>();
            map.put("errno", 0);
            if (sysCircuits != null && sysCircuits.size() > 0) {
                List<String> appStrs = new ArrayList<>();
                List<String> webStrs = new ArrayList<>();
                sysCircuits.stream().forEach(sc -> {
                    String rsaUrl = StrUtil.rsaPublicEncryptApiData(sc.getCircuitUrl());
                    if (sc.getCircuitType() == SysCircuitTypeEnum.APP) {
                        appStrs.add(rsaUrl);
                    } else {
                        webStrs.add(rsaUrl);
                    }
                });
                map.put("errno", 0);
                map.put("app", appStrs);
                map.put("web", webStrs);
            } else {
                map.put("errno", 1);
            }
            String jsonString = JSON.toJSONString(map);
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(excelFile), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
            // 获取文件名
            String filename = excelFile.getName();
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(excelFile);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + excelFile.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelFile.exists()) { // 如果已存在,删除旧文件
                excelFile.delete();
            }
        }
    }
}
