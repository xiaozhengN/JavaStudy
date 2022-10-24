package main.daily;

import lombok.extern.slf4j.Slf4j;

/**
 * 面试题: wait和sleep
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-23 10:25:28
 **/
@Slf4j
public class WaitVsSleep {

    static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        // wait 必须配合锁一起使用
        illegalWait();
        waitting();
        sleeping(false);
        sleeping(true);
    }

    private static void sleeping(Boolean isInterrupted) throws InterruptedException {
        log.info("test sleeping() start, param is {}", isInterrupted);
        Thread t1 = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    log.info("current thread: {} sleeping...", Thread.currentThread().getName());
                    // Thread.sleep() 会继续持有 LOCK 对象的锁资源, 直到自己的事情做完(5s)
                    Thread.sleep(5000L);
                    log.info("current thread: {} sleeping end...", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    log.info("current thread: {} has interrupted...", Thread.currentThread().getName(), e);
                }
            }
        }, "T1");
        t1.start();
        Thread.sleep(100);
        if (isInterrupted) {
            t1.interrupt();
        }
        synchronized (LOCK) {
            log.info("current thread: {} other...", Thread.currentThread().getName());
        }
        log.info("test sleeping() end...");
    }

    private static void waitting() throws InterruptedException {
        log.info("test waitting() start...");
        new Thread(() -> {
            synchronized (LOCK) {
                try {
                    log.info("current thread: {} waiting...", Thread.currentThread().getName());
                    // wait() 不会去持续 5s 去占有 LOCK 的锁资源
                    LOCK.wait(5000L);
                } catch (InterruptedException e) {
                    log.info("current thread: {} interrupted...", Thread.currentThread().getName(), e);
                }
            }
        }, "T1").start();
        Thread.sleep(100);
        synchronized (LOCK) {
            log.info("current thread: {} other...", Thread.currentThread().getName());
        }
        log.info("test waitting() end...");
    }

    private static void illegalWait() throws InterruptedException {
        try {
            LOCK.wait();
        } catch (Exception e) {
            log.error("执行 sleep 异常: ", e);
        }
    }
}
