package model;

import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * LinkedHashMap是有顺序的Map
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2023-04-24 22:15:20
 **/
public class TestLinkedHashMap {
    public static void main(String[] args) {
        // 测试集合1
        HashMap<Object, Object> tempMap1 = new LinkedHashMap<>();
        tempMap1.put(2, 2);
        tempMap1.put(1, 1);
        tempMap1.put(3, 3);
        tempMap1.put(4, 4);
        tempMap1.put(5, 5);

        // 测试集合2
        HashMap<Object, Object> tempMap2 = new LinkedHashMap<>();
        tempMap2.put(7, 7);
        tempMap2.put(6, 6);
        tempMap2.put(8, 8);
        tempMap2.put(9, 9);
        tempMap2.put(10, 10);

        // 待验证的集合res
        HashMap<Object, Object> res = new LinkedHashMap<>(tempMap1);
        if (MapUtil.isNotEmpty(tempMap2)) {
            res.putAll(tempMap2);
        }

        System.out.println(res);
    }
}
