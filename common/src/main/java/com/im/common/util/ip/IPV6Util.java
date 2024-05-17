package com.im.common.util.ip;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sirius
 * @date 2019/3/11
 */
public class IPV6Util {

    private static File file;

    private static boolean enableFileWatch = false;

    private static Long lastModifyTime = 0L;

    private static MappedByteBuffer buffer;

    private static StringBuffer bufferStr;

    // 偏移地址长度（2~8）
    private static byte offsetLen;

    // IP地址长度(4或8或12或16, 现在只支持4(ipv4)和8(ipv6))
    /*private static byte ipLen;*/

    // 数据文件的总记录数
    private static long indexCount;

    // 索引区第一条记录的偏移
    private static long firstIndex;

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // load("D:\\Daniel\\ipv6wry.db");
        // String[] strings = find("2001:da8:20c:311:4a76:f6a0:d00a:cb1c");
        // System.out.println(strings[0]);
        // System.out.println(strings[1]);
    }

    public static void load(File ipFile) {
        file = ipFile;
        load();
        if (enableFileWatch) {
            watch();
        }
    }

    /**
     * 加载IP地址文件
     */
    public static void load() {
        lastModifyTime = file.lastModified();
        FileInputStream fileInputStream = null;
        lock.lock();
        try {
            fileInputStream = new FileInputStream(file);

            bufferStr = new StringBuffer(new Scanner(fileInputStream).useDelimiter("\\Z").next());

            buffer = new RandomAccessFile(file, "r").getChannel().
                    map(FileChannel.MapMode.READ_ONLY, 0, file.length());

            if (buffer.order().toString().equals(ByteOrder.BIG_ENDIAN.toString())) {
                buffer.order(ByteOrder.LITTLE_ENDIAN);
            }

            offsetLen = buffer.get(6);

            /*ipLen = buffer.get(7);*/

            indexCount = buffer.getLong(8);

            firstIndex = buffer.getLong(16);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {

            } finally {
                lock.unlock();
            }
        }
    }

    public static void watch() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long time = file.lastModified();
                if (time > lastModifyTime) {
                    lastModifyTime = time;
                    load();
                }
            }
        }, 1000L, 5000L, TimeUnit.MILLISECONDS);
    }

    /**
     * 在文件中查找出IP对应的地区
     *
     * @param ip
     * @return
     */
    public static String[] find(String ip) {
        ip = ip.replace(" ", "");
        byte[] bytes = ipv6ToBytes(ip);
        BigInteger bigIp = new BigInteger(bytes);
        // 需将转换后的IP值右移64位
        bigIp = bigIp.shiftRight(64);

        String[] address = null;
        lock.lock();
        try {
            // 查找IP的索引偏移
            long ipIndexOff = findIndex(bigIp.longValue(), 0, indexCount);
            // IP所在的索引记录
            long ipIndex = firstIndex + ipIndexOff * (8 + offsetLen);
            long ipIndex2 = buffer.getInt((int) ipIndex + 8);
            address = getAddr((int) ipIndex2);
        } finally {
            lock.unlock();
        }
        return address;
    }

    /**
     * 获得IP对应的地区
     *
     * @param ipIndex
     * @return
     */
    public static String[] getAddr(int ipIndex) {
        byte b = buffer.get(ipIndex);
        if (b == 1) {
            return getAddr(buffer.get(ipIndex + 1));
        } else {
            String cArea = getAreaAddr(ipIndex);
            if (b == 2) {
                ipIndex += 1 + offsetLen;
            } else {
                ipIndex = bufferStr.indexOf("\0", ipIndex) + 1;
            }
            String aArea = getAreaAddr(ipIndex);
            String[] address = new String[]{cArea, aArea};
            return address;
        }
    }

    /**
     * 获得宽带地址
     *
     * @param offset
     * @return
     */
    public static String getAreaAddr(int offset) {
        byte b = buffer.get(offset);
        if (b == 1 || b == 2) {
            // offlen为3，所以此处是获取三个字节
            int i = buffer.getInt(offset + 1) & 0x00FFFFFF;
            return getAreaAddr(i);
        } else {
            String str = getString(offset);
            int i = str.indexOf("\0");
            return str.substring(0, i);
        }
    }


    /**
     * 获得IP的偏移地址，以便到记录区去查找
     *
     * @param offset
     * @return
     */
    public static String getString(int offset) {
        // 新建一个长度为200的数组，用来存入地区所在的数据
        byte[] indexBytes = new byte[200];
        buffer.position(offset);
        buffer.get(indexBytes, 0, 200);

        try {
            return new String(indexBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 在索引区进行二分查找
     *
     * @param ip    ip地址
     * @param start 进行二分查找的初始起点
     * @param count 总记录数
     * @return
     */
    public static long findIndex(long ip, long start, long count) {
        long i = count - start;
        if (i <= 1) {
            return start;
        }
        long midden = (start + count) / 2;
        long o = firstIndex + midden * (8 + offsetLen);
        long new_ip = buffer.getLong(Integer.valueOf(String.valueOf(o)));
        if (ip < new_ip) {
            return findIndex(ip, start, midden);
        } else {
            return findIndex(ip, midden, count);
        }
    }

    /**
     * IP地址转换成byte数组
     *
     * @param ipv6
     * @return
     */
    public static byte[] ipv6ToBytes(String ipv6) {
        byte[] ret = new byte[17];
        ret[0] = 0;
        int ib = 16;
        // ipv4混合模式标记
        boolean comFlag = false;
        // 去掉开头的冒号
        if (ipv6.startsWith(":")) {
            ipv6 = ipv6.substring(1);
        }
        String groups[] = ipv6.split(":");
        // 反向扫描
        for (int ig = groups.length - 1; ig > -1; ig--) {
            if (groups[ig].contains(".")) {
                // 出现ipv4混合模式
                byte[] temp = ipv4ToBytes(groups[ig]);
                ret[ib--] = temp[4];
                ret[ib--] = temp[3];
                ret[ib--] = temp[2];
                ret[ib--] = temp[1];
                comFlag = true;
            } else if ("".equals(groups[ig])) {
                // 出现零长度压缩,计算缺少的组数
                int zlg = 9 - (groups.length + (comFlag ? 1 : 0));
                while (zlg-- > 0) {// 将这些组置0
                    ret[ib--] = 0;
                    ret[ib--] = 0;
                }
            } else {
                int temp = Integer.parseInt(groups[ig], 16);
                ret[ib--] = (byte) temp;
                ret[ib--] = (byte) (temp >> 8);
            }
        }
        return ret;
    }

    /**
     * IPV4地址转换（出现混合模式时使用）
     *
     * @param ipv4
     * @return
     */
    public static byte[] ipv4ToBytes(String ipv4) {
        byte[] ret = new byte[5];
        ret[0] = 0;
        // 先找到IP地址字符串中.的位置
        int position1 = ipv4.indexOf(".");
        int position2 = ipv4.indexOf(".", position1 + 1);
        int position3 = ipv4.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ret[1] = (byte) Integer.parseInt(ipv4.substring(0, position1));
        ret[2] = (byte) Integer.parseInt(ipv4.substring(position1 + 1, position2));
        ret[3] = (byte) Integer.parseInt(ipv4.substring(position2 + 1, position3));
        ret[4] = (byte) Integer.parseInt(ipv4.substring(position3 + 1));
        return ret;
    }
}
