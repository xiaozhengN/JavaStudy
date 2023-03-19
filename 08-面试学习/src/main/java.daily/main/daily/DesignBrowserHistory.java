package main.daily;

import java.util.Stack;

/**
 * 1472. 设计浏览器历史记录
 * <p>
 * 题目: https://leetcode.cn/problems/design-browser-history/
 * 高赞题解: https://leetcode.cn/problems/design-browser-history/solution/java-by-leo-72-60gn/
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-07-03 09:12:33
 **/
public class DesignBrowserHistory {
    /**
     * 记录访问历史的栈
     */
    public Stack<String> historyStack = new Stack<>();

    /**
     * 记录将来的访问记录栈
     */
    public Stack<String> futureStack = new Stack<>();

    /**
     * 有参构造初始化浏览器历史记录类
     *
     * @param homepage 指定的起始页
     */
    public DesignBrowserHistory(String homepage) {
        historyStack.push(homepage);
    }

    /**
     * 访问指定URL地址
     * <p>
     * 从当前页跳转访问 url 对应的页面  。执行此操作会把浏览历史前进的记录全部删除。
     *
     * @param url 指定的url地址
     */
    public void visit(String url) {
        new DesignBrowserHistory(url);
        futureStack.clear();
    }

    /**
     * 回退
     * <p>
     * 在浏览历史中后退 steps 步。如果你只能在浏览历史中后退至多 x 步且 steps > x ，那么你只后退 x 步。请返回后退 至多 steps 步以后的 url 。
     *
     * @param steps 回退的步数
     * @return 返回指定URL地址
     */
    public String back(int steps) {
        // 我们希望 steps 不要太大, 如果过大直接取 historyStack.size()
        if (steps > historyStack.size()) {
            steps = historyStack.size();
        }
        for (int i = 0; i < steps; i++) {
            futureStack.push(historyStack.pop());
        }
        return historyStack.pop();
    }

    /**
     * 前进
     *
     * @param steps 前进的步数
     * @return 返回指定URL地址
     */
    public String forward(int steps) {
        // 我们希望 steps 不要太大, 如果过大直接取 historyStack.size()
        if (steps > futureStack.size()) {
            steps = futureStack.size();
        }
        for (int i = 0; i < steps; i++) {
            historyStack.push(futureStack.pop());
        }
        return historyStack.pop();
    }
}
