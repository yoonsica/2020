package io.gitbook.algorithmPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shengli
 */
public class QuickStart {

    /**
     * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从 0 开始)。
     * 如果不存在，则返回 -1。
     */
    public static int strStr(String haystack, String needle) {
        for (int i = 0; i < haystack.length() - needle.length() + 1; i++) {
            int j = 0;
            for (; j < needle.length(); j++) {
                if (haystack.charAt(i + j) != needle.charAt(j)) { //关键点
                    break;
                }
            }
            if (j == needle.length()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）
     * 输入: nums = [1,2,3]
     * 输出:
     * [
     *   [3],
     *   [1],
     *   [2],
     *   [1,2,3],
     *   [1,3],
     *   [2,3],
     *   [1,2],
     *   []
     * ]
     * @param nums
     * @return
     */



    List<List<Integer>> output = new ArrayList();
    int n;

    /**
     * result = []
     * def backtrack(路径, 选择列表):
     *     if 满足结束条件:
     *         result.add(路径)
     *         return
     *     for 选择 in 选择列表:
     *         做选择
     *         backtrack(路径, 选择列表)
     *         撤销选择
     * @param nums
     * @return
     */
    public void backtrack(int first, ArrayList<Integer> curr, int[] nums) {

        output.add(new ArrayList(curr));

        for (int i = first; i < n; i++) {
            // add i into the current combination
            curr.add(nums[i]);
            // use next integers to complete the combination
            backtrack(i + 1, curr, nums);
            // backtrack
            curr.remove(curr.size() - 1);
        }
    }


    public List<List<Integer>> subsets(int[] nums) {
        n = nums.length;
        backtrack(0, new ArrayList<Integer>(), nums);
        return output;
    }

    public static void main(String[] args) {
        new QuickStart().subsets(new int[]{1,2,3});
    }
}
