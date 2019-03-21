package com.rookiefly.test.commons.json;

import com.alibaba.fastjson.JSON;
import com.rookiefly.commons.json.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by rookiefly on 2015/12/1.
 */
public class JsonTest {

    @Test
    public void test01() {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("json/bank.json");
            String json = IOUtils.toString(inputStream);
            Map map = JsonUtil.toObject(json, Map.class);
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02() {
        String json = "{\"5\":\"http://haitao.nos.netease.com/fc54243ec3fc492a9809ba189ab58333.png\",\"7\":\"http://haitao.nos.netease.com/ff535501e7874eaa8921d9841fe4d20b.png\",\"52\":\"http://haitao.nos.netease.com/cc18f7b167ed40038d6c164a24504924.png\"}";
        Map<String, String> payMethodIconMap = JSON.parseObject(json, Map.class);
        System.out.println(JSON.toJSONString(payMethodIconMap));
        System.out.println(payMethodIconMap.get(5));
    }

    @Test
    public void test03() {
        String ids = "18;";
        String[] strings = ids.split(";");
        System.out.println(strings);
        String[] strings1 = StringUtils.split(ids, ";");
        System.out.println(strings1);
        String s = ",a,b,c,,";
        String[] strings2 = s.split(",");
        System.out.println(strings2);
        String[] strings3 = StringUtils.split(s, ",");
        System.out.println(strings3);
    }
}
