package metric.computer;

import lombok.Data;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2024-01-09 4:50:01
 */
@Data
public class CompositeMetric {
    /**
     * 复合指标名称
     */
    String metricName;

    /**
     * 复合指标计算公式
     */
    String formula;
}
