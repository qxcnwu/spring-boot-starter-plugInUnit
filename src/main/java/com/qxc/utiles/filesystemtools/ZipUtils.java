package com.qxc.utiles.filesystemtools;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Description 解压缩文件工具类
 * @Author Mr.nobody
 * @Date 2021/3/8
 * @Version 1.0.0
 */
@Slf4j
public class ZipUtils {

    /**
     * 解压
     *
     * @param zipFilePath  带解压文件
     * @param desDirectory 解压到的目录
     */
    public static void unzip(String zipFilePath, String desDirectory) throws Exception {
        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            mkdir(new File(desDirectory));
        }
        // 读入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath), Charset.forName("GBK"));
        // 遍历每一个文件
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // 文件夹
                // 直接创建
                mkdir(new File(unzipFilePath));
            } else { // 文件
                File file = new File(unzipFilePath);
                // 创建父目录
                mkdir(file.getParentFile());
                // 写出文件流
                BufferedOutputStream bufferedOutputStream =
                        new BufferedOutputStream(new FileOutputStream(unzipFilePath));
                byte[] bytes = new byte[1024];
                int readLen;
                while ((readLen = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, readLen);
                }
                bufferedOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    /**
     * 创建文件夹
     *
     */
    private static void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());
        file.mkdir();
    }


    @SuppressWarnings("MissingJavadoc")
    public static @NotNull String zipFileCheckAndUnzip(String path, String plugInUnitPath) throws Exception {
        // 文件没有被占用那么就继续执行
        while (!isNotUsed(path)) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.info("file has been interrupt!!");
            }
        }
        File file = new File(path);
        final String fileName = file.getName();
        final String[] strings = fileName.replace(".zip", "").split("-");
        String plugInUnitName = strings[0];
        String versionName = strings[1];
        String execPath = plugInUnitPath + File.separator + plugInUnitName + File.separator + versionName;
        unzip(path, execPath);
        return execPath;
    }

    /**
     * 监测文件是否被占用
     *
     */
    private static boolean isNotUsed(String path) {
        File file = new File(path);
        File file2 = new File("bat");
        boolean success = file.renameTo(file2);
        if (!success) {
            return file2.renameTo(file);
        } else {
            return false;
        }
    }
}