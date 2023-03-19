package daily;

import java.io.Serializable;

/**
 * 单例模式: 饿汉式
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-16 12:55:49
 **/
public class Singleton1 implements Serializable {
    // 饿汉式: 一开始就创建对象
    private static final Singleton1 INSTANCE = new Singleton1();

    /**
     * 构造方法私有化
     */
    private Singleton1() {
        // 规避反射去破坏单例对象
        if (INSTANCE != null) {
            throw new RuntimeException("单例对象不能重复创建");
        }
        System.out.println("私有的无参构造");
    }

    /**
     * 对外提供获取该对象的静态方法
     *
     * @return 单例对象
     */
    public static Singleton1 getInstance() {
        return INSTANCE;
    }

    /**
     * 模拟类其他方法
     */
    public static void otherMethod() {
        System.out.println("对象其他方法");
    }

    /**
     * 对象中重写 readResolve 方法, 规避反序列化破坏单例对象
     *
     * @return 单例对象
     */
    public Object readResolve() {
        return INSTANCE;
    }
}
