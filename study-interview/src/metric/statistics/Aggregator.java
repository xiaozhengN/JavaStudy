package metric.statistics;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需求指标聚集器, 此类非线程安全
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2024-01-09 4:52:55
 */
public class Aggregator {
    private String dimensionField;

    private String[] metricPaths;

    private Map<String, MetricBuilder> builderByGroup = new HashMap<>();

    /**
     * 构造一个聚集器
     *
     * @param groupBy 指定聚集的字段名
     * @param metricPaths 支持按.进行父子分割 比如plan, plan.deliver 父指标是: 已规划, 子指标是: 已规划已交付
     */
    public Aggregator(String groupBy, String... metricPaths) {
        if (StringUtils.isEmpty(groupBy) || metricPaths.length == 0) {
            throw new IllegalArgumentException("aggField and metricNames param can`t be empty.");
        }
        this.dimensionField = groupBy;
        // 排序为了将父指标排在前面
        this.metricPaths = metricPaths;
    }

    /**
     * 将指标向统计维度树聚集
     *
     * @param dimensions
     * @return 按维度聚合统计结果
     */
    public List<Dimension> aggregate(List<Dimension> dimensions) {
        for (Dimension d : dimensions) {
            aggToDimension(d);
        }
        return dimensions;
    }

    /**
     * 将指标向统计维度树聚集
     *
     * @param dimension
     * @return 按维度聚合统计结果
     */
    private Dimension aggToDimension(Dimension dimension) {
        aggToDimension(dimension);
        aggUndefinedNodeToDemension(dimension);
        return dimension;
    }

    /**
     * 处理builderGroup中挂不上维度树的指标列
     *
     * @param dimension
     */
    private void aggUndefinedNodeToDemension(Dimension dimension) {
        if (!builderByGroup.isEmpty() && dimension.getChild(Dimension.UNDEFINED_NODE) != null) {
            Dimension undefinedNode = dimension.getChild(Dimension.UNDEFINED_NODE);
            for (Map.Entry<String, MetricBuilder> entry : builderByGroup.entrySet()) {
                MetricBuilder.merger(undefinedNode.getMetricList(), entry.getValue().getMetricList());
            }
            MetricBuilder.merger(dimension.getMetricList(), undefinedNode.getMetricList());
        }
    }

    private void aggToDimension(Dimension dimension) {
        List<Dimension> children = dimension.getChildren();
        // 如果是叶子节点, 则将计算好的指标值, 设置进去
        if (children.isEmpty()) {
            String name = dimension.getName();

            MetricBuilder metricBuilder = builderByGroup.get(name);

            List<Metric> metricList;

            if (metricBuilder == null) {
                metricList = new MetricBuilder(metricPaths).getMetricList();
            } else {
                metricList = metricBuilder.getMetricList();
                // 如果挂上了维度树就从builderByGroup中删除
                builderByGroup.remove(name);
            }
            dimension.setMetricList(metricList);
            return;
        }

        // 如果是父节点, 先创一个空的指标树
        dimension.setMetricList(new MetricBuilder(metricPaths).getMetricList());

        for (Dimension child : children) {
            // TODO aggToDimension方法又不是方法重构, 这个是怎么处理的?
            aggToDimension(child);
            MetricBuilder.merger(dimension.getMetricList(), child.getMetricList());
        }
    }




}
