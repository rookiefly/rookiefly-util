package com.rookiefly.test.commons.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.*;
import com.rookiefly.commons.json.SerializeFilterBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rookiefly on 2015/12/1.
 */
public class FastJsonTest {

    @Test
    public void test01() {

        Address address = new Address("hangzhou", "binjiang", 0);

        Account account = new Account("1001", "rookiefly", 25, address);

        Bank bank = new Bank("2010", "http://www.rookiefly.com", account);

        String json = JSON.toJSONString(bank);

        System.out.println(json);
        SerializerFeature[] serializerFeatures = new SerializerFeature[] {
                SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.NotWriteRootClassName };

        SerializeFilter filter = SerializeFilterBuilder.newExcludeFilter(Account.class, "address");

        System.out.println(JSON.toJSONString(bank, filter));

        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        map.put("account", account);
        JSON.toJSONString(map, filter, serializerFeatures);
    }

    @Test
    public void test02() {
        Address address = new Address("hangzhou", "binjiang", 0);
        System.out.println(JSON.toJSONString(address));
        String json = "{\"area\":\"binjiang\",\"city\":\"hangzhou\"}";
        Address address1 = JSON.parseObject(json, Address.class);
        System.out.println(address1);
    }
}
