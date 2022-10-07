package main.daily;

import java.util.Stack;

/**
 * 71. 简化路径
 * <p>
 * 题目: https://leetcode.cn/problems/simplify-path/
 * 高赞题解:
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-07-02 11:08:42
 **/
public class SimplifyPath {
    /**
     * 方法一: 基于栈的简化路径
     *
     * @param path 需要简化的路径
     * @return
     */
    public static String simplifyPath(String path) {
        Stack<String> dirPathStack = new Stack<>();
        /**
         * 1. .
         * 2. ..
         * 3. 目录
         */
        String[] dirPathArr = path.split("/");
        StringBuilder resBuilder = new StringBuilder("/");
        for (String dirPath : dirPathArr) {
            if (!"".equals(dirPath)) {
                if (!".".equals(dirPath) && !"..".equals(dirPath)) {
                    dirPathStack.push(dirPath);
                }
                if (".".equals(dirPath)) {
                    continue;
                }
                if ("..".equals(dirPath) && !dirPathStack.isEmpty()) {
                    dirPathStack.pop();
                }
            }
        }
        if (dirPathStack.isEmpty()) {
            resBuilder.append("/");
        }
        while (!dirPathStack.isEmpty()) {
            for (String pop : dirPathStack) {
                if (!"".equals(pop)) {
                    resBuilder.append(pop).append("/");
                }
            }
            dirPathStack.clear();
        }
        return resBuilder.substring(0, resBuilder.toString().length() - 1);
    }
}
