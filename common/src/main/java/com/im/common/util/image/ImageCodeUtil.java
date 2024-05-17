package com.im.common.util.image;

import cn.hutool.core.codec.Base64;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 验证码生成工具
 *
 * @author Daniel
 * @date 2019/10/24
 */
public class ImageCodeUtil {
    private static Random r = new Random();

    private static int width = 250;
    private static int height = 100;
    private static int line = 80;
    private static int length = 4;
    private static int fontSize = 84;

    public static String[] generate(HttpServletResponse response) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setFont(font());
        g.setColor(color(110, 133));
        for (int i = 0; i <= line; i++) {
            drawLine(g);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(drawString(g, i * 60 + 10, 80));
        }
        g.dispose();

        String base64Str = write(image, response);
        return new String[]{sb.toString(), base64Str};
    }

    private static String write(BufferedImage image, HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        ByteArrayOutputStream bs = null;
        try {
            bs = new ByteArrayOutputStream();
            // 将绘制得图片输出到流
            ImageIO.write(image, "png", bs);
            return Base64.encode(bs.toByteArray()).trim();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bs != null) {
                try {
                    bs.flush();
                    bs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static String drawString(Graphics g, int x, int y) {
        Random r = new Random();
        g.setFont(font());
        int red = r.nextInt(101);
        int green = r.nextInt(111);
        int blue = r.nextInt(121);
        g.setColor(new Color(red, green, blue));
        String s = string();
        g.drawString(s, x, y);
        return s;
    }

    private static void drawLine(Graphics g) {
        int x = r.nextInt(width);
        int y = r.nextInt(height);
        int xl = r.nextInt(13);
        int yl = r.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    private static char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static Font font() {
        return new Font("宋体", Font.BOLD, 84);
    }

    private static Color color(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int red = fc + r.nextInt(bc - fc - 16);
        int green = fc + r.nextInt(bc - fc - 14);
        int blue = fc + r.nextInt(bc - fc - 18);
        return new Color(red, green, blue);
    }

    private static String string() {
        int index = r.nextInt(ch.length);
        return String.valueOf(ch[index]);
    }
}