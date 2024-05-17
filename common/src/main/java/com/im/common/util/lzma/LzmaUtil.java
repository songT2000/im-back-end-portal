package com.im.common.util.lzma;

import cn.hutool.core.util.ZipUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.lzma.SevenZip.Compression.LZMA.Decoder;
import com.im.common.util.lzma.SevenZip.Compression.LZMA.Encoder;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

/**
 * Lamz工具类
 *
 * @author Barry
 * @date 2017/6/28
 */
public class LzmaUtil {
    /**
     * 把一串字符串压缩，必须串必须一定量级才来调用
     *
     * @param str 任意字符串
     * @return 返回压缩后的字符串
     * @throws IOException
     */
    public static String compress(final String str) throws IOException {
        if (StrUtil.isBlank(str)) {
            return str;
        }

        final Encoder encoder = new Encoder();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        try {
            encoder.SetEndMarkerMode(false);
            encoder.WriteCoderProperties(out);
            final long fileSize = in.available();
            for (int i = 0; i < 8; i++) {
                out.write((int) (fileSize >>> (8 * i)) & 0xFF);
            }
            encoder.Code(in, out, -1, -1, null);

            final byte[] compressed = out.toByteArray();

            byte[] b64 = Base64.getEncoder().encode(compressed);
            return new String(b64, "UTF-8");
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 把使用Lzma压缩的字符串进行解压
     *
     * @param compressedStr 使用Lzma压缩的字符串
     * @return 解压后的
     * @throws IOException
     */
    public static String decompress(final String compressedStr) throws IOException {
        if (StrUtil.isBlank(compressedStr)) {
            return compressedStr;
        }

        final Decoder decoder = new Decoder();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(compressedStr));

        try {
            final int propertiesSize = 5;
            final byte[] properties = new byte[propertiesSize];
            if (in.read(properties, 0, propertiesSize) != propertiesSize) {
                throw new IOException("input lzma content is too short");
            }
            if (!decoder.SetDecoderProperties(properties)) {
                throw new IOException("Incorrect stream properties");
            }
            long outSize = 0;
            for (int i = 0; i < 8; i++) {
                final int v = in.read();
                if (v < 0) {
                    throw new IOException("Can't read stream size");
                }
                outSize |= ((long) v) << (8 * i);
            }
            if (!decoder.Code(in, out, outSize)) {
                throw new IOException("Error in data stream");
            }

            return out.toString("UTF-8");
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    public static void main(String[] args) {
        try {
            Instant start = Instant.now();

            String mqtt = "{\"data\":[{\"a\":\"7\",\"b\":\"BTT\",\"c\":\"BTT\",\"d\":\"2021-09-19 16:52:11\",\"e\":24189998656.507698,\"f\":0.00397529,\"g\":0.00394705,\"h\":0.00388516,\"i\":0.0039994,\"j\":95207437.49607843,\"k\":-0.00000929,\"l\":-0.0023,\"m\":0.00395634},{\"a\":\"12\",\"b\":\"DOT\",\"c\":\"DOT\",\"d\":\"2021-09-19 16:52:08\",\"e\":2589405.733686473,\"f\":34.8505,\"g\":34.3911,\"h\":33.6232,\"i\":35.8984,\"j\":90016685.0933855,\"k\":-0.6135,\"l\":-0.0175,\"m\":35.0046},{\"a\":\"13\",\"b\":\"FIL\",\"c\":\"FIL\",\"d\":\"2021-09-19 16:52:08\",\"e\":1566732.6982626887,\"f\":85.3746,\"g\":84.321,\"h\":82.662,\"i\":86.1279,\"j\":132074206.49969383,\"k\":-0.7769,\"l\":-0.0091,\"m\":85.0979},{\"a\":\"16\",\"b\":\"EOS\",\"c\":\"EOS\",\"d\":\"2021-09-19 16:52:06\",\"e\":14497645.718650952,\"f\":5.3259,\"g\":5.2763,\"h\":5.1875,\"i\":5.5054,\"j\":77716020.3050352,\"k\":-0.0598,\"l\":-0.0112,\"m\":5.3361},{\"a\":\"3\",\"b\":\"DOGE\",\"c\":\"DOGE\",\"d\":\"2021-09-19 16:52:03\",\"e\":181706492.10942852,\"f\":0.244038,\"g\":0.241166,\"h\":0.238405,\"i\":0.247448,\"j\":43922537.90477139,\"k\":-0.001548,\"l\":-0.0063,\"m\":0.242714},{\"a\":\"11\",\"b\":\"JST\",\"c\":\"JST\",\"d\":\"2021-09-19 16:52:11\",\"e\":395688706.03242177,\"f\":0.090832,\"g\":0.089186,\"h\":0.087733,\"i\":0.091726,\"j\":35637052.276198246,\"k\":-0.001887,\"l\":-0.0207,\"m\":0.091073},{\"a\":\"17\",\"b\":\"SUN\",\"c\":\"SUN\",\"d\":\"2021-09-19 16:52:10\",\"e\":1466366962.1050332,\"f\":0.039758,\"g\":0.037196,\"h\":0.036559,\"i\":0.039924,\"j\":55715296.706659034,\"k\":-0.001631,\"l\":-0.0420,\"m\":0.038827},{\"a\":\"1\",\"b\":\"BTC\",\"c\":\"BTC\",\"d\":\"2021-09-19 16:52:11\",\"e\":8198.929543955472,\"f\":48606.33,\"g\":48280.32,\"h\":47580.0,\"i\":48825.08,\"j\":395177642.64025587,\"k\":-133.32,\"l\":-0.0027,\"m\":48413.64},{\"a\":\"10\",\"b\":\"SOL\",\"c\":\"SOL\",\"d\":\"2021-09-19 16:52:11\",\"e\":892246.3936352448,\"f\":158.9178,\"g\":162.9033,\"h\":157.2585,\"i\":171.4527,\"j\":145964601.0766538,\"k\":-0.7527,\"l\":-0.0045,\"m\":163.656},{\"a\":\"5\",\"b\":\"XRP\",\"c\":\"XRP\",\"d\":\"2021-09-19 16:52:06\",\"e\":38943667.8595357,\"f\":1.08586,\"g\":1.08294,\"h\":1.06447,\"i\":1.09366,\"j\":41979993.387358434,\"k\":-0.00031,\"l\":-0.0002,\"m\":1.08325},{\"a\":\"14\",\"b\":\"ICP\",\"c\":\"ICP\",\"d\":\"2021-09-19 16:52:00\",\"e\":540744.4367184431,\"f\":58.04,\"g\":58.59,\"h\":56.91,\"i\":61.19,\"j\":31991202.981408227,\"k\":-0.76,\"l\":-0.0128,\"m\":59.35},{\"a\":\"2\",\"b\":\"ETH\",\"c\":\"ETH\",\"d\":\"2021-09-19 16:52:11\",\"e\":130074.71953964107,\"f\":3513.23,\"g\":3446.83,\"h\":3373.69,\"i\":3541.59,\"j\":448518287.54635036,\"k\":-27.80,\"l\":-0.0080,\"m\":3474.63},{\"a\":\"15\",\"b\":\"LINK\",\"c\":\"LINK\",\"d\":\"2021-09-19 16:52:09\",\"e\":1566107.6358580538,\"f\":29.1441,\"g\":28.7396,\"h\":27.6895,\"i\":29.4549,\"j\":44665655.35101266,\"k\":0.0433,\"l\":0.0015,\"m\":28.6963},{\"a\":\"9\",\"b\":\"TRX\",\"c\":\"TRX\",\"d\":\"2021-09-19 16:52:11\",\"e\":502716847.6662558,\"f\":0.107751,\"g\":0.106497,\"h\":0.103722,\"i\":0.107939,\"j\":53217078.45229742,\"k\":-0.000209,\"l\":-0.0019,\"m\":0.106706},{\"a\":\"8\",\"b\":\"NFT\",\"c\":\"NFT\",\"d\":\"2021-09-19 16:52:09\",\"e\":59162494490118.44,\"f\":0.0000040299,\"g\":0.0000039741,\"h\":0.0000038,\"i\":0.000004085,\"j\":233966396.23778594,\"k\":-0.0000000370,\"l\":-0.0092,\"m\":0.0000040111},{\"a\":\"6\",\"b\":\"ADA\",\"c\":\"ADA\",\"d\":\"2021-09-19 16:52:07\",\"e\":14569470.844799755,\"f\":2.395292,\"g\":2.387958,\"h\":2.33675,\"i\":2.43853,\"j\":34707209.141234756,\"k\":-0.004259,\"l\":-0.0017,\"m\":2.392217},{\"a\":\"4\",\"b\":\"LUNA\",\"c\":\"LUNA\",\"d\":\"2021-09-19 16:52:00\",\"e\":2399968.862840682,\"f\":35.4808,\"g\":35.7848,\"h\":34.0627,\"i\":35.9582,\"j\":84451620.8279473,\"k\":0.1953,\"l\":0.0054,\"m\":35.5895}],\"messageType\":\"MARKET_DETAIL\"}";
            // String mqtt = "{\"a\":\"7\",\"b\":\"BTT\",\"c\":\"BTT\",\"d\":\"2021-09-19 16:52:11\",\"e\":24189998656.507698,\"f\":0.00397529,\"g\":0.00394705,\"h\":0.00388516,\"i\":0.0039994,\"j\":95207437.49607843,\"k\":-0.00000929,\"l\":-0.0023,\"m\":0.00395634}";
            System.out.println("压缩前大小" + mqtt.getBytes().length);
            System.out.println("lzma压缩后大小" + compress(mqtt).getBytes().length);
            System.out.println("gzip后大小" + ZipUtil.gzip(mqtt, "UTF-8").length);
            System.out.println("lzma压缩再gzip后大小" + ZipUtil.gzip(compress(mqtt), "UTF-8").length);

            long mills = Duration.between(start, Instant.now()).toMillis();
            System.out.println("压缩耗时" + mills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}