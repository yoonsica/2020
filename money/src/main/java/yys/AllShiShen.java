package yys;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Classname AllShiShen
 * @Description TODO
 * @Date 2021/4/7 下午2:31
 * @Author shengli
 */
public class AllShiShen {

    public static void writeToFile(String fileName,String jsonStr) throws IOException {
        List<ShiShenVO> shiShenVOS = JSON.parseArray(jsonStr, ShiShenVO.class);
        File result  = new File(fileName);
        if (result.exists()) {
            result.delete();
        }
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

    public static void main(String[] args) throws Exception {
        String url = "https://yys.res.netease.com/pc/gw/20180913151832/js/app/shishen.json";
        String jsonStr = new HttpUtil().doPost(url, "v=39");
        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fileName = "yys-" + dtf.format(now);
        writeToFile(fileName,jsonStr);
    }
}
