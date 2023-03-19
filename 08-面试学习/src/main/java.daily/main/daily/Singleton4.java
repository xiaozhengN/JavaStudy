package main.daily;

import java.io.Serializable;

/**
 * 懒汉式单例-DCL(Double Checked Locked)
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-16 16:06:50
 **/
public class Singleton4 implements Serializable {
    // 懒加载
    private static volatile Singleton4 INSTANCE = null;

    // 无参构造私有化
    private Singleton4() {
        if (INSTANCE != null) {
            throw new RuntimeException("单例对象不能重复创建");
        }
        System.out.println("Singleton4 无参构造");
    }

    /**
     * 对外提供获取该单例对象的方法
     *
     * @return 单例对象
     */
    public static Singleton4 getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton4.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton4();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 模拟单例对象其他方法
     */
    public static void otherMethod() {
        System.out.println("main.daily.Singleton4.otherMethod");
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
