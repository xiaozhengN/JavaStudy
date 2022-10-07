package main.daily;

import java.util.Arrays;

/**
 * 选择排序
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-04 12:59:08
 **/
public class SelectionSort {

    public static void main(String[] args) {
        int[] arr = {5, 3, 7, 2, 1, 9, 8, 4};
        selection(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void selection(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            // i 代表每轮选择最小元素要交换到的目标索引
            // s 代表最小元素的索引
            int s = i;
            for (int j = s + 1; j < arr.length; j++) {
                if (arr[s] > arr[j]) {
                    s = j;
                }
            }
            if (s != i) {
                swap(arr, s, i);
            }
        }
    }

    // 交换
    private static void swap(int[] arr, int j, int i) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}
