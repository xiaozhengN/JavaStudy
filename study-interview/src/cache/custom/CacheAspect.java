package cache.custom;

import cache.redis.RedisCacheClient;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 缓存切面
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-17 16:01:20
 */
@Component
@Aspect
@Slf4j
public class CacheAspect {
    @Autowired
    private RedisCacheClient redisCacheClient;

    @Autowired
    private CacheKeyLruPool cacheKeyLruPool;

    @Value("${spring.application.name}")
    private String appName;

    private static final String NO_CACHE_MODE_HEADER = "NO-CACHE";

    @Around("@annotation(cache)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
        if (isNoCacheMode()) {
            // TODO 当前用户这里是伪代码
            log.info("无缓存模式访问. 调用方法: {}, 当前用户: {}", joinPoint, "System");
            return joinPoint.proceed();
        }

        // 1. 如果缓存存在, 直接返回
        String key = getKey(cache.prefix(), joinPoint.getArgs());
        try {
            Object resultObject = redisCacheClient.get(key);
            if (resultObject != null) {
                // 更新缓存
                cacheKeyLruPool.refreshIfExist(key);
                return resultObject;
            }
        } catch (Exception e) {
            log.error("Redis get value error", e);
            joinPoint.proceed();
        }

        // 2. 执行查询, 做耗时统计
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long costTime = System.currentTimeMillis() - startTime;

        // 3. 通过耗时判断是否需要缓存该结果
        if (cache.enterRefreshPool() && costTime > CacheKeyLruPool.standardCostTime) {
            // TODO 这里找不到包
//            MethodInvoker methodInvoker = SpringContextUtil.getMethodInvker(joinPoint);
//            cacheKeyLruPool.add(key, methodInvoker);
        }

        // 4. 缓存结果
        if (costTime > cache.moreThan()) {
            try {
                redisCacheClient.set(key, result, cache.expire());
            } catch (Exception e) {
                log.error("Redis set value error", e);
            }
        }
        return result;
    }

    /**
     * 将入参序列化成json字符串
     *
     * @param prefix 缓存前缀
     * @param args   方法入参
     * @return 缓存key
     */
    private String getKey(String prefix, Object[] args) {
        return appName + ":" + prefix + ":" + JSONArray.toJSONString(args);
    }

    /**
     * 判断是否为无缓存模式
     *
     * @return 无缓存模式返回true, 反之返回, false
     */
    private boolean isNoCacheMode() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return false;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return "true".equals(request.getHeader(NO_CACHE_MODE_HEADER));
    }
}
