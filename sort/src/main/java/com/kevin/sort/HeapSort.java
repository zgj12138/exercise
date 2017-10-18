package com.kevin.sort;

import com.kevin.sort.util.ArrayUtils;

/**
 * 堆排序算法
 *
 * @author guoji_z
 * create on 2017/10/18 10:11
 */
public class HeapSort {
    public static void main(String[] args) {
        int[] array = {12, 38, 67, 6, 5, 34, 3, 27, 71, 0, -1, -42, -37};
        System.out.println("Before heap: ");
        ArrayUtils.printArray(array);
        heapSort(array);
        System.out.println("After heap sort:");
        ArrayUtils.printArray(array);
    }

    /**
     * 堆排序
     *
     * @param array
     */
    private static void heapSort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        buildMaxHeap(array);

        for (int i = array.length - 1; i >= 1; i--) {
            ArrayUtils.exchangeElements(array, 0, i);

            maxHeap(array, i, 0);
        }
    }

    /**
     * 构建最大堆
     *
     * @param array
     */
    private static void buildMaxHeap(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int half = array.length / 2;
        for (int i = half; i >= 0; i--) {
            maxHeap(array, array.length, i);
        }
    }

    private static void maxHeap(int[] array, int heapSize, int index) {
        int left = index * 2 + 1;
        int right = index * 2 + 2;

        int largest = index;
        if (left < heapSize && array[left] > array[index]) {
            largest = left;
        }

        if (right < heapSize && array[right] > array[largest]) {
            largest = right;
        }

        if (index != largest) {
            ArrayUtils.exchangeElements(array, index, largest);

            maxHeap(array, heapSize, largest);
        }
    }
}
