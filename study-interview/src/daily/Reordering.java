package daily;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

/**
 * volatile 有序性例子
 * <p>
 * 没有测试成功, 报错: Unrecognized VM option 'StressCCP'
 * java -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation -jar jcstress.jar -t main.daily.Reordering.Case1
 * D:\17-JDKS\jdk-16.0.1\bin\java -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation -jar jcstress.jar -t main.daily.Reordering.Case1
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-11-19 18:27:03
 **/
public class Reordering {

    @JCStressTest
    @Outcome(id = {"0, 0", "1, 1", "0, 1"}, expect = Expect.ACCEPTABLE, desc = "可接受的值")
    @Outcome(id = "1, 0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "感兴趣的值(发生指令重排)")
    @State
    public static class Case1 {
        int x;
        int y;

        @Actor
        public void actor1() {
            x = 1;
            y = 1;
        }

        @Actor
        public void actor2(II_Result result) {
            result.r1 = y;
            result.r2 = x;
        }
    }

    @JCStressTest
    @Outcome(id = {"0, 0", "1, 1", "0, 1"}, expect = Expect.ACCEPTABLE, desc = "可接受的值")
    @Outcome(id = "1, 0", expect = Expect.FORBIDDEN, desc = "被禁止的值")
    @State
    public static class Case2 {
        int x;
        volatile int y;

        @Actor
        public void actor1() {
            x = 1;
            y = 1;
        }

        @Actor
        public void actor2(II_Result result) {
            result.r1 = y;
            result.r2 = x;
        }
    }

    @JCStressTest
    @Outcome(id = {"0, 0", "1, 1", "0, 1"}, expect = Expect.ACCEPTABLE, desc = "可接受的值")
    @Outcome(id = "1, 0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "感兴趣的值(发生指令重排)")
    @State
    public static class Case3 {
        volatile int x;
        int y;

        @Actor
        public void actor1() {
            x = 1;
            y = 1;
        }

        @Actor
        public void actor2(II_Result result) {
            result.r1 = y;
            result.r2 = x;
        }
    }
}
