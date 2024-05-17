package com.im.portal.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.FileService;
import com.im.common.util.aop.limit.RequestLimit;
import com.im.common.util.aop.limit.RequestLimitTypeEnum;
import com.im.common.vo.PortalSessionUser;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传接口
 *
 * @author Max
 * @date 2020-11-09
 */
@RestController
@Api(tags = "文件上传相关接口")
@ApiSupport(order = 98)
public class FileController extends BaseController {
    private static final Log LOG = LogFactory.get();

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = ApiUrl.FILE_UPLOAD_ID, method = RequestMethod.POST)
    @ApiOperation(value = "上传ID/身份证", notes = "返回的data里面就是文件的绝对地址，仅接收JPG和PNG文件")
    // @RequestLimit(type = RequestLimitTypeEnum.USER, second = 60, count = 300)
    public RestResponse<String> fileUploadId(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            PortalSessionUser sessionUser = getSessionUser(request);
            String originalFilename = file.getOriginalFilename();

            return fileService.uploadId(sessionUser, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传ID出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传ID出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }

    @RequestMapping(value = ApiUrl.FILE_UPLOAD_VOUCHER, method = RequestMethod.POST)
    @ApiOperation(value = "上传支付凭证等", notes = "返回的data里面就是文件的绝对地址，仅接收JPG和PNG文件")
    // @RequestLimit(type = RequestLimitTypeEnum.USER, second = 60, count = 60)
    public RestResponse<String> fileUploadVoucher(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            PortalSessionUser sessionUser = getSessionUser(request);
            String originalFilename = file.getOriginalFilename();

            return fileService.uploadVoucher(sessionUser, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传凭证出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传凭证出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }

    @RequestMapping(value = ApiUrl.FILE_UPLOAD_IMAGE, method = RequestMethod.POST)
    @ApiOperation(value = "上传图片", notes = "返回的data里面就是文件的绝对地址，仅接收JPG和PNG文件")
    // @RequestLimit(type = RequestLimitTypeEnum.USER, second = 60, count = 60)
    public RestResponse<String> fileUploadBanner(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        //如果是使用minio作为文件服务器，要支持匿名访问，直接在控制台配置，支持*策略
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            PortalSessionUser sessionUser = getSessionUser(request);
            String originalFilename = file.getOriginalFilename();

            return fileService.uploadImage(sessionUser, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传图片出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传图片出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }

    @RequestMapping(value = ApiUrl.FILE_UPLOAD_AVATAR, method = RequestMethod.POST)
    @ApiOperation(value = "上传头像", notes = "返回的data里面就是文件的绝对地址，仅接收JPG和PNG文件")
    // @RequestLimit(type = RequestLimitTypeEnum.USER, second = 60, count = 60)
    public RestResponse<String> fileUploadAvatar(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        //如果是使用minio作为文件服务器，要支持匿名访问，直接在控制台配置，支持*策略
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            PortalSessionUser sessionUser = getSessionUser(request);
            String originalFilename = file.getOriginalFilename();

            return fileService.uploadAvatar(sessionUser, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传头像出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传头像出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }

    @RequestMapping(value = ApiUrl.FILE_UPLOAD_AVATAR_NO_AUTH, method = RequestMethod.POST)
    @ApiOperation(value = "注册上传头像", notes = "返回的data里面就是文件的绝对地址，仅接收JPG和PNG文件")
    @RequestLimit(type = RequestLimitTypeEnum.IP, second = 60, count = 6)
    public RestResponse<String> registerUploadAvatar(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        //如果是使用minio作为文件服务器，要支持匿名访问，直接在控制台配置，支持*策略
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            String originalFilename = file.getOriginalFilename();

            return fileService.uploadAvatar(null, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传头像出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传头像出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }

    @RequestMapping(value = ApiUrl.FILE_UPLOAD_VIDEO, method = RequestMethod.POST)
    @RequestLimit(type = RequestLimitTypeEnum.USER, second = 60, count = 60)
    // @ApiOperation(value = "上传视频", notes = "返回的data里面就是文件的绝对地址")
    public RestResponse<String> fileUploadVideo(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        //如果是使用minio作为文件服务器，要支持匿名访问，直接在控制台配置，支持*策略
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            PortalSessionUser sessionUser = getSessionUser(request);
            String originalFilename = file.getOriginalFilename();

            return fileService.uploadVideo(sessionUser, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传视频出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传视频出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }


    @RequestMapping(value = ApiUrl.FILE_UPLOAD_SOUND, method = RequestMethod.POST)
    @ApiOperation(value = "上传语音", notes = "返回的data里面就是文件的绝对地址")
    // @RequestLimit(type = RequestLimitTypeEnum.USER, second = 60, count = 60)
    public RestResponse<String> fileUploadSound(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        //如果是使用minio作为文件服务器，要支持匿名访问，直接在控制台配置，支持*策略
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            PortalSessionUser sessionUser = getSessionUser(request);
            String originalFilename = file.getOriginalFilename();

            return fileService.uploadSound(sessionUser, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传语音出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传语音出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }


    @RequestMapping(value = ApiUrl.FILE_UPLOAD_FILE, method = RequestMethod.POST)
    @ApiOperation(value = "上传文件", notes = "返回的data里面就是文件的绝对地址")
    // @RequestLimit(type = RequestLimitTypeEnum.USER, second = 60, count = 60)
    public RestResponse<String> fileUploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        //如果是使用minio作为文件服务器，要支持匿名访问，直接在控制台配置，支持*策略
        if (file == null || file.isEmpty()) {
            return failed(ResponseCode.SYS_UPLOAD_FILE_NOT_EMPTY);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            PortalSessionUser sessionUser = getSessionUser(request);
            String originalFilename = file.getOriginalFilename();

            return fileService.uploadFile(sessionUser, originalFilename, inputStream);
        } catch (IOException e) {
            LOG.error(e, "上传文件出错");
            return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e, "上传文件出错");
                    return failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }
}
