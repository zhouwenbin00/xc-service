package com.zwb.demo.xc.manage_media.test;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

/** Created by zwb on 2019/10/24 21:11 */
public class FileTest {

    /** 测试文件分块 */
    @Test
    public void test() throws IOException {
        // 源文件目录
        File sourceFile = new File("E:\\video\\lucene.avi");
        // 快文件目录
        String chunksFileFolder = "E:\\video\\chunks\\";
        // 定义块文件大小
        long chunksFileSize = 1 * 1024 * 1024;
        // 块数
        long chunksFileNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunksFileSize);
        // 创建读文件对象
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        // 缓冲区
        byte[] b = new byte[1024];
        for (int i = 0; i < chunksFileNum; i++) {
            // 块文件
            File chunksFile = new File(chunksFileFolder + i);
            // 创建块文件写对象
            RandomAccessFile raf_write = new RandomAccessFile(chunksFile, "rw");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
                // 如果块文件大小达到size，开始写下一块
                if (chunksFile.length() >= chunksFileSize) {
                    break;
                }
            }
            raf_write.close();
        }
        raf_read.close();
    }

    /** 合并 */
    @Test
    public void test2() throws IOException {
        // 快文件目录
        String chunksFileFolderPath = "E:\\video\\chunks\\";
        // 快文件目录对象
        File chunksFileFolder = new File(chunksFileFolderPath);
        // 块文件列表
        File[] files = chunksFileFolder.listFiles();

        List<File> fileList = Arrays.asList(files);
        // 排序
        fileList.sort(
                (o1, o2) -> {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    }

                    return -1;
                });

        // 合并文件
        File mergeFile = new File("E:\\lucene.avi");
        boolean newFile = mergeFile.createNewFile();
        // 创建写对象
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        // 缓冲区
        byte[] b = new byte[1024];
        for (File chunksFile : fileList) {
            // 创建读文件对象
            RandomAccessFile raf_read = new RandomAccessFile(chunksFile, "r");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}
