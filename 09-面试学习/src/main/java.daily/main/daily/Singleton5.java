package main.daily;

import java.io.Serializable;

/**
 * 懒汉式单例-内部类
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-16 16:52:51
 **/
public class Singleton5 implements Serializable {
    private Singleton5() {
        if (Holder.INSTANCE != null) {
            throw new RuntimeException("单例对象不能重复创建");
        }
        System.out.println("Singleton5 无参构造");
    }

    private static class Holder {
        static Singleton5 INSTANCE = new Singleton5();
    }

    /**
     * 对外提供获取该单例对象的方法
     *
     * @return 单例对象
     */
    public static Singleton5 getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 模拟单例对象其他方法
     */
    public static void otherMethod() {
        System.out.println("main.daily.Singleton5.otherMethod");
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
