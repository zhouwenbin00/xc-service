<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!
<br/>
遍历list的学生信息-----
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>金额</td>
        <td>生日</td>

    </tr>
    <#if stus??>
        <#list stus as stu>
            <tr>
                <td>${stu_index}</td>
                <td <#if stu.name == '小明'>style="color: blue"</#if>> ${stu.name}</td>
                <td>${stu.age}</td>
                <td <#if (stu.mondy > 300)>
                    style="color: blue"
                </#if> >${stu.mondy}</td>
                <td>${stu.birthday?string('YYYY年MM月dd日')}</td>

            </tr>
        </#list>
    </#if>
    学生的个数：${stus?size}
</table>
遍历stuMap的学生信息(map数据)-----
<br/>
第一种方法：在中括号中填写map的key--
<br/>
<#--姓名：${stuMap['stu1'].name}<br/>-->
<#--年龄：${stuMap['stu1'].age}<br/>-->
姓名：${(stuMap.stu1.name)!''}<br/>
年龄：${(stuMap.stu1.age)!''}<br/>
第一种方法：直接在map后面加"点key"--
<br/>
姓名：${stuMap.stu2.name}<br/>
年龄：${stuMap.stu2.age}<br/>
遍历map种的key----stuMap?keys就是key列表（是一个list）<br>
<#list stuMap?keys as k>
    ${k} <br>
    姓名:${stuMap[k].name}<br>
    年龄:${stuMap[k].age}<br>
</#list>
<br>
</body>
</html>