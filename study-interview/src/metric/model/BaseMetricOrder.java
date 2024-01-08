package metric.model;

import base.query.OrderBy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2024-01-09 4:50:40
 */
public class BaseMetricOrder {
    /**
     * 分隔符: --
     */
    public static final String SEPARATOR_DOUBLE_CROSSBAR = "- -";

    /**
     * 分隔符: %
     */
    public static final String SEPARATOR_PERCENT = "%";

    /**
     * 指标VO类型排序
     *
     * @param metricVoList VOList
     * @param orderBy 排序对象
     * @param filterKey 需要特殊处理的指标
     * @param filterValue 需要特殊处理的指标值
     * @return 排序后的List
     * @param <T> 各页面VO
     */
    public static <T extends BaseMetricVO> List<T> order(List<T> metricVoList, OrderBy orderBy, String filterKey,
                                                         String filterValue
             , OrderFieldType orderFieldType
    ) {
        List<T> sortList = new ArrayList<>(metricVoList.size());

        List<T> filterUnassigned = metricVoList.stream()
                .filter(item -> filterValue.equals(item.getMetricValue(filterKey))).collect(Collectors.toList());

        List<T> filterList = metricVoList.stream()
                .filter(item -> !filterValue.equals(item.getMetricValue(filterKey)))
                .collect(Collectors.toList());

        Comparator<T> comparator = (o1, o2) -> 0;

        for (Map.Entry<String, OrderBy.OrderType> entry : orderBy.getOrders().entrySet()) {
            Comparator<T> nextComparator;
            if (OrderFieldType.STRING == orderFieldType) {
                nextComparator = Comparator.comparing(vo-> {
                    String metricValue = vo.getMetricValue(entry.getKey());
                    if (specialValue(metricValue)) {
                        return null;
                    }
                    return metricValue;
                }, Comparator.nullsLast(String::compareTo).reversed());
            } else {
                nextComparator = Comparator.comparingDouble(vo -> {
                    String metricValue = vo.getMetricValue(entry.getKey());
                    if (!specialValue(metricValue)) {
                        return Double.parseDouble(dealMetricValue(metricValue));
                    }
                    if (entry.getValue() == OrderBy.OrderType.DESC) {
                        return Double.MIN_EXPONENT;
                    }
                    return Double.MAX_VALUE;
                });
            }
            if (entry.getValue() == OrderBy.OrderType.DESC) {
                nextComparator = nextComparator.reversed();
            }
            comparator = comparator.thenComparing(nextComparator);
        }

        filterList.sort(comparator);
        sortList.addAll(filterList);
        sortList.addAll(filterUnassigned);
        return sortList;
    }

    private static boolean specialValue(String mericValue) {
        return StringUtils.isEmpty(mericValue) ||
                "0".equals(mericValue)
                || mericValue.contains(SEPARATOR_DOUBLE_CROSSBAR);
    }

    private static String dealMetricValue(String metricValue) {
        if (metricValue.contains(SEPARATOR_PERCENT)) {
            metricValue = metricValue.substring(0, metricValue.indexOf(SEPARATOR_PERCENT));
        }
        return metricValue;
    }

    public enum OrderFieldType {
        STRING,
        DOUBLE
    }
}
