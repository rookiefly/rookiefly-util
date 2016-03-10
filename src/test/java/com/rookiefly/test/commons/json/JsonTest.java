package com.rookiefly.test.commons.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.rookiefly.commons.json.JsonUtil;

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

}
