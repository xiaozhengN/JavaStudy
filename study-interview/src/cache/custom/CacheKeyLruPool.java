package cache.custom;

import cache.redis.CacheClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MethodInvoker;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-14 22:01:46
 */
@Component
@Slf4j
public class CacheKeyLruPool {
    @Autowired
    private CacheClient cacheClient;

    /**
     * 执行时间超过1500ms, 放入LRU缓存池
     */
    public static int standardCostTime = 1500;

    /**
     * 缓存池大小, 例如: 每个查询耗时3s, 一个周期查询耗时 3 * 100 = 5min
     */
    public static int cacheSize = 50;

    /**
     * 刷新周期: 8min
     */
    public static final int RATE = 8;

    /**
     * 当前缓存池里内容个数
     */
    final AtomicInteger size = new AtomicInteger(0);

    /**
     * key到节点的映射Map, 方便通过key定位到节点
     */
    final Map<String, Node> keyToNode = new ConcurrentHashMap<>();

    /**
     * 头节点
     */
    final Node HEAD = new Node();

    /**
     * 尾节点
     */
    final Node TAIL = new Node();

    {
        HEAD.next = TAIL;
        TAIL.pre = HEAD;
    }

    /**
     * 记录进了LRU池后执行时间低于阈值的key, 防止不耗费时间的动作, 一致在池子中存活
     */
    private Set<String> lessThanStandard = new CopyOnWriteArraySet<>();

    /**
     * LRU链表锁
     */
    private final Lock lurLinkedListLock = new ReentrantLock();

    /**
     * 链表节点实体
     */
    @AllArgsConstructor
    @NoArgsConstructor
    class Node {
        /**
         * 缓存的key
         */
        String key;

        /**
         * 获取值所调用的方法
         */
        MethodInvoker methodInvoker;

        /**
         * 前一个节点
         */
        Node pre;

        /**
         * 后一个节点
         */
        Node next;

        void delelte() {
            lurLinkedListLock.lock();
            try {
                pre.next = next;
                next.pre = pre;
                size.decrementAndGet();
                keyToNode.remove(key);
            } finally {
                lurLinkedListLock.unlock();
            }
        }

        void addToHead() {
            lurLinkedListLock.lock();
            try {
                HEAD.next.pre = this;
                this.next = HEAD.next;
                HEAD.next = this;
                this.pre = HEAD;
                size.incrementAndGet();
                while (size.get() > cacheSize) {
                    TAIL.pre.delelte();
                }
                keyToNode.put(key, this);
            } finally {
                lurLinkedListLock.unlock();
            }
        }

        @PostConstruct
        public void init() {
            // 使用定时任务刷新缓存内容, 固定周期刷新, 避免数据库查询时间太久, 然后还得等待, 导致Redis的缓存过期了
            new ScheduledThreadPoolExecutor(1)
                    .scheduleAtFixedRate(this::refresh, 0, RATE, TimeUnit.MINUTES);
        }

        /**
         * 定时刷新
         */
        private void refresh() {
            Set<String> newLessThanStandard = new CopyOnWriteArraySet<>();
            for (Node node : keyToNode.values()) {
                try {
                    long startTime = System.currentTimeMillis();
                    Object result = node.methodInvoker.invoke();
                    long costTime = System.currentTimeMillis() - startTime;
                    // 不耗时的动作, 从链表中提出
                    if (costTime < standardCostTime) {
                        if (lessThanStandard.contains(node.key)) {
                            node.delelte();
                        } else {
                            newLessThanStandard.add(node.key);
                        }
                    }
                    cacheClient.set(node.key, result, RATE * 2);
                    log.info("refresh cache successed, key is: " + node.key);
                } catch (Throwable e) {
                    log.error("refresh cache error ", e);
                }
            }
            lessThanStandard = newLessThanStandard;
        }
    }
}
