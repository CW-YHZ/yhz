package com.cw.yhz.Lambda;

import com.google.common.collect.Lists;
import com.ouhl.utildemo.WebService.pojo.User;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * lambda 表达式用法
 * 注释：lambda是在java8的时候推出的，常用于对集合进行数据处理
 *      lambda 取代了又长又臭的 for 循环
 */
public class LambdaDemo {

    /* 相关资料 url：https://www.cnblogs.com/qdhxhz/p/9399015.html */
    @Test
    public void test1() {
        List<User> persionList = Lists.newArrayList();
        persionList.add(new User("name1", 16, "男"));
        persionList.add(new User("name2", 14, "女"));
        persionList.add(new User("name3", 13, "男"));
        persionList.add(new User("name4", 12, "男"));
        persionList.add(new User("name5", 18, "男"));
        persionList.add(new User("name6", 22, "男"));
        persionList.add(new User("name7", 26, "女"));
        persionList.add(new User("name8", 29, "男"));
        persionList.add(new User("name9", 33, "男"));
        persionList.add(new User("name10", 11, "女"));

        //1、只取出该集合中所有姓名组成一个新集合（String类型）
        List<String> nameList = persionList.stream().map(User::getName).collect(Collectors.toList());
        System.out.println(nameList.toString());

        //2、只取出该集合中所有年龄组成一个新集合（INT类型）
        List<Integer> idList = persionList.stream().mapToInt(User::getAge).boxed().collect(Collectors.toList());
        System.out.println(idList.toString());

        //3、list转map，key值为name，value为User对象
        Map<String, User> personmap = persionList.stream().collect(Collectors.toMap(User::getName, person -> person));
        System.out.println(personmap.toString());

        //4、list转map，key值为Age，value为name
        Map<Integer, String> namemap = persionList.stream().collect(Collectors.toMap(User::getAge, User::getName));
        System.out.println(namemap.toString());

        //5、进行map集合存放，key为age值 value为User对象 它会把相同age的对象放到一个集合中
        Map<Integer, List<User>> ageMap = persionList.stream().collect(Collectors.groupingBy(User::getAge));
        System.out.println(ageMap.toString());

        //6、获取最小年龄
        Integer ageMin = persionList.stream().mapToInt(User::getAge).min().getAsInt();
        System.out.println("最小年龄为: " + ageMin);

        //7、获取最大年龄
        Integer ageMax = persionList.stream().mapToInt(User::getAge).max().getAsInt();
        System.out.println("最大年龄为: " + ageMax);

        //8、集合年龄属性求和
        Integer ageAmount = persionList.stream().mapToInt(User::getAge).sum();
        System.out.println("年龄总和为: " + ageAmount);

        //9、获取最大年龄
        Integer ageMaxs = persionList.stream().mapToInt(User::getAge).max().getAsInt();
        System.out.println("最大年龄为: " + ageMaxs);

        //10、使用Lambda表达式 倒序
        persionList.sort((a, b) ->  b.getAge() - a.getAge());
        System.out.println("\n"+persionList);

        //11、使用Lambda表达式 正序
        persionList.sort((a, b) -> a.getAge() - b.getAge());
        System.out.println("\n"+persionList);

    }
}
