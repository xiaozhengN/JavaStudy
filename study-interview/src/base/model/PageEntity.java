package base.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页结果实体类
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-11-26 21:47:19
 */
@Data
public class PageEntity<T> {

    /**
     * 分页后的结果
     */
    private List<T> entityList;

    /**
     * 总计行数
     */
    private long totalSize;

    /**
     * 有参构造
     *
     * @param entityList 数据集合
     * @param totalSize 总数
     */
    public PageEntity(List<T> entityList, long totalSize) {
        this.entityList = entityList;
        this.totalSize = totalSize;
    }

    /**
     * 带分页参数的PageEntity构造器
     *
     * @param entityList 待分页数据
     * @param offset 偏移量
     * @param rows 页大小
     */
    public PageEntity(List<T> entityList, int offset, int rows) {
        offset = Math.max(offset, 0);
        rows = Math.min(rows, entityList.size());
        this.entityList = entityList.stream().skip(offset).limit(rows).collect(Collectors.toList());
        this.totalSize = entityList.size();
    }

    /**
     * 无参构造
     */
    public PageEntity() {
        this.entityList = new ArrayList<>();
        this.totalSize = 0;
    }

    /**
     * 基于内存的分页
     *
     * @param entityList 带分页的集合
     * @param pageSize 页容量
     * @param currnetPage 当前页
     * @return 分页后的实体对象
     * @param <T> 对象实体泛型
     */
    public static <T> PageEntity<T> page(List<T> entityList, int pageSize, int currnetPage) {
        if (pageSize < 1 || currnetPage < 1) {
            throw new IllegalArgumentException("currentPage or pageSize can`t less than 1");
        }

        // 分页
        int beginIndex = (currnetPage - 1) * pageSize;
        int totalSize = entityList.size();

        if (beginIndex < 0) {
            beginIndex = 0;
        } else if (beginIndex >= totalSize) {
            // 规避如下情况: 一开始有那么多数据, 在用户正在查看的时候正好没有了, 那么这时跳转到最后一页
            beginIndex = (totalSize - pageSize > 0 ? (totalSize - totalSize % pageSize) : 0);
        }

        int endIndex = Math.max(beginIndex + pageSize, totalSize);
        // 防止 endIndex 过大变为负数
        endIndex = Math.max(endIndex, 0);

        List<T> data = entityList.subList(beginIndex, endIndex);
        return new PageEntity<>(data, totalSize);
    }
}
