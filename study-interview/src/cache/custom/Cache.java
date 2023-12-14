package cache.custom;

import java.lang.annotation.*;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-14 21:54:28
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {
    /**
     * 缓存默认过期时长, 默认10min
     */
    int CACHE_DEFAULT_EXPIRE = 10;

    /**
     * 配置过期时间, 默认10min
     *
     * @return 过期时间
     */
    int expire() default CACHE_DEFAULT_EXPIRE;

    /**
     * 配置缓存结果的阈值, 超过100ms对结果进行缓存
     *
     * @return 缓存的阈值
     */
    int moreThan() default 100;

    /**
     * 配置缓存key名称的前缀
     *
     * @return 缓存key名称的前缀
     */
    String prefix() default "metricCache";

    /**
     * 配置是否进入缓存持续刷新池
     *
     * @return 是否进入缓存持续刷新池
     */
    boolean enterRefreshPool() default true;
}
