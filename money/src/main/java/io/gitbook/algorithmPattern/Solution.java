package io.gitbook.algorithmPattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import yys.ShiShenVO;

import java.io.*;
import java.util.List;

public class Solution{
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/shengli/IdeaProjects/2020/money/src/main/resources/shishi.json");
        List<ShiShenVO> shiShenVOS = JSON.parseArray(IOUtils.toString(new FileInputStream(file), Charsets.UTF_8.toString()), ShiShenVO.class);

        File result  = new File("all.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(result));
        StringBuffer sb = new StringBuffer();
        for (ShiShenVO vo : shiShenVOS) {
            sb.append(vo.getId()).append(" ").append(vo.getName())
                    .append(" ").append(vo.getLevel())
                    .append(" ").append(vo.isInteractive()).append("\n");
            bw.write(sb.toString());
            sb = new StringBuffer();
        }
        bw.close();
    }

    void bSort(int a[], int n){
        int temp;
        int i,j;
        boolean flag;
        for(j = 0;j < n; j++){
            flag = false;
            for(i = 0; i < n - j - 1; i++){
                if(a[i] > a[i+1]){
                    flag = true;
                    temp = a[i];
                    a[i] = a[i+1];
                    a[i+1] = temp;
                }
            }
            if(!flag){
                break;
            }
        }
    }

    /**
     *j <= n -1 或 j < n
     *
     * i < n - 1 - j 或 j  < = n - j - 2
     *
     * a[i+1]
     *
     * a[i+1] = temp
     *
     * !flag 或 flag==false
     */
}