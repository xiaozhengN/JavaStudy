package main.daily;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * TestThreadLocal: 线程之间资源隔离, 线程内资源共享
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-11-27 22:12:39
 **/
@Slf4j
public class TestThreadLocal {
    public static void main(String[] args) {
        test1();
        test2();
    }

    /**
     * 多个线程调用, 得到的是自己的Connection对象
     */
    private static void test1() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> log.info("当前线程: {}, Connection: {}", Thread.currentThread().getName(), Utils.getConnection()), "test1Thread" + (i + 1)).start();
        }
    }

    /**
     * 一个线程内调用, 得到的是同一个Connection对象
     */
    private static void test2() {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                log.info("当前线程: {}, Connection: {}", Thread.currentThread().getName(), Utils.getConnection());
                log.info("当前线程: {}, Connection: {}", Thread.currentThread().getName(), Utils.getConnection());
            }, "test2Thread" + (i + 1)).start();

        }
    }

    static class Utils {
        // 线程隔离
        private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

        /**
         * 获取连接
         *
         * @return Connection对象
         */
        public static Connection getConnection() {
            Connection connection = threadLocal.get();
            if (connection == null) {
                connection = innerGetConnection();
                threadLocal.set(connection);
            }
            return connection;
        }

        private static Connection innerGetConnection() {
            try {
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/bos?useSSL=false", "root", "root");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
