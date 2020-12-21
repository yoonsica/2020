package io.gitbook.algorithmPattern;

public class KMP {
    /**
     * 计算最长公共前后缀
     * @param pattern
     * @return
     */
    private int[] prefix_table(char[] pattern) {
        int[] prefix = new int[pattern.length];
        int i = 1;
        int len = 0;
        prefix[0] = 0;
        while (i < pattern.length) {
            if (pattern[i] == pattern[len]) {
                len++;
                prefix[i] = len;
                i++;
            } else {
                if (len > 0) {
                    len = prefix[len - 1];
                } else {
                    prefix[i] = len;
                    i++;
                }
            }
        }
        return prefix;
    }

    private void getNext(int[] prefix) {
        for (int i = prefix.length - 1; i > 0; i--) {
            prefix[i] = prefix[i - 1];
        }
        prefix[0] = -1;
    }

    void kmp_search(char[] txt, char[] pattern) {
        int[] prefix = prefix_table(pattern);
        getNext(prefix);
        int i = 0, j = 0;
        while (i < txt.length) {
            if (j == pattern.length - 1 && pattern[j] == txt[i]) {
                System.out.println("Found pattern at:" + (i - j));
                j = prefix[j];
            }
            if (txt[i] == pattern[j]) {
                i++;
                j++;
            } else {
                j = prefix[j];
                if (j == -1) {
                    i++;
                    j++;
                }
            }
        }
    }

    public static void main(String[] args) {
        new KMP().kmp_search("ABAB".toCharArray(),"BA".toCharArray());
    }
}
