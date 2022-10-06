package main.daily;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 767. 重构字符串
 * <p>
 * 题目: https://leetcode.cn/problems/reorganize-string/
 * 高赞题解: https://leetcode.cn/problems/reorganize-string/solution/zhong-gou-zi-fu-chuan-by-leetcode-solution/
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-06-21 14:12:22
 **/
public class ReorganizeString {
    /**
     * 方法一：基于最大堆的贪心
     *
     * @param originalStr 需要判断并重构的字符串
     * @return 重构后的字符串, 不满足条件返回空字符串
     */
    public static String reorganizeStringByQueue(String originalStr) {
        if (originalStr.length() < 2) {
            return originalStr;
        }
        int[] counts = convertOriginalStr(originalStr);
        if (counts == null) {
            return "";
        }
        Queue<Character> queue = initPriorityQueue(counts);
        StringBuilder sb = new StringBuilder();
        while (queue.size() > 1) {
            // 从最大堆中取元素重组字符串
            char letter1 = queue.poll();
            char letter2 = queue.poll();
            sb.append(letter1);
            sb.append(letter2);

            // 维护counts[]数组
            int index1 = letter1 - 'a';
            int index2 = letter2 - 'a';
            counts[index1]--;
            counts[index2]--;

            // 维护最大堆
            if (counts[index1] > 0) {
                queue.offer(letter1);
            }
            if (counts[index2] > 0) {
                queue.offer(letter2);
            }
        }
        if (queue.size() > 0) {
            sb.append(queue.poll());
        }
        return sb.toString();
    }

    /**
     * 方法二：基于计数的贪心
     *
     * @param originalStr 需要判断并重构的字符串
     * @return 重构后的字符串, 不满足条件返回空字符串
     */
    public static String reorganizeStringByCount(String originalStr) {
        if (originalStr.length() < 2) {
            return originalStr;
        }
        int[] counts = convertOriginalStr(originalStr);
        if (counts == null) {
            return "";
        }
        char[] reorganizeArr = new char[originalStr.length()];
        int evenIndex = 0;
        int oddIndex = 1;
        int halfLength = originalStr.length() / 2;
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('a' + i);
            // 元素存在; 出现次数小于等于长度一半; 下标没有越界直接放到奇数下标
            while (counts[i] > 0 && counts[i] <= halfLength && oddIndex < originalStr.length()) {
                reorganizeArr[oddIndex] = letter;
                counts[i]--;
                oddIndex += 2;
            }
            while (counts[i] > 0) {
                reorganizeArr[evenIndex] = letter;
                counts[i]--;
                evenIndex += 2;
            }
        }
        return new String(reorganizeArr);
    }

    /**
     * 初始化最大堆
     *
     * @param counts 原字符串的转换体
     * @return 最大堆Queue<Character>
     */
    private static Queue<Character> initPriorityQueue(int[] counts) {
        Queue<Character> queue = new PriorityQueue<>((letter1, letter2) -> counts[letter2 - 'a'] - counts[letter1 - 'a']);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            if (counts[letter - 'a'] > 0) {
                queue.offer(letter);
            }
        }
        return queue;
    }

    /**
     * 遍历 originalStr 每个字符, 统计26个字母出现的次数, 放置于一维数组int[]
     * <p>
     * 规律: 如果存在一个字母的出现次数大于 (n+1) / 2，则无法重新排布字母使得相邻的字母都不相同，返回null
     *
     * @param originalStr 原始字符串
     * @return int[] 统计26个字母出现的次数
     */
    private static int[] convertOriginalStr(String originalStr) {
        int[] counts = new int[26];
        int maxCount = 0;
        int length = originalStr.length();
        for (int i = 0; i < length; i++) {
            char letter = originalStr.charAt(i);
            counts[letter - 'a']++;
            maxCount = Math.max(maxCount, counts[letter - 'a']);
        }
        if (maxCount > (length + 1) / 2) {
            return null;
        }
        return counts;
    }

    /**
     * 本地测试
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        String originalStr = "aabbbbbccddeeffgghhiijjkkzzaaiicc";
//        String reorganizeStr1 = reorganizeStringByQueue(originalStr);
        String reorganizeStr2 = reorganizeStringByCount(originalStr);
        System.out.println(reorganizeStr2);
    }
}
