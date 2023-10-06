package daily.spring;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 用于测试的Spring Bean
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-10-05 14:19:46
 */
@Component
public class OrderService implements InitializingBean {
    @Override
    public void afterPropertiesSet() {}

    /**
     * 用户自定义需要初始化的方法
     */
    public void customInitMethod() {}
}
