package base.query;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-21 0:08:44
 */
public class CompositeCondition extends QueryCondition{
    private QueryCondition left;

    private QueryCondition right;

    private boolean isAnd = true;
}