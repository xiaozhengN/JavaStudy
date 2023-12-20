package base.query;

import org.apache.commons.beanutils.BeanComparator;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-20 23:29:54
 */
public class OrderBy {
     private Map<String, OrderType> orders = new LinkedHashMap<>();

    /**
     * 依据field构建OrderBy对象
     *
     * @param field 排序字段
     * @param type 类型
     */
     public OrderBy(String field, OrderType type) {
         orders.put(field, type);
     }

    /**
     * 指定字段降序
     *
     * @param field 指定字段
     */
     public void orderByDesc(String field) {
         orders.put(field, OrderType.DESC);
     }

    /**
     * 指定字段升序
     *
     * @param field 指定字段
     */
     public void orderByAsc(String field) {
         orders.put(field, OrderType.ASC);
     }

    /**
     * 获取排序内容
     *
     * @return key: 字段, value: 排序类型
     */
    public Map<String, OrderType> getOrders() {
         return orders;
     }

    /**
     * 清除排序内容
     */
    public void clearOrders() {
        this.orders.clear();
     }

    /**
     * 无参构造
     */
    public OrderBy() {}

    /**
     * 复合排序
     *
     * @param orderBy 排序对象
     * @param dataList 元数据
     * @param <T> 泛型
     */
    public static <T> void sort(OrderBy orderBy, List<T> dataList) {
        Comparator<T> comparator = (o1, o2) -> 0;
        for (Map.Entry<String, OrderType> entity : orderBy.getOrders().entrySet()) {
            String field = entity.getKey();
            Comparator<T> nextComparator;
            if (entity.getValue() == OrderType.ASC) {
                nextComparator = new BeanComparator<>(field, Comparator.nullsLast(Comparator.naturalOrder()));
            } else {
                nextComparator = new BeanComparator<>(field, Comparator.nullsLast(Comparator.reverseOrder()));
            }
            comparator = comparator.thenComparing(nextComparator);
        }
        dataList.sort(comparator);
    }

    /**
     * 排序类型
     */
    public enum OrderType {
        DESC,
        ASC
    }
}

