package main.daily;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * JDK Lock 锁分析
 * <p>
 * --add-opens java.base/java.util.concurrent=ALL-UNNAMED --add-opens java.base/java.util.concurrent.locks=ALL-UNNAMED
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-23 16:41:59
 **/
@Slf4j
public class TestReentrantLock {
    static final MyReentrantLock LOCK = new MyReentrantLock(true);

    static Condition c1 = LOCK.newCondition("c1");
    static Condition c2 = LOCK.newCondition("c2");

    static volatile boolean stop = false;

    public static void main(String[] args) throws InterruptedException, IOException {
        learnLock();
    }

    private static void learnLock() throws InterruptedException {
        log.info("LOCK: {}", LOCK);
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
        }, "t1").start();

        Thread.sleep(100);
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
        }, "t2").start();

        Thread.sleep(100);
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
        }, "t3").start();

        Thread.sleep(100);
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
        }, "t4").start();
    }

    private static void fairVsUnfair() throws InterruptedException {
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
            sleep1s();
            LOCK.unlock();
        }, "t1").start();

        Thread.sleep(100);
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
            sleep1s();
            LOCK.unlock();
        }, "t2").start();

        Thread.sleep(100);
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
            sleep1s();
            LOCK.unlock();
        }, "t3").start();

        Thread.sleep(100);
        new MyThread(() -> {
            LOCK.lock();
            log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
            sleep1s();
            LOCK.unlock();
        }, "t4").start();

        log.info("{}", LOCK);

        while (!stop) {
            new Thread(() -> {
                try {
                    boolean b = LOCK.tryLock(10, TimeUnit.MILLISECONDS);
                    if (b) {
                        log.info("current thread: {}, acquire lock...", Thread.currentThread().getName());
                        stop = true;
                        sleep1s();
                        LOCK.unlock();
                    }
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }).start();
        }
    }

    /**
     * 自定义的 MyReentrantLock, 主要重写了 toString() 方法
     */
    private static class MyReentrantLock extends ReentrantLock {
        /**
         * key: 条件队列/条件变量名称, value: 条件队列/条件变量
         */
        private final Map<String, Condition> conditionMap = new HashMap<>();

        /**
         * 有参构造
         *
         * @param fair 是否构造公平锁
         */
        public MyReentrantLock(boolean fair) {
            super(fair);
        }

        /**
         * 条件队列/条件变量有参构造
         *
         * @param name 条件队列/条件变量名称
         * @return 条件队列/条件变量
         */
        public Condition newCondition(String name) {
            Condition condition = super.newCondition();
            conditionMap.put(name, condition);
            return condition;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(512);
            String queuedInfo = getQueuedInfo();
            List<String> all = new ArrayList<>();
            all.add(String.format("| owner[%s] state[%s]", this.getOwner(), this.getState()));
            all.add(String.format("| blocked queue %s", queuedInfo));
            for (Map.Entry<String, Condition> entry : this.conditionMap.entrySet()) {
                String waitingInfo = getWaitingInfo(entry.getValue());
                all.add(String.format("| waiting queue [%s] %s", entry.getKey(), waitingInfo));
            }
            int maxLength = all.stream().map(String::length).max(Comparator.naturalOrder()).orElse(100);
            for (String s : all) {
                sb.append(s);
                String space = IntStream.range(0, maxLength - s.length() + 7).mapToObj(i -> " ").collect(Collectors.joining(""));
                sb.append(space).append("|\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            String line1 = IntStream.range(0, maxLength).mapToObj(i -> "-").collect(Collectors.joining(""));
            sb.insert(0, String.format("%n| Lock %s|%n", line1));
            maxLength += 6;
            String line3 = IntStream.range(0, maxLength).mapToObj(i -> "-").collect(Collectors.joining(""));
            sb.append(String.format("%n|%s|", line3));
            return sb.toString();
        }

        private Object getState() {
            try {
                Field syncField = ReentrantLock.class.getDeclaredField("sync");
                Class<?> aqsClass = Class.forName("java.util.concurrent.locks.AbstractQueuedSynchronizer");
                Field stateField = aqsClass.getDeclaredField("state");
                syncField.setAccessible(true);
                AbstractQueuedSynchronizer sync = (AbstractQueuedSynchronizer) syncField.get(this);
                stateField.setAccessible(true);
                return stateField.get(sync);
            } catch (Exception e) {
                log.error("", e);
                throw new RuntimeException(e);
            }
        }

        private String getWaitingInfo(Condition condition) {
            List<String> result = new ArrayList<>();
            try {
                Field firstWaiterField = AbstractQueuedSynchronizer.ConditionObject.class.getDeclaredField("firstWaiter");
                Class<?> conditionNodeClass = Class.forName("java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionNode");
                Class<?> nodeClass = Class.forName("java.util.concurrent.locks.AbstractQueuedSynchronizer$Node");
                Field waiterField = nodeClass.getDeclaredField("waiter");
                Field statusField = nodeClass.getDeclaredField("status");
                Field nextWaiterField = conditionNodeClass.getDeclaredField("nextWaiter");
                firstWaiterField.setAccessible(true);
                waiterField.setAccessible(true);
                statusField.setAccessible(true);
                nextWaiterField.setAccessible(true);
                Object fistWaiter = firstWaiterField.get(condition);
                while (fistWaiter != null) {
                    Object waiter = waiterField.get(fistWaiter);
                    Object status = statusField.get(fistWaiter);
                    result.add(String.format("([%s]%s)", status, waiter));
                    fistWaiter = nextWaiterField.get(fistWaiter);
                }
            } catch (Exception e) {
                log.error("", e);
            }
            return String.join("->", result);
        }

        /**
         * 通过反射获取到 ReentrantLock 的阻塞队列
         * // TODO 这个获取阻塞队列有点问题
         *
         * @return 自定义展示的阻塞队列
         */
        private String getQueuedInfo() {
            List<String> result = new ArrayList<>();
            try {
                Field syncField = ReentrantLock.class.getDeclaredField("sync");
                Field headField = AbstractQueuedSynchronizer.class.getDeclaredField("head");
                Class<?> nodeClass = Class.forName("java.util.concurrent.locks.AbstractQueuedSynchronizer$Node");
                Field waiterField = nodeClass.getDeclaredField("waiter");
                Field statusField = nodeClass.getDeclaredField("status");
                Field nextField = nodeClass.getDeclaredField("next");
                syncField.setAccessible(true);
                AbstractQueuedSynchronizer sync = (AbstractQueuedSynchronizer) syncField.get(this);
                waiterField.setAccessible(true);
                statusField.setAccessible(true);
                nextField.setAccessible(true);
                headField.setAccessible(true);
                Object head = headField.get(sync);
                while (head != null) {
                    Object waiter = waiterField.get(head);
                    Object status = statusField.get(head);
                    result.add(String.format("({%s}%s)", status, waiter));
                    head = nextField.get(head);
                }
            } catch (Exception e) {
                log.error("", e);
            }
            return String.join("->", result);
        }
    }

    // 线程休眠1s
    private static void sleep1s() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    /**
     * 自定义方便测试的Thread, 重新 toString() 方法
     */
    static class MyThread extends Thread {
        /**
         * 有参构造
         *
         * @param target 可运行的任务
         * @param name   线程名称
         */
        public MyThread(Runnable target, String name) {
            super(target, name);
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }
}