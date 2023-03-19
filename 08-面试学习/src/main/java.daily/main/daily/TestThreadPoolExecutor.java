package main.daily;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池学习
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-19 22:39:13
 **/
@Slf4j
public class TestThreadPoolExecutor {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                3,
                0,
                TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(2),
                r -> new Thread(r, "myThread" + atomicInteger.getAndIncrement()),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        showState(threadPool);
        threadPool.submit(new MyTask("1", 3600000));
        showState(threadPool);
        threadPool.submit(new MyTask("2", 3600000));
        showState(threadPool);
        threadPool.submit(new MyTask("3"));
        showState(threadPool);
        threadPool.submit(new MyTask("4"));
        showState(threadPool);
        threadPool.submit(new MyTask("5", 3600000));
        showState(threadPool);
        threadPool.submit(new MyTask("6"));
        showState(threadPool);
    }

    // 获取线程池核心线程数和阻塞队列信息
    private static void showState(ThreadPoolExecutor threadPool) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Object> blockingQueueList = new ArrayList<>();
        for (Runnable runnable : threadPool.getQueue()) {
            try {
                Field callable = FutureTask.class.getDeclaredField("callable");
                callable.setAccessible(true);
                Object adapter = callable.get(runnable);
                Class<?> clazz = Class.forName("java.util.concurrent.Executors$RunnableAdapter");
                Field task = clazz.getDeclaredField("task");
                task.setAccessible(true);
                Object o = task.get(adapter);
                blockingQueueList.add(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         log.debug("pool size: {}, queue: {}", threadPool.getPoolSize(), blockingQueueList);
    }

    static class MyTask implements Runnable {
        private final String name;
        private final long duration;

        public MyTask(String name) {
            this(name, 0);
        }

        public MyTask(String name, long duration) {
            this.name = name;
            this.duration = duration;
        }

        @Override
        public void run() {
            try {
                log.debug("running...name: {}, obj: {}", Thread.currentThread().getName(), this);
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "MyTask(" + name + ")";
        }
    }
}
