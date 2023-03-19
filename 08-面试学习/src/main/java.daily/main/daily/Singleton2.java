package main.daily;

/**
 * 枚举饿汉式
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-16 14:43:15
 **/
public enum Singleton2 {
    INSTANCE;

    // 枚举无参构造默认私用化
    Singleton2() {
        System.out.println("Singleton2 无参构造方法");
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    /**
     * 对外提供获取单例对象方法
     *
     * @return 单例对象
     */
    public static Singleton2 getInstance() {
        return INSTANCE;
    }

    /**
     * 模拟其他方法: 主要用于监测是否是饿汉式
     */
    public static void otherMethod() {
        System.out.println("Singleton2 其他方法");
    }
}
