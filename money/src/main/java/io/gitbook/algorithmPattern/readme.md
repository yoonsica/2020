# https://greyireland.gitbook.io/algorithm-pattern/ru-men-pian
## 算法快速入门练习题
1. 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从 0 开始)。如果不存在，则返回 -1。
```java
public class QuickStart {
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
}
```
2. 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）