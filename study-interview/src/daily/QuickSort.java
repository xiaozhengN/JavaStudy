package daily;

import java.util.Arrays;

/**
 * 快速排序
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-05 11:51:01
 **/
public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {5, 3, 7, 2, 9, 8, 1, 4};
        quick(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    private static void quick(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        // 基准点元素索引
        // int p = partition1(arr, low, high);
        int p = partition2(arr, low, high);
        quick(arr, low, p - 1);
        quick(arr, p + 1, high);
    }

    /**
     * 单边循环: 分区
     * <p>
     * 左边元素都比基准点小, 右边都比基准点元素大
     *
     * @param arr  待排序数组
     * @param low  分区的下边界
     * @param high 分区的上边界
     * @return 基准点元素最后的索引
     */
    private static int partition1(int[] arr, int low, int high) {
        // 选择上边界作为基准点元素
        int pv = arr[high];
        int i = low;
        for (int j = low; j < high; j++) {
            if (arr[j] < pv) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, high, i);
        return i;
    }

    /**
     * 双边循环: 分区
     *
     * @param arr  待排序数组
     * @param low  分区的下边界
     * @param high 分区的上边界
     * @return 基准点元素最后的索引
     */
    private static int partition2(int[] arr, int low, int high) {
        // 选取左边元素作为基准点
        int pv = arr[low];
        int i = low;
        int j = high;
        while (i < j) {
            // j 从右找小的元素
            while (i < j && arr[j] > pv) {
                j--;
            }
            // i 从左找大的元素
            while (i < j && arr[i] <= pv) {
                i++;
            }
            swap(arr, i, j);
        }
        swap(arr, low, j);
        return j;
    }

    // 交换
    private static void swap(int[] arr, int j, int i) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}
