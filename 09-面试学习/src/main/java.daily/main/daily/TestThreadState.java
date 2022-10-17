package main.daily;

/**
 * 线程状态测试
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-18 07:33:11
 **/
public class TestThreadState {
    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        testNewRunnableTerminated();
        testWaiting();
        testBlocked();
    }

    private static void testBlocked() {
        Thread t2 = new Thread(() -> {
            System.out.println("before sync"); // 3
            synchronized (LOCK) {
                System.out.println("in sync"); // 4
            }
        }, "T2");
        t2.start();
        System.out.println(t2.getState()); // 1
        synchronized (LOCK) {
            System.out.println(t2.getState()); // 2
        }
        System.out.println(t2.getState()); // 5
    }

    private static void testWaiting() {
        Thread t3 = new Thread(() -> {
            synchronized (LOCK) {
                System.out.println("before waiting"); // 1
                try {
                    LOCK.wait(); // 3
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T3");
        t3.start();
        System.out.println(t3.getState()); // 2
        synchronized (LOCK) {
            System.out.println(t3.getState()); // 4
            LOCK.notify(); // 5
            System.out.println(t3.getState()); // 6
        }
        System.out.println(t3.getState()); // 7
    }

    private static void testNewRunnableTerminated() {
        Thread t1 = new Thread(() -> {
            System.out.println("running..."); // 3
        }, "T1");
        System.out.println(t1.getState()); // 1
        t1.start();
        System.out.println(t1.getState()); // 2
        System.out.println(t1.getState()); // 4
    }
}
