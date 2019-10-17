package com.zwb.demo.xc.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Create by zwb on 2019-10-06 18:11 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {

    @Autowired GridFsTemplate gridFsTemplate;
    @Autowired GridFSBucket gridFSBucket;

    // 存文件
    @Test
    public void testStore() throws FileNotFoundException {
        // 定义file
        File file =
                new File(
                        "E:\\IdeaProjects\\other\\xc-servce\\test-freemarker\\src\\main\\resources\\templates\\course.ftl");
        // 定义文件输入流
        FileInputStream fileInputStream = new FileInputStream(file);

        ObjectId objectId = gridFsTemplate.store(fileInputStream, "course.ftl");
        System.out.println(objectId);
    }

    // 查文件
    @Test
    public void test() throws IOException {
        // 查询文件
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(
                        Query.query(Criteria.where("_id").is("5d99bf2ab845233b364102a2")));
        // 打开下载流
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 创建GridFsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        // 从流中获取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(content);
    }
}
