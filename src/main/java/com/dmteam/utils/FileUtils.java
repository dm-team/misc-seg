package com.dmteam.utils;

import com.dmteam.system.SystemConfig;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created by xh on 2014/12/16.
 */
public class FileUtils {

    public static String NIOReadFile(File file) throws IOException {
        return NIOReadFile(file, SystemConfig.SYSTEM_CHARSET);
    }

    public static String NIOReadFile(File file, Charset charset) throws IOException {
        FileInputStream fos = null;
        try {

            fos = new FileInputStream(file);
            FileChannel channel = fos.getChannel();

            int length = (int)channel.size();

            byte[] bytes = new byte[length];

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 5);

            int len, offset=0;
            while ((len = channel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                byteBuffer.get(bytes, offset, len);
                offset += len;
            }

            return new String(bytes, charset);

        } finally {
            IOUtils.closeQuietly(fos);
        }
    }


    public static void main(String[] args) throws IOException {

        long st;
        String s;

//        st = System.currentTimeMillis();
//
//
//        s = new String(Files.readAllBytes(Paths.get("D:\\___folder\\QQ\\cul.qq.com_a_20130604_019704.htm.txt")), "utf-8");
//
//
//        System.out.println(System.currentTimeMillis() - st);
//
//
//        System.gc();

        st = System.currentTimeMillis();

        s = NIOReadFile(new File("D:\\___folder\\QQ\\cul.qq.com_a_20130604_019704.htm.txt"));

        System.out.println(System.currentTimeMillis() - st);


    }
}
