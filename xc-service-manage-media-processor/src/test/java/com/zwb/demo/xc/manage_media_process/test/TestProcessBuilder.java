package com.zwb.demo.xc.manage_media_process.test;

import com.zwb.demo.xc.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/** Created by zwb on 2019/10/25 20:54 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {

    /** 使用processBuilder调用第三方程序 */
    @Test
    public void test() throws IOException {
        // 创建processBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ipconfig");
        // 标准输入流和错误流合并
        processBuilder.redirectErrorStream();
        // 启动进程
        Process process = processBuilder.start();
        // 通过标准输入流获得正常和错误信息
        InputStream inputStream = process.getInputStream();
        // 转成字符流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
        // 缓冲
        char[] chars = new char[1024];
        int len = -1;
        while ((len = inputStreamReader.read(chars)) != -1) {
            String string = new String(chars, 0, len);
            System.out.println(string);
        }
        inputStreamReader.close();
        inputStream.close();
    }
}
