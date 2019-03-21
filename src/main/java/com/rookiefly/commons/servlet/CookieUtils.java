package com.rookiefly.commons.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtils {

    private final static String defEncode = "UTF-8";

    private static final Log log = LogFactory.getLog(CookieUtils.class);

    /**
     * 获取cookie值，默认编码UTF-8
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        return getCookieValue(request, name, defEncode);
    }

    /**
     * 获取cookie值，指定编码
     *
     * @param request
     * @param name
     * @param encode
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name, String encode) {
        if (encode == null || "".equals(encode.trim())) {
            encode = defEncode; // 默认"UTF-8"
        }
        Cookie sCookie = getCookie(request, name);
        if (sCookie == null) {
            return null;
        }
        String val = sCookie.getValue();
        if (val == null)
            return null;
        try {
            return URLDecoder.decode(val, encode);
        } catch (UnsupportedEncodingException e) {
            log.error("decode cookie value failed, val=" + val, e);
            return val;
        }
    }

    /**
     * 获取cookie
     *
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null && null != name) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equals(cookies[i].getName())) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    /**
     * 删除Cookie(设置成空串，使立即清空)
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse response, String name, String path) {
        Cookie killMyCookie = new Cookie(name, "");
        killMyCookie.setMaxAge(0);
        killMyCookie.setPath(path);
        response.addCookie(killMyCookie);
    }

    public static void removeCookie(HttpServletResponse response, String name, String domain, String path) {
        Cookie killMyCookie = new Cookie(name, "");
        killMyCookie.setMaxAge(0);
        killMyCookie.setDomain(domain);
        killMyCookie.setPath(path);
        response.addCookie(killMyCookie);
    }

    /**
     * 删除所有Cookie
     */
    public static void removeAllCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || cookies.length <= 0)
            return;
        for (int i = 0; i < cookies.length; i++) {
            Cookie sCookie = cookies[i];
            sCookie.setValue(null);
            sCookie.setPath("/");
            sCookie.setMaxAge(0);
            response.addCookie(sCookie);
        }
    }

    /**
     * 删除所有Cookie
     */
    public static void removeAllCookieWithPath(HttpServletRequest request, HttpServletResponse response, String path) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || cookies.length <= 0)
            return;
        for (int i = 0; i < cookies.length; i++) {
            Cookie sCookie = cookies[i];
            sCookie.setValue(null);
            sCookie.setPath(path);
            sCookie.setMaxAge(0);
            response.addCookie(sCookie);
        }
    }

    /**
     * 设置Cookie
     */
    public static Cookie cookie(String name, String value) {
        Cookie _cookie;
        try {
            _cookie = new Cookie(name, URLEncoder.encode(value, defEncode));
        } catch (UnsupportedEncodingException e) {
            _cookie = new Cookie(name, value);
            log.error("decode cookie value failed, value=" + value, e);
        }
        return _cookie;
    }

    /**
     * 设置为负值，为浏览器进程Cookie(内存中保存)，关闭浏览器就失效 设置session时需要
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        Cookie _cookie = cookie(name, value);
        _cookie.setMaxAge(-1); // 设置为负值
        _cookie.setPath("/");
        response.addCookie(_cookie);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        Cookie _cookie = cookie(name, value);
        _cookie.setMaxAge(expiry);// how many seconds
        _cookie.setPath("/");
        response.addCookie(_cookie);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int expiry, String path) {
        Cookie _cookie = cookie(name, value);
        _cookie.setMaxAge(expiry);// how many seconds
        _cookie.setPath(path);
        response.addCookie(_cookie);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int expiry, String domain,
                                 String path) {
        Cookie _cookie = cookie(name, value);
        _cookie.setMaxAge(expiry);// how many seconds
        _cookie.setDomain(domain);
        _cookie.setPath(path);
        response.addCookie(_cookie);
    }

    public static void setCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    private void extendTime(HttpServletResponse response, Cookie cookie, int seconds, String path) {
        String domain = cookie.getDomain();
        String name = cookie.getName();
        String value = cookie.getValue();
        removeCookie(response, name, path);
        if (domain != null)
            setCookie(response, name, value, seconds, domain, path);
        else
            setCookie(response, name, value, seconds, path);
    }

    public void extendTime(HttpServletRequest request, HttpServletResponse response, String cookieName, int seconds,
                           String path) {
        extendTime(response, getCookie(request, cookieName), seconds, path);
    }
}
