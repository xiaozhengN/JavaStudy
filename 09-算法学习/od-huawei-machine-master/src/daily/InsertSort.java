package daily;

import java.util.Arrays;

/**
 * 插入排序
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-04 16:16:55
 **/
public class InsertSort {
    public static void main(String[] args) {
        int[] arr = {9, 3, 7, 2, 5, 8, 1, 4};
        insertSortSolution(arr);
        System.out.println(Arrays.toString(arr));
    }

    // 插入排序
    private static void insertSortSolution(int[] arr) {
        // i 代表带插入元素的索引
        for (int i = 1; i < arr.length; i++) {
            // 代表待插入的元素值, 这里放到临时变量temp, 方便后面元素移动
            int temp = arr[i];
            // 代表已排序区域的元素索引
            int j = i - 1;
            while (j >= 0) {
                if (temp < arr[j]) {
                    // 元素的移动
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
                j--;
            }
            arr[j + 1] = temp;
        }
    }
}