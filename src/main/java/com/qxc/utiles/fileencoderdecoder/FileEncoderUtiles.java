package com.qxc.utiles.fileencoderdecoder;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Author qxc
 * @Date 2023 2023/7/23 19:04
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.fileencoderdecoder
 */
@Slf4j
public class FileEncoderUtiles {
    private static final MessageDigest MD5;
    private static final MessageDigest SHA256;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
            SHA256 = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件的sha256编码
     *
     * @param path
     * @return
     */
    public static byte @NotNull [] getSHA256(@NotNull String s) {
        InputStream fis;
        final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        SHA256.update(bytes);
        return SHA256.digest();
    }

    /**
     * 获取byte数组
     *
     * @param file
     * @return
     */
    public static byte @NotNull [] getSHA256(File file) {
        InputStream fis;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            for (int numRead = 0; (numRead = fis.read(buffer)) > 0; ) {
                SHA256.update(buffer, 0, numRead);
            }
            fis.close();
            return SHA256.digest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为hash字符
     *
     * @param b
     * @return
     */
    static @NotNull String toHexString(byte @NotNull [] b) {
        StringBuilder sb = new StringBuilder();
        for (byte aB : b) {
            sb.append(Integer.toHexString(aB & 0xFF));
        }
        return sb.toString();
    }

    /**
     * 获取字符串的md5值
     *
     * @param s
     * @return
     */
    public static byte @NotNull [] getMd5(@NotNull String s) {
        return MD5.digest(s.getBytes(StandardCharsets.UTF_8));
    }
}
