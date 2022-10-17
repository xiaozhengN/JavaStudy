package main.daily;

import java.io.Serializable;

/**
 * 懒汉式单例
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-16 15:55:24
 **/
public class Singleton3 implements Serializable {
    // 懒加载
    private static Singleton3 INSTANCE = null;

    // 构造方法私有
    private Singleton3() {
        // 规避反射去破坏单例对象
        if (INSTANCE != null) {
            throw new RuntimeException("单例对象不能重复创建");
        }
        System.out.println("Singleton3 无参构造方法");
    }

    /**
     * 对外提供获取该单例对象的方法
     *
     * @return 单例对象
     */
    public static synchronized Singleton3 getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton3();
        }
        return INSTANCE;
    }

    /**
     * 模拟单例对象其他方法
     */
    public static void otherMethod() {
        System.out.println("main.daily.Singleton3.otherMethod");
    }

    /**
     * 对象中重写 readResolve 方法, 规避反序列化破坏单例对象
     *
     * @return 单例对象
     */
    public Object readResolve() {
        return getInstance();
    }
}
