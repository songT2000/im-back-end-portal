package com.im.common.util.image;

import cn.hutool.core.codec.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类，本类补充{@link cn.hutool.extra.qrcode.QrCodeUtil}部分功能
 *
 * @author Barry
 * @date 2018/5/17
 */
public final class QrCodeUtil extends cn.hutool.extra.qrcode.QrCodeUtil {
    public static void main(String[] args) {
        System.out.println(encodeRechargeQrCode("http://www.baidu.com"));
    }

    /**
     * 二维码默认高度像素
     **/
    private static final int DEFAULT_QR_HEIGHT = 200;
    /**
     * 二维码默认宽度像素
     **/
    private static final int DEFAULT_QR_WIDTH = 200;

    private QrCodeUtil() {
    }

    /**
     * <p>将文本写到支付二维码中，并进行base64编码, 默认size: 200 * 200</p>
     *
     * <p>生成支付二维码、客服二维码、跳转二维码都用这个</p>
     *
     * @param text 要写到二维码中的字符串
     * @return 返回Base64编码的字符串, 可以直接显示在img的src上
     */
    public static String encodeRechargeQrCode(String text) {
        return encodeToBase64(text, DEFAULT_QR_HEIGHT, DEFAULT_QR_WIDTH);
    }

    /**
     * <p>将文本写到普通二维码中，并进行base64编码, 默认size: 200 * 200</p>
     *
     * <p>生成支付二维码、客服二维码、跳转二维码都用这个</p>
     *
     * @param text 要写到二维码中的字符串
     * @return 返回Base64编码的字符串, 可以直接显示在img的src上
     */
    public static String encodeToBase64(String text) {
        return encodeToBase64(text, DEFAULT_QR_HEIGHT, DEFAULT_QR_WIDTH);
    }

    /**
     * 将文本写到二维码中，并进行base64编码
     *
     * @param text   要写到二维码中的字符串
     * @param height 高度像素
     * @param width  宽度像素
     * @return 返回Base64编码的字符串, 可以直接显示在img的src上
     */
    public static String encodeToBase64(String text, int height, int width) {
        ByteArrayOutputStream outputStream = null;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>(2);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, height, width, hints);

            outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

            return "data:image/jpg;base64," + Base64.encode(outputStream.toByteArray()).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
