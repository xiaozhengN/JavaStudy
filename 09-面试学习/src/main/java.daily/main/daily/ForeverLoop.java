package main.daily;

import lombok.extern.slf4j.Slf4j;

/**
 * volatile 可见性例子
 *
 * -Xint
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-11-19 17:36:20
 **/
@Slf4j
public class ForeverLoop {

    // 共享变量, 控制 foo() 循环次数
    static volatile boolean isStop = false;

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            isStop = true;
            log.info("修改共享变量成功: isStop: {}", isStop);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            log.info("从主内存中获取共享变量, isStop: {}", isStop);
        }).start();
        foo();
    }

    static void foo() {
        int i = 0;
        while (!isStop) {
            i++;
        }
        log.info("foo方法已结束, 循环次数为: {}", i);
    }
}
