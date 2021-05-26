package com.rookiefly.commons.netty.chat;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final Map<String, String> USER_MAP = new HashMap<>();

    public static String getUser(String channelId) {
        return USER_MAP.get(channelId);
    }

    public static void setUser(String channelId) {
        USER_MAP.put(channelId, RandomStringUtils.randomAlphabetic(6));
    }
}
