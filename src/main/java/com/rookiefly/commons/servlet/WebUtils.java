package com.rookiefly.commons.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class WebUtils {

    public static final String JSON_RESPONSE_TYPE = "application/json;charset=UTF-8";

    public static final String HTML_RESPONSE_TYPE = "text/html;charset=UTF-8";

    public static void outJson(HttpServletResponse response, String content) throws IOException {
        response.setContentType(WebUtils.JSON_RESPONSE_TYPE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();
        pw.write(content);
        pw.flush();
        pw.close();

    }

    /**
     * 是否ajax请求
     * 
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (StringUtils.isBlank(requestType)) {
            return false;
        }
        if ("XMLHttpRequest".equals(requestType)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the Servlet 3 APIs are detected.
     * @return
     */
    private boolean isServlet3() {
        try {
            ServletRequest.class.getMethod("startAsync");
            return true;
        } catch (NoSuchMethodException e) {
        }
        return false;
    }
}
