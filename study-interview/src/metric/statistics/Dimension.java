package metric.statistics;

import lombok.Data;

import java.util.List;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2024-01-09 4:53:28
 */
@Data
public class Dimension {

    private String name;

    // TODO
    public static final Dimension UNDEFINED_NODE = new Dimension();

    public Dimension getChild(Object undefinedNode) {
        // TODO
        return null;
    }

    // TODO
    public Object getMetricList() {
        return null;
    }

    // TODO
    public List<Dimension> getChildren() {

        return null;
    }

    // TODO
    public void setMetricList(List<Metric> metricList) {

    }
}
