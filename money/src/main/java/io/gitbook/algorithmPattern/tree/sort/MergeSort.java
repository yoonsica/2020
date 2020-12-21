package io.gitbook.algorithmPattern.tree.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 归并排序
 * @author shengli
 */
public class MergeSort<T extends Comparable<T>> {

    /**
     * @param list
     */
    private List<T> mergeSort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        int mid = list.size() / 2;
        List<T> left = mergeSort(list.subList(0, mid));
        List<T> right = mergeSort(list.subList(mid,list.size()));
        return merge(left, right);
    }

    /**
     * 合并
     * @param left
     * @param right
     */
    private List<T> merge(List<T> left, List<T> right) {
        List<T> list = new ArrayList<>();
        int l = 0, r = 0;
        while (l < left.size() && r < right.size()) {
            if (left.get(l).compareTo(right.get(r)) < 0) {
                list.add(left.get(l++));
            } else {
                list.add(right.get(r++));
            }
        }
        while (l < left.size()) {
            list.add(left.get(l++));
        }
        while (r < right.size()) {
            list.add(right.get(r++));
        }
        return list;
    }

    public static void main(String[] args) {
        new MergeSort<Integer>().mergeSort(Arrays.asList(1, 5, 30, 100, 30, 50))
                .forEach(System.out::println);
    }
}
