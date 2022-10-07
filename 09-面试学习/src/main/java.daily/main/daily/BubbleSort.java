package main.daily;

import java.util.Arrays;

/**
 * 冒泡排序
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-04 11:59:36
 **/
public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = {100, 1, 3, 4, 6, 2, 1, 3};
        // bubbleSort(arr);
        // bubbleSortV1(arr);
        bubbleSortV2(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 减少比较次数
     *
     * @param arr 待排序数组
     */
    private static void bubbleSortV2(int[] arr) {
        // 比较次数
        int compareCount = arr.length - 1;
        do {
            // 表示最后一次交换索引位置
            int last = 0;
            for (int j = 0; j < compareCount; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    last = j;
                }
            }
            compareCount = last;
        } while (compareCount != 0);
    }

    /**
     * 减少冒泡(交换)次数
     *
     * @param arr 待排序数组
     */
    private static void bubbleSortV1(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            // 是否冒泡标识, 默认false
            boolean isSwaped = false;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    isSwaped = true;
                }
            }
            if (!isSwaped) {
                break;
            }
        }
    }

    // 冒泡排序
    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
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
