package daily.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 一个测试类
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-10-05 14:21:30
 */
@ComponentScan("daily.spring")
public class AppConfig {
    /**
     * 注册Bean, 并指定初始化方法
     *
     * @return OrderService
     */
    @Bean(initMethod = "customInitMethod")
    public OrderService generateOrderService() {
        return new OrderService();
    }
}
