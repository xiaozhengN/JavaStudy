package daily;

import org.openjdk.jol.info.ClassLayout;
import org.springframework.util.StopWatch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ArrayList与LinkedList比较
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022年10月7日00:47:41
 **/
public class ArrayListVsLinkedList {
    public static void main(String[] args) {
        int n = 1000;
        int insertIndex = n / 2;
        int[] array = Utils.randomArray(n);
        List<Integer> list1 = Arrays.stream(array).boxed().collect(Collectors.toList());
        LinkedList<Integer> list2 = new LinkedList<>(list1);
        // 测试随机访问
        randomAccess(list1, list2, insertIndex);
        // 测试头部插入
        addFirst(list1,list2);
        // 测试中间插入
        addMiddle(list1, list2, insertIndex);
        // 测试尾部插入
        addLast(list1,list2);
        // 局部性原理, 空间占用测试
        arrayListSize((ArrayList<Integer>) list1);
        linkedListSize(list2);
    }

    private static void linkedListSize(LinkedList<Integer> list) {
        try {
            System.out.println("局部性原理, 空间占用测试 LinkedList: ");
            long size = 0;
            ClassLayout layout = ClassLayout.parseInstance(list);
            size += layout.instanceSize();

            Field firstField = LinkedList.class.getDeclaredField("first");
            firstField.setAccessible(true);
            Object first = firstField.get(list);
            long nodeSize = ClassLayout.parseInstance(first).instanceSize();

            size += (nodeSize * (list.size() + 2));

            long elementSize = ClassLayout.parseInstance(list.getFirst()).instanceSize();
            System.out.println("LinkedList size:[" + size + "],per Node size:[" + nodeSize + "],per Element size:[" + elementSize * list.size() + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void arrayListSize(ArrayList<Integer> list) {
        try {
            System.out.println("局部性原理, 空间占用测试 ArrayList: ");
            long size = 0;
            ClassLayout layout = ClassLayout.parseInstance(list);
            size += layout.instanceSize();

            Field elementDataField = ArrayList.class.getDeclaredField("elementData");
            elementDataField.setAccessible(true);
            Object elementData = elementDataField.get(list);

            size += ClassLayout.parseInstance(elementData).instanceSize();

            long elementSize = ClassLayout.parseInstance(list.get(0)).instanceSize();
            System.out.println("ArrayList size:[" + size + "],array length:[" + length(list) + "],per Element size:[" + elementSize * list.size() + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void randomAccess(List<Integer> list1, LinkedList<Integer> list2, int mid) {
        StopWatch sw = new StopWatch("测试随机访问");
        sw.start("测试随机访问: ArrayList");
        list1.get(mid);
        sw.stop();
        sw.start("测试随机访问: LinkedList");
        list2.get(mid);
        sw.stop();
        System.out.println(sw.prettyPrint());
    }

    private static void addMiddle(List<Integer> list1, LinkedList<Integer> list2, int mid) {
        StopWatch sw = new StopWatch("测试中间插入");
        sw.start("ArrayList: ");
        list1.add(mid, 100);
        sw.stop();
        sw.start("LinkedList: ");
        list2.add(mid, 100);
        sw.stop();
        System.out.println(sw.prettyPrint());
    }

    private static void addFirst(List<Integer> list1, LinkedList<Integer> list2) {
        StopWatch sw = new StopWatch("测试头部插入");
        sw.start("ArrayList: ");
        list1.add(0, 100);
        sw.stop();
        sw.start("LinkedList: ");
        list2.addFirst(100);
        sw.stop();
        System.out.println(sw.prettyPrint());
    }

    private static void addLast(List<Integer> list1, LinkedList<Integer> list2) {
        StopWatch sw = new StopWatch("测试尾部插入");
        sw.start("ArrayList: ");
        list1.add(100);
        sw.stop();
        sw.start("LinkedList: ");
        list2.add(100);
        sw.stop();
        System.out.println(sw.prettyPrint());
    }

    private static int length(ArrayList<Integer> list) {
        try {
            Field field = ArrayList.class.getDeclaredField("elementData");
            field.setAccessible(true);
            return ((Object[]) field.get(list)).length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
