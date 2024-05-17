package com.im.portal.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.lock.exception.LockFailureException;
import com.im.common.exception.CommonException;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.RequestUtil;
import com.im.common.util.i18n.I18nTranslateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * <p>统一异常处理器</p>
 *
 * <p>Controller/Service层中尽量不要自己处理异常，保持代码干净清爽，一直往上抛，尽量让该处理器来统一处理异常</p>
 *
 * <p>关于打印异常：只有当处理器找不到明确定义的处理方法，进到{@link #handleException(Exception)}时才会打印异常(log)，其它情况均不打印异常</p>
 *
 * <p>{@link ImException}可以标识为强制登出</p>
 *
 * @author Barry
 * @date 2018/5/12
 */
@RestControllerAdvice
public class PortalExceptionHandler {
    private static final String BIND_EXCEPTION_CODE_TYPE_MISMATCH = "typeMismatch";
    private static String BIND_EXCEPTION_CODE_TYPE_MISMATCH_ERROR_MSG = "field [{}] is illegal";

    private static final Log LOG = LogFactory.get();

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    /**
     * 系统自定义异常
     */
    @ExceptionHandler(ImException.class)
    public RestResponse handleException(ImException e) {
        RestResponse rsp = RestResponse.exception(e);

        return rsp;
    }

    /**
     * 系统自定义异常
     */
    @ExceptionHandler(CommonException.class)
    public RestResponse handleCommonException(CommonException e) {
        String message = e.getMessage();
        Object[] params = e.getParams();

        RestResponse rsp;

        if (StrUtil.isNotBlank(message)) {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, message, params);
        } else {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        return rsp;
    }

    /**
     * <p>方法未实现异常</p>
     * <p>{@link NoHandlerFoundException}：调用未配置的接口</p>
     * <p>{@link MethodNotAllowedException}：使用GET调用仅支持POST的接口</p>
     *
     * @return
     */
    @ExceptionHandler({NoHandlerFoundException.class, MethodNotAllowedException.class, HttpRequestMethodNotSupportedException.class})
    public Object handleNoHandlerFoundException() {
        String requestPath = RequestUtil.getCurrentRequestPath();
        return RestResponse.exception(ResponseCode.SYS_NO_SUCH_METHOD, requestPath);
    }

    /**
     * <p>传错了Content-Type</p>
     * <p>{@link HttpMediaTypeNotSupportedException}：传错了Content-Type</p>
     *
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class})
    public Object handleHttpMediaTypeNotSupportedException(HttpMediaTypeException e) {
        return RestResponse.exception(ResponseCode.SYS_CONTENT_TYPE_ERROR);
    }

    /**
     * 参数异常
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public Object handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();

        RestResponse rsp;

        if (StrUtil.isNotBlank(message)) {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, message);
        } else {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        return rsp;
    }

    /**
     * 参数异常
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = e.getMessage();

        RestResponse rsp;

        if (StrUtil.isNotBlank(message)) {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, message);
        } else {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        return rsp;
    }

    /**
     * 参数异常
     */
    @ExceptionHandler({NullPointerException.class})
    public Object handleNullPointerException(NullPointerException e) {
        // 打印异常日志
        LOG.error(e);

        String message = e.getMessage();

        RestResponse rsp;

        if (StrUtil.isNotBlank(message)) {
            rsp = RestResponse.exception(ResponseCode.SYS_CUSTOM_MSG, message);
        } else {
            rsp = RestResponse.exception(ResponseCode.SYS_SERVER_ERROR);
        }

        return rsp;
    }

    /**
     * 参数缺失
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Object handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_MISSING, e.getParameterName());
    }


    /**
     * Spring 参数验证异常,直接验证字段时返回的异常
     */
    @ExceptionHandler({ValidationException.class})
    public Object handleValidationException(ValidationException e) {
        String message = null;
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();


            if (constraintViolations != null && constraintViolations.size() > 0) {
                ConstraintViolation<?> next = constraintViolations.iterator().next();
                message = next.getMessageTemplate();
            }
        } else if (e instanceof UnexpectedTypeException) {
            UnexpectedTypeException ute = (UnexpectedTypeException) e;
            message = ute.getMessage();
        }

        RestResponse rsp;

        if (StrUtil.isNotBlank(message)) {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, message);
        } else {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        return rsp;
    }

    /**
     * Spring 参数验证异常,当验证对象(@Valid)时抛出异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        RestResponse rsp;
        if (fieldError != null) {
            if (I18nTranslateUtil.isI18nKey(fieldError.getDefaultMessage())) {
                rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, fieldError.getDefaultMessage());
            } else {
                rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, fieldError.getField() + " " + fieldError.getDefaultMessage());
            }
        } else {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        return rsp;
    }

    /**
     * Spring 参数验证异常,当验证对象(@Valid)时抛出异常
     */
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        RestResponse rsp;
        if (fieldError != null) {
            String code = fieldError.getCode();
            // 参数类型错误
            if (BIND_EXCEPTION_CODE_TYPE_MISMATCH.equals(code)) {
                String errorMsg = StrUtil.format(BIND_EXCEPTION_CODE_TYPE_MISMATCH_ERROR_MSG,
                        fieldError.getField());
                rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, errorMsg);
            } else {
                rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, fieldError.getDefaultMessage());
            }
        } else {
            rsp = RestResponse.exception(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        return rsp;
    }

    /**
     * 上传文件大小限制异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Object handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return RestResponse.exception(ResponseCode.SYS_UPLOAD_FILE_SIZE_EXCEEDED, maxFileSize);
    }

    /**
     * 分布式锁获取锁超时
     */
    @ExceptionHandler(LockFailureException.class)
    public Object handleLockFailureException(LockFailureException e) {
        return RestResponse.exception(ResponseCode.SYS_REQUEST_ACQUIRE_LOCK_FAILED);
    }

    /**
     * 其它未知异常，将会打印异常信息到log中
     */
    @ExceptionHandler({Exception.class})
    public Object handleException(Exception e) {
        String msg = e.getMessage();
        msg = StrUtil.isNotBlank(msg) ? msg : "error caused";

        // 打印异常日志
        LOG.error(e, msg);

        return RestResponse.exception(ResponseCode.SYS_SERVER_ERROR);
    }
}