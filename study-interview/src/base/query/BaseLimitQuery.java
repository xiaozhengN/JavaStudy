package base.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-20 22:48:52
 */
@Data
@ApiModel
public class BaseLimitQuery {
    @ApiModelProperty("查询条件")
    private List<SimpleCondition> conditions;

    @ApiModelProperty("排序参数")
    private OrderBy orderBy;

    @ApiModelProperty("当前页")
    private Integer currentPage;

    @ApiModelProperty("分页大小")
    private Integer pageSize;
}
