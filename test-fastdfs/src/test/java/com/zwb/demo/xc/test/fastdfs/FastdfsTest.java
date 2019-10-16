package com.zwb.demo.xc.test.fastdfs;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** Created by zwb on 2019/10/15 21:08 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FastdfsTest {

    @Test
    public void testUpload() throws IOException, MyException {
        // 加载配置文件
        ClientGlobal.initByProperties("config/fastdfs-client.properties.properties");
        // 定义TrackerClient，用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        // 连接TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取StorageServer
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
        // 定义StorageClient
        StorageClient1 storageClient = new StorageClient1(trackerServer, storeStorage);
        // 向store上传文件
        String filePath = "D:/Chrysanthemum.jpg";
        String fileId = storageClient.upload_file1(filePath, "jpg", null);
        System.out.println(fileId);
    }

    @Test
    public void testDownload() throws IOException, MyException {
        // 加载配置文件
        ClientGlobal.initByProperties("config/fastdfs-client.properties.properties");
        // 定义TrackerClient，用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        // 连接TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取StorageServer
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
        // 定义StorageClient
        StorageClient1 storageClient = new StorageClient1(trackerServer, storeStorage);
        // 下载文件
        byte[] bytes =
                storageClient.download_file1("group1/M00/00/00/rBgUw12mi1uAKv-hAA1rIuRd3Es610.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/a.jpg"));
        fileOutputStream.write(bytes);
    }
}
