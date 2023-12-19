package base.config;

import base.model.RestResponse;
import cn.hutool.json.JSONUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.swagger.web.UiConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一的结果处理
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-20 1:52:59
 */
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    /**
     * 判断是否执行 beforeBodyWrite
     *
     * @param returnType    范围类型
     * @param converterType 转换器类型
     * @return 是否执行 beforeBodyWrite
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object responseBody, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (responseBody instanceof RestResponse ||
                // 不拦截Swagger页面
                responseBody instanceof UiConfiguration ||
                responseBody instanceof Json ||
                (
                        responseBody instanceof ArrayList &&
                                !((List) responseBody).isEmpty() &&
                                !(((List) responseBody).get(0) instanceof SwaggerConfig)
                )
        ) {
            return responseBody;
        }

        // String类型需要单独处理, MessageConverters 不会自动转成JSON格式
        if (responseBody instanceof String) {
            return JSONUtil.toJsonStr(RestResponse.ok(responseBody));
        }
        return RestResponse.ok(responseBody);
    }
}
