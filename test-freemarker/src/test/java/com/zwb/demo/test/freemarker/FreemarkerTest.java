package com.zwb.demo.test.freemarker;

import com.zwb.demo.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/** Create by zwb on 2019-10-06 16:42 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    @Test
    public void testGenerateHtmlByFtl() throws IOException, TemplateException {
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 定义模板
        String classPath = this.getClass().getResource("/").getPath();
        File file = new File(classPath + "/templates/");
        configuration.setDirectoryForTemplateLoading(file);
        Template template = configuration.getTemplate("test1.ftl");
        // 定义数据
        Map map = getMap();
        // 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //        System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("/Volumes/D/test1.html"));
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testGenerateHtmlByString() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 定义模板
        // 模板内容（字符串）

        String templateString =
                ""
                        + "<html>\n"
                        + " <head></head>\n"
                        + " <body>\n"
                        + " 名称：${name}\n"
                        + " </body>\n"
                        + "</html>";
        // 使用一个模板加载器变为模板
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateString);
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template = configuration.getTemplate("template", "utf-8");
        Map map = getMap();
        // 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //        System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("/Volumes/D/test1.html"));
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    // 获取数据模型
    public Map getMap() {
        Map map = new HashMap();
        map.put("name", "传智播客");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMondy(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMondy(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        // 向数据模型放数据
        map.put("stus", stus);
        // 准备map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        //        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        // 向数据模型放数据
        map.put("stu1", stu1);
        // 向数据模型放数据
        map.put("stuMap", stuMap);
        return map;
    }
}
