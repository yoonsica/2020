package io.gitbook.algorithmPattern.tree.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 快速排序
 * @author shengli
 */
public class QuickSort<T extends Comparable<T>> {
    public List<T> sort(List<T> list) {
        return quickSort(list);
    }

    private List<T> quickSort(List<T> list) {
        if (list.size() <= 1) {
            return list;
        }
        List<T> result = new ArrayList<>();
        int end = list.size();
        int pivot = partition(list);
        if (pivot > 0) {
            List<T> left = quickSort(list.subList(0, pivot));
            result.addAll(left);
        }
        result.add(list.get(pivot));
        List<T> right = quickSort(list.subList(pivot + 1, end));
        result.addAll(right);
        return result;
    }

    private int partition(List<T> list) {
        T tmp = list.get(list.size() - 1);
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).compareTo(tmp) < 0) {
                swap(list, i, j);
                j++;
            }
        }
        swap(list,j,list.size()-1);
        return j;
    }

    public void swap(List<T> list, int i, int j) {
        T a = list.get(i);
        list.set(i, list.get(j));
        list.set(j, a);
    }

    public static void main(String[] args) {
        new QuickSort<Integer>().sort(Arrays.asList(1,5,3,2,8,6,6)).forEach(System.out::print);
    }
}
