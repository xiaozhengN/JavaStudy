package base.query;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-20 22:50:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SimpleCondition extends QueryCondition implements Serializable {
    // 字段
    private String field;

    // 值
    private Object values;

    // 符号位
    private Match match = Match.EQUAL;

    /**
     * 无参构造
     */
    @Tolerate
    public SimpleCondition() {}

    /**
     * 有参构造
     *
     * @param field 字段
     * @param values 值
     * @param match 符号位
     */
    public SimpleCondition(String field, Object values, Match match) {
        this.field = field;
        this.values = values;
        this.match = match;
    }

    public void eq(String field, Object values) {
        this.field = field;
        this.values = values;
    }

    public String getSymbol() {
        return this.match.symbol;
    }

    @Getter
    public enum Match {
        EQUAL("equal", "="),
        NOT_EQUAL("notEqual", "!="),
        CONTAIN("contain"),
        IN("in"),
        NOT_IN("notIn"),
        START_WITH("startWith"),
        END_WITH("endWith"),
        NOT_NULL("notNull"),
        IS_NULL("isNull"),
        GREATER_THAN("gt", ">"),
        GREATER_EQUAL("ge", ">="),
        LESS_THAN("lt", "<"),
        LESS_EQUAL("le", "<="),
        INCLUDE("include"),
        EXCLUDE("exclude"),
        ORDER_BY("orderBy"),
        SELECT("select"),
        GROUP_BY("group by"),
        BETWEEN("between");

        private String value;

        private String symbol;

        Match(String value) {
            this.value = value;
        }

        Match(String value, String symbol) {
            this.value = value;
            this.symbol = symbol;
        }
    }

}
