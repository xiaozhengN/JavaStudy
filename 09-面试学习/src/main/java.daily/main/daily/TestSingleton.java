package main.daily;

import cn.hutool.core.util.StrUtil;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 单例模式测试类
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-16 13:14:31
 **/
public class TestSingleton {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        System.out.println("------------------------ Singleton1: 饿汉单例开始 ------------------------");
        Singleton1.otherMethod();
        System.out.println(Singleton1.getInstance());
        System.out.println(Singleton1.getInstance());
        destroySingletonByReflection(Singleton1.class, Singleton1.getInstance());
        destroySingletonBySerializable(Singleton1.getInstance(), Singleton1.getInstance().toString());
        destroySingletonByUnsafe(Singleton1.class, Singleton1.getInstance().toString());
        System.out.println("------------------------ Singleton1: 饿汉单例结束 ------------------------");

        System.out.println("------------------------ Singleton2: 枚举饿汉式开始 ------------------------");
        Singleton2.otherMethod();
        System.out.println(Singleton2.getInstance());
        System.out.println(Singleton2.getInstance());
        // 枚举可以自己规避反射去破坏单例
        destroySingletonByReflection(Singleton2.class, Singleton2.getInstance());
        // 枚举可以自己规避反序列化去破坏单例
        destroySingletonBySerializable(Singleton2.getInstance(), Singleton2.getInstance().toString());
        destroySingletonByUnsafe(Singleton2.class, Singleton2.getInstance().toString());
        System.out.println("------------------------ Singleton2: 枚举饿汉式结束 ------------------------");

        System.out.println("------------------------ Singleton3: 懒汉式开始 ------------------------");
        Singleton3.otherMethod();
        System.out.println(Singleton3.getInstance());
        System.out.println(Singleton3.getInstance());
        // 枚举可以自己规避反射去破坏单例
        destroySingletonByReflection(Singleton3.class, Singleton3.getInstance());
        // 枚举可以自己规避反序列化去破坏单例
        destroySingletonBySerializable(Singleton3.getInstance(), Singleton3.getInstance().toString());
        destroySingletonByUnsafe(Singleton3.class, Singleton3.getInstance().toString());
        System.out.println("------------------------ Singleton3: 懒汉式结束 ------------------------");

        System.out.println("------------------------ Singleton4: DCL懒汉式开始 ------------------------");
        Singleton4.otherMethod();
        System.out.println(Singleton4.getInstance());
        System.out.println(Singleton4.getInstance());
        // 枚举可以自己规避反射去破坏单例
        destroySingletonByReflection(Singleton4.class, Singleton4.getInstance());
        // 枚举可以自己规避反序列化去破坏单例
        destroySingletonBySerializable(Singleton4.getInstance(), Singleton4.getInstance().toString());
        destroySingletonByUnsafe(Singleton4.class, Singleton4.getInstance().toString());
        System.out.println("------------------------ Singleton4: DCL懒汉式结束 ------------------------");

        System.out.println("------------------------ Singleton5: 内部类懒汉式开始 ------------------------");
        Singleton5.otherMethod();
        System.out.println(Singleton5.getInstance());
        System.out.println(Singleton5.getInstance());
        // 枚举可以自己规避反射去破坏单例
        destroySingletonByReflection(Singleton5.class, Singleton5.getInstance());
        // 枚举可以自己规避反序列化去破坏单例
        destroySingletonBySerializable(Singleton5.getInstance(), Singleton5.getInstance().toString());
        destroySingletonByUnsafe(Singleton5.class, Singleton5.getInstance().toString());
        System.out.println("------------------------ Singleton5: 内部类懒汉式结束 ------------------------");
    }

    // Unsafe 破坏单例
    private static void destroySingletonByUnsafe(Class<?> singletonClass, String singletonStr) throws InstantiationException {
        Object obj = UnsafeUtils.getUnsafe().allocateInstance(singletonClass);
        System.out.println("反序列化是否已经被Unsafe破坏: " + !StrUtil.equals(obj.toString(), singletonStr));
    }

    // 反序列化破坏单例
    private static void destroySingletonBySerializable(Object instance, String singletonStr) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(instance);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        String newStr = ois.readObject().toString();
        System.out.println("反序列化是否已经被Serializable破坏: " + !StrUtil.equals(newStr, singletonStr));
    }

    // 反射破坏单例
    private static void destroySingletonByReflection(Class<?> singletonClass, Object singletonObj) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 通过反射获取私有构造方法
        if (singletonObj instanceof Singleton1 || singletonObj instanceof Singleton3 || singletonObj instanceof Singleton4 || singletonObj instanceof Singleton5) {
            Constructor<?> declaredConstructor = singletonClass.getDeclaredConstructor();
            // 开启访问权限
            declaredConstructor.setAccessible(true);
            // 执行私有构造方法
            String newStr;
            try {
                newStr = declaredConstructor.newInstance().toString();
                System.out.println("单例模式是否已经被Reflection破坏: " + !StrUtil.equals(newStr, singletonObj.toString()));
            } catch (Exception e) {
                System.out.println("反射破坏单例模式, 已通过抛出异常规避!");
            }
        }

        if (singletonObj instanceof Singleton2) {
            Constructor<?> declaredConstructor = singletonClass.getDeclaredConstructor(String.class, int.class);
            // 开启访问权限
            declaredConstructor.setAccessible(true);
            String newStr;
            try {
                newStr = declaredConstructor.newInstance("OTHER", 3).toString();
                System.out.println("单例模式是否已经被Reflection破坏: " + !StrUtil.equals(newStr, singletonObj.toString()));
            } catch (Exception e) {
                System.out.println("反射破坏枚举单例模式, 已通过抛出异常规避!");
            }
        }
    }
}
