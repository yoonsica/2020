package io.gitbook.algorithmPattern;

import java.util.*;

/**
 *
 */
public class Solution {
    public String reverseStr(String s, int k) {
        char[] array = s.toCharArray();
        int start = 0;
        while (start + k - 1 < array.length && start + 2 * k < array.length) {
            reverse(array, start, start + k - 1);
            start += 2*k;
        }
        if (start + k > array.length - 1) {
            reverse(array, start, array.length - 1);
        } else if (start + k < array.length) {
            reverse(array,start,start + k - 1);
        }
        return new String(array);
    }

    public void reverse(char[] array, int start, int end) {
        while (start < end) {
            char tmp = array[start];
            array[start++] = array[end];
            array[end--] =  tmp;
        }
    }
    public static void main(String[] args) {
        System.out.println(new Solution().reverseStr("abcdefg",2));
    }

}
