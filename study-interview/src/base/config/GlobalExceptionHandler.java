package base.config;

import base.model.RestResponse;
import cn.hutool.core.collection.CollectionUtil;
import exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.BindException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局Rest异常处理
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-20 2:03:05
 */
@Slf4j
@RestControllerAdvice
@Primary
public class GlobalExceptionHandler {

    private static final String ERROR_START_STR = "system error: {}";

    private static final String COMMA = ",";

    private static final String MISS = "system error: Required request body is missing:";

    private static final String ERROR = "system error: JSON parse error: ";

    private static final int ERROR_CODE = 500;

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public RestResponse sysExceptionHandler(Exception exception) {
        log.error(ERROR_START_STR, exception.getMessage(), exception);
        return RestResponse.fail();
    }

    @ExceptionHandler(BusinessException.class)
    public RestResponse businessExceptionHandler(BusinessException businessException) {
        String msg = getMessageByErrorCode(businessException);
        log.error(msg, businessException);
        return new RestResponse<>(ERROR_CODE, msg);
    }

    /**
     * 根据错误码生成提示信息, 并根据当前上下文中的Request获取Locale, 返回对应语言的提示信息
     *
     * @param businessException 业务异常
     * @return 异常信息
     */
    private String getMessageByErrorCode(BusinessException businessException) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Locale requestLocale = request.getLocale();
        String errorCode = businessException.getErrorCode();
        String message = this.messageSource.getMessage(errorCode, null, requestLocale);
        if (businessException.getMessages() != null) {
            message = String.format(message, businessException.getMessages());
        }
        return message;
    }

    /**
     * 处理FormData方式调用接口校验失败抛出的异常
     *
     * @param bindException 绑定异常
     * @return RestResponse
     */
    @ExceptionHandler(BindException.class)
    public RestResponse bindExceptionHandler(BindException bindException) {
        // TODO 这里是不是有问题, 感觉只能输出, message
        log.error(ERROR_START_STR, bindException.getMessage(), bindException);
        // TODO 这里导包可能有问题
        // List<FieldError> fieldErrorList = bindException.getBindingResult().getFieldErrors();
        List<FieldError> fieldErrorList = Collections.emptyList();
        if (CollectionUtil.isEmpty(fieldErrorList)) {
            return RestResponse.fail();
        }
        List<String> defaultMessageList = fieldErrorList.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new RestResponse<>(ERROR_CODE, StringUtils.join(defaultMessageList, COMMA));
    }

    /**
     * 处理JSON请求体调用接口校验失败抛出的异常
     *
     * @param ex MethodArgumentNotValidException
     * @return RestResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ERROR_START_STR, ex.getMessage(), ex);
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (CollectionUtil.isEmpty(fieldErrors)) {
            return RestResponse.fail();
        }
        List<String> defaultMessageList = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new RestResponse<>(ERROR_CODE, StringUtils.join(defaultMessageList, COMMA));
    }

    /**
     * 处理单个参数校验失败的异常
     *
     * @param ex ConstraintViolationException
     * @return RestResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public RestResponse handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ERROR_START_STR, ex.getMessage(), ex);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        if (CollectionUtil.isEmpty(constraintViolations)) {
            return RestResponse.fail();
        }
        List<String> defaultMessageList = constraintViolations.stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return new RestResponse<>(ERROR_CODE, StringUtils.join(defaultMessageList, COMMA));
    }

    /**
     * 忽略参数异常处理器
     *
     * @param ex MissingServletRequestParameterException
     * @return RestResponse
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RestResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error(ERROR_START_STR, ex.getMessage(), ex);
        return new RestResponse<>(ERROR_CODE, "请求参数: " + ex.getParameterName() + " 不能为空");
    }

    /**
     * 缺少请求体异常处理器
     *
     * @param ex HttpMessageNotReadableException
     * @return RestResponse
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RestResponse handleXxxxx(HttpMessageNotReadableException ex) {
        log.error(ERROR_START_STR, ex.getMessage(), ex);
        String errorMessage = ex.getMessage();
        if (StringUtils.isEmpty(errorMessage)) {
            return new RestResponse<>(ERROR_CODE, "请求参数体为空或请求参数有误");
        }
        if (StringUtils.startsWithIgnoreCase(errorMessage, MISS)) {
            return new RestResponse<>(ERROR_CODE, "请求参数体为空");
        } else if (StringUtils.startsWithIgnoreCase(errorMessage, ERROR)) {
            return new RestResponse<>(ERROR_CODE, "请求参数体有误");
        } else {
            return new RestResponse<>(ERROR_CODE, "请求参数体为空或请求参数有误");
        }
    }
}
