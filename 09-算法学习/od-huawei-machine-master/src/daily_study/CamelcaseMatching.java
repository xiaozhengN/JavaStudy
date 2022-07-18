package daily_study;

import java.util.ArrayList;
import java.util.List;

/**
 * 1023. 驼峰式匹配
 * <p>
 * 题目: https://leetcode.cn/problems/camelcase-matching/
 * 高赞题解: https://leetcode.cn/problems/reorganize-string/solution/zhong-gou-zi-fu-chuan-by-leetcode-solution/
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-06-23 21:32:04
 **/
public class CamelcaseMatching {

    /**
     * 驼峰式匹配
     *
     * @param queries 待查询项数组
     * @param pattern 模式串
     * @return
     */
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> res = new ArrayList<>();
        // 发现的规律: queries[i] 和 pattern 大写字母按照顺序找处理, 不相等就false
        for (String query : queries) {
            res.add(matchPattern(query, pattern));
        }
        return res;
    }

    /**
     * 匹配
     *
     * @param query   待查询项
     * @param pattern 模式串
     */
    private boolean matchPattern(String query, String pattern) {
        char[] queryChars = query.toCharArray();
        // 记录 query 的大写字母
        StringBuilder sb1 = new StringBuilder();
        // 记录 pattern 的大写字母
        StringBuilder sb2 = new StringBuilder();
        for (char letter : queryChars) {
            if (letter >= 'A' && letter <= 'Z') {
                sb1.append(letter);
            }
        }
        char[] patternChars = pattern.toCharArray();
        for (char letter : patternChars) {
            if (letter >= 'A' && letter <= 'Z') {
                sb2.append(letter);
            }
        }
        if (sb1.toString().equals(sb2.toString())) {
            return true;
        }
        return false;
    }
}
