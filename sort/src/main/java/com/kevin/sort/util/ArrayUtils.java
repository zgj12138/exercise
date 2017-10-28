package com.kevin.sort.util;

/**
 * 数组工具类
 * @author guoji_z
 * create on 2017/10/18 14:28
 */
public class ArrayUtils {
    /**
     * 输出数组
     * @param array
     */
    public static void printArray(int[] array) {
        System.out.print("{");
        for(int i = 0; i <array.length; i++) {
            System.out.print(array[i]);
            if(i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

    /**
     * 交换元素
     * @param array
     * @param i
     * @param j
     */
    public static void exchangeElements(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
