package daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-06-21 20:38:19
 **/
public class Solution {

    static class BrowserHistory {
        Node page;
        Node temp = page;

        /**
         * 有参构造初始化 BrowserHistory
         *
         * @param homepage 起始页
         */
        public BrowserHistory(String homepage) {
            page = new Node(homepage);
        }

        /**
         * 访问指定url
         *
         * @param url 指定URL
         */
        public void visit(String url) {
            Node newPage = new Node(url);
            newPage.next = null;
            page.next = newPage;
            newPage.pre = page;
            page = page.next;
        }

        /**
         * 后退
         *
         * @param steps 后退的步数
         * @return 后退后, 返回的URL
         */
        public String back(int steps) {
            while (steps != 0) {
                if (page.pre == temp) {
                    break;
                } else {
                    page = page.pre;
                    steps--;
                }
            }
            return page.str;
        }

        /**
         * 前进
         *
         * @param steps 前进的步数
         * @return 前进后返回的URL
         */
        public String forward(int steps) {
            while (steps != 0) {
                if (page.next == null) {
                    break;
                } else {
                    page = page.next;
                    steps--;
                }
            }
            return page.str;
        }
    }

    /**
     * 自定义节点
     */
    static class Node {
        String str;
        Node pre;
        Node next;

        Node(String str) {
            this.str = str;
        }
    }

    public static void main(String[] args) {
        /**
         * ["BrowserHistory","visit","visit","visit","back","back","forward","visit","forward","back","back"]
         * [["leetcode.com"],["google.com"],["facebook.com"],["youtube.com"],[1],[1],[1],["linkedin.com"],[2],[2],[7]]
         *
         * 断言:
         * [null,null,null,null,"facebook.com","google.com","facebook.com",null,"linkedin.com","google.com","leetcode.com"]
         */
        List<String> res = new ArrayList<>();
        BrowserHistory obj = new BrowserHistory("leetcode.com");
        obj.visit("google.com");
        res.add(null);
        obj.visit("facebook.com");
        res.add(null);
        obj.visit("youtube.com");
        res.add(null);
        String param_1 = obj.back(1);
        res.add(param_1);
        String param_2 = obj.back(1);
        res.add(param_2);
        String param_3 = obj.forward(1);
        res.add(param_3);
        obj.visit("linkedin.com");
        res.add(null);
        String param_4 = obj.forward(2);
        res.add(param_4);
        String param_5 = obj.back(2);
        res.add(param_5);
        String param_6 = obj.back(7);
        res.add(param_6);
        System.out.println(res);
    }
}