package base.accesslog;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-11-26 15:46:02
 */
@Aspect
@Component
@Slf4j
public class AccessLogRecorder {
    private static final int TRUNCAT_LENTH = 500;

    @Around("execution(* cache.custom.*Controller.*(..))")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Object[] args = proceedingJoinPoint.getArgs();
        Stream<?> stream = ObjectUtil.isEmpty(args) ? Stream.empty() : Arrays.stream(args.clone());
        /**
         * HttpServletRequest: 不能被序列化, 报错: It is illegal to call this method if the current request is not in asynchron
         * HttpServletResponse: 不能被序列化, 报错: java.lang.IllegalStateExcetion: getOutputStream() has alreadly been called for this response
         * MultipartFile: fastjson 不能序列化MultipartFile
         */
        Collection<Object> logArgs = stream.filter(arg -> !(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)
                && !(arg instanceof MultipartFile) && !(arg instanceof MultipartFile[])
        ).collect(Collectors.toList());

        // hostIp和端口
        String hostAndPort = request.getRemoteHost() + ":" + request.getRemotePort();

        // 请求方法
        String requestMethod = request.getMethod();

        // 接口路径
        String servletPath = request.getServletPath();

        // 请求参数: 这里没有测试
        String requestInfo = JSONUtil.toJsonStr(logArgs);

        // 请求人
        // UserContext.UserInfo userInfo = UserContext.getInstance.getCurrentUser();
        String operator = "";
//        if (userInfo != null) {
//            operator = userInfo.getEmployeeNumber() + "[" + userInfo.getId() + "]";
//        }

        // 请求人IP
        // String operatorIp = IpAddressUtils.getIPAddress(request);

        // 请求人信息
        // String operatorInfo = operator + operatorIp;

        // 响应信息
        String responseInfo = null;
        long startTime = System.currentTimeMillis();
        try {
            // 返回值
            Object result = proceedingJoinPoint.proceed();
            String jsonStr = JSONUtil.toJsonStr(result);

            if (jsonStr.length() > TRUNCAT_LENTH) {
                jsonStr = jsonStr.substring(0, TRUNCAT_LENTH) + "...";
            }

            requestInfo = jsonStr;

            // 耗时
            long timeConsuming = System.currentTimeMillis() - startTime;

            log.info("{} --- {} --- {} --- {} --- {} --- {} --- {}ms", hostAndPort, requestMethod, servletPath,
                    requestInfo, responseInfo, operator, timeConsuming);
            return result;
        } catch (Throwable t) {
            requestInfo = t.getClass().getName() + ":" + t.getMessage();
            // 耗时
            long timeConsuming = System.currentTimeMillis() - startTime;
            log.info("{} --- {} --- {} --- {} --- {} --- {} --- {}ms", hostAndPort, requestMethod, servletPath,
                    requestInfo, responseInfo, operator, timeConsuming);
            throw t;
        }
    }
}
