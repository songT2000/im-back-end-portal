package com.im.common.util;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.im.common.constant.CommonConstant;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet响应工具类
 *
 * @author Barry
 * @date 2018/7/21
 */
public final class ResponseUtil {
    private static final String BEAUTIFUL_HTML_START = "<html><head><title>.</title><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'></head><body style='margin:0 auto;'>";
    private static final String BEAUTIFUL_HTML_END = "</body></html>";
    private static final String BEAUTIFUL_TEXT_START = "<div style='text-align: center;height:100%; background-color: #473b3b;'><div style='padding: 50px 20px;'><span  style='font-size: 22px;color:#db2222;'>";
    private static final String BEAUTIFUL_TEXT_END = "</span></div></div>";

    private static final Log LOG = LogFactory.get();

    private ResponseUtil() {
    }

    /**
     * 往response中打印json字符串
     *
     * @param response ServletResponse
     * @param object   需要转成json的对象，任何对象
     */
    public static void printJson(ServletResponse response, Object object) {
        String json = JSON.toJSONString(object);
        printValue(response, json, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * 往response中打印html
     *
     * @param response ServletResponse
     * @param html     HTML内容
     */
    public static void printHtml(ServletResponse response, String html) {
        printValue(response, html, MediaType.TEXT_HTML_VALUE);
    }

    public static void printBeautifulText(ServletResponse response, String text) {
        String html = BEAUTIFUL_HTML_START + BEAUTIFUL_TEXT_START + text + BEAUTIFUL_TEXT_END + BEAUTIFUL_HTML_END;
        printValue(response, html, "text/html;charset=UTF-8");
    }

    public static void main(String[] args) {
        String html = BEAUTIFUL_HTML_START + BEAUTIFUL_TEXT_START + "请先登录" + BEAUTIFUL_TEXT_END + BEAUTIFUL_HTML_END;
        System.out.println(html);
    }

    public static void printText(ServletResponse response, String text) {
        printValue(response, text, "text/html;charset=UTF-8");
    }

    public static String appendHtmlContent(String content) {
        StringBuffer html = new StringBuffer();
        html.append(BEAUTIFUL_HTML_START);
        html.append(content);
        html.append(BEAUTIFUL_HTML_END);
        return html.toString();
    }

    public static void redirect(HttpServletResponse response, String url) {
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_FOUND);
        // try {
        //     response.setHeader("refresh", "0;url=" + url);
        //     response.flushBuffer();
        // } catch (IOException e) {
        //     LOG.error(e);
        // }
    }

    /**
     * 往response中打印普通值，响应类型
     *
     * @param response    ServletResponse
     * @param object      值
     * @param contentType 响应类型
     */
    public static void printValue(ServletResponse response, String object, String contentType) {
        PrintWriter out = null;
        try {
            response.setContentType(contentType);
            response.setCharacterEncoding(CommonConstant.CHARACTER_ENCODING_UTF8);
            out = response.getWriter();
            out.print(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 获取一个PrintWriter，响应类型text/html
     *
     * @param response HttpServletResponse
     * @return PrintWriter
     */
    public static PrintWriter getTextHtmlWriter(HttpServletResponse response) {
        return getWriter(response, "text/html;charset=UTF-8");
    }

    /**
     * 获取一个PrintWriter，响应类型application/json
     *
     * @param response HttpServletResponse
     * @return PrintWriter
     */
    public static PrintWriter getJsonWriter(HttpServletResponse response) {
        return getWriter(response, "application/json;charset=UTF-8");
    }

    /**
     * 获取一个PrintWriter，默认是
     *
     * @param response    HttpServletResponse
     * @param contentType 响应类型
     * @return PrintWriter
     */
    public static PrintWriter getWriter(HttpServletResponse response, String contentType) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType(contentType);
            return response.getWriter();
        } catch (IOException e) {
            LOG.error(e, "获取printWriter出错");
        }

        return null;
    }
}
