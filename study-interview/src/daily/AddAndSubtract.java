package daily;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * volatile 原子性例子: 不能保证原子性
 * <p>
 * 调试需要拆解"balance -= 5;" 使用Debug模式复现
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-11-19 18:09:41
 **/
@Slf4j
public class AddAndSubtract {

    static volatile int balance = 10;

    private static void subtract() {
        balance -= 5;
    }

    private static void add() {
        balance += 5;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            subtract();
            latch.countDown();
        }).start();

        new Thread(() -> {
            add();
            latch.countDown();
        }).start();
        latch.await();
        log.info("共享变量balance: {}", balance);
    }
}
