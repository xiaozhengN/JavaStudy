//package daily;
//
//import jdk.internal.misc.Unsafe;
//import lombok.Builder;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 悲观锁和乐观锁区别
// * <p>
// * Setting -> Java Compiler -> Override compiler parameters pre-module
// * --add-exports=java.base/jdk.internal.access=ALL-UNNAMED
// * --add-exports=java.base/jdk.internal=ALL-UNNAMED
// * --add-exports=java.base/jdk.internal.misc=ALL-UNNAMED
// *
// * @author xiaozhengN 571457082@qq.com
// * @since 2022-11-20 16:55:07
// **/
//@Slf4j
//public class SyncVsCas {
//
//    static final Unsafe UNSAFE = Unsafe.getUnsafe();
//
//    static final long BALANCE = UNSAFE.objectFieldOffset(Account.class, "balance");
//
//    /**
//     * 账户对象
//     */
//    @Data
//    @Builder
//    static class Account {
//        // 账户余额
//        volatile int balance;
//    }
//
//    // 悲观锁例子
//    private static void sync(Account account) {
//        new Thread(() -> {
//            synchronized (account) {
//                int oldValue = account.balance;
//                int newValue = oldValue + 5;
//                account.balance = newValue;
//            }
//        }).start();
//        new Thread(() -> {
//            synchronized (account) {
//                int oldValue = account.balance;
//                int newValue = oldValue - 5;
//                account.balance = newValue;
//            }
//        }).start();
//        log.info("账户余额: " + account.balance);
//    }
//
//    // 乐观锁例子
//    private static void cas(Account account) {
//        new Thread(() -> {
//            while (true) {
//                int oldValue = account.balance;
//                int newValue = oldValue + 5;
//                // 比较交换
//                if (UNSAFE.compareAndSetInt(account, BALANCE, oldValue, newValue)) {
//                    break;
//                }
//            }
//        }).start();
//        new Thread(() -> {
//            while (true) {
//                int oldValue = account.balance;
//                int newValue = oldValue - 5;
//                // 比较交换
//                if (UNSAFE.compareAndSetInt(account, BALANCE, oldValue, newValue)) {
//                    break;
//                }
//            }
//        }).start();
//        log.info("账户余额: " + account.balance);
//    }
//
//    public static void main(String[] args) {
//        sync(Account.builder().balance(10).build());
//        cas(Account.builder().balance(10).build());
//    }
//}
