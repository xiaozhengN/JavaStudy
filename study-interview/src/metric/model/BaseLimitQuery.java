package metric.model;

import base.query.OrderBy;
import base.query.SimpleCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2024-01-09 4:42:39
 */
@Data
@ApiModel
public class BaseLimitQuery {
    @ApiModelProperty("模型查询条件")
    List<SimpleCondition> conditions;

    @ApiModelProperty("排序参数")
    OrderBy orderBy;

    @ApiModelProperty("分页条件:currentPage,pageSize")
    String limit;

    public void convertOrder() {
        OrderBy order = getOrderBy();
        if (order != null) {
            order.getOrders().forEach((field, orderType) -> getConditions().add(SimpleCondition.builder().values(orderType).match(SimpleCondition.Match.ORDER_BY).field(field).build()));
        }
    }
}
