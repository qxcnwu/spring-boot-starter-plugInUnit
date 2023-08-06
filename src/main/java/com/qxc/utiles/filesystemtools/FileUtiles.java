package com.qxc.utiles.filesystemtools;

import com.qxc.pluginunit.construct.PlugInUnit;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 10:35
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 */
@Slf4j
public final class FileUtiles {
    /**
     * 将输入流保存到文件
     *
     */
    @Contract(pure = true)
    public static void writeByteToFile(String filePath, InputStream bytes) {
        log.info(filePath + " start write to file");
        FileWriter file = null;
        BufferedReader reader = null;
        try {
            file = new FileWriter(filePath);
            reader = new BufferedReader(new InputStreamReader(bytes));
            String line;
            while ((line = reader.readLine()) != null) {
                file.write(line + "\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 将PROCESS输入流保存到文件
     *
     */
    @Contract(pure = true)
    public static boolean writeByteToFile(String filePath, @NotNull Process p) {
        FileWriter file = null;
        BufferedReader reader = null;
        try {
            file = new FileWriter(filePath);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                file.write(line + "\n");
            }
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public static boolean isExsist(String path) {
        final File file = new File(path);
        return !file.exists();
    }

    @SuppressWarnings("MissingJavadoc")
    public static boolean dirExists(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    /**
     * 判断文件是否存在
     *
     */
    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    /**
     * 文件夹是否存在
     *
     */
    public static boolean dirExists(@NotNull PlugInUnit pu) {
        return dirExists(pu.getDocPathName()) && fileExists(pu.getConfigPathName()) && dirExists(pu.getParentPathName()) && dirExists(pu.getStaticPathName()) && dirExists(pu.getServerPathName());
    }

    /**
     * 检查文件夹文件个数是否
     *
     */
    public static boolean dirIsNotEmpty(String path) {
        File file = new File(path);
        return Objects.requireNonNull(file.listFiles()).length > 0;
    }

    /**
     * 读取JSON文件
     *
     * @throws IOException
     */
    public static @NotNull String readJson(String path) throws IOException {
        if (!fileExists(path)) {
            throw new FileNotFoundException("No such file:" + path);
        }
        final File file = new File(path);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存到文件
     *
     * @param jsonObj
     */
    public static void saveJson(String path, String jsonObj) {
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonObj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public static @Nullable List<String> getChildrenDir(String parentPath, FileCheckable<String> checker) {
        if (!dirExists(parentPath)) {
            log.error("Empty dir!!");
            return null;
        }
        ArrayList<String> arr = new ArrayList<>();
        File file = new File(parentPath);
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory() && checker.check(f.getAbsolutePath())) {
                arr.add(f.getAbsolutePath());
            }
        }
        return arr;
    }

    /**
     * 读取RST
     *
     */
    public static @NotNull ArrayList<String> readRst(String path) throws IOException {
        if (!fileExists(path)) {
            throw new FileNotFoundException("No such file:" + path);
        }
        ArrayList<String> arr = new ArrayList<>();
        final File file = new File(path);
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                arr.add(line);
            }
            return arr;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入RST
     *
     */
    public static boolean writeRst(@NotNull ArrayList<String> arr, String path) {
        try (FileWriter file = new FileWriter(path)) {
            for (String s : arr) {
                file.write(s + "\n");
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 拷贝文件
     *
     */
    @Contract(pure = true)
    public static void copyFile(String srcPath, String dstPath) {
        // 如果目标文件不存在返回错误
        if (!FileUtiles.fileExists(srcPath)) {
            return;
        }
        // 如果源文件存在需要先进行删除
        try {
            Files.deleteIfExists(new File(dstPath).toPath());
            Files.copy(Path.of(srcPath), Path.of(dstPath));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 读取文件
     *
     */
    public static @NotNull String readFile(String path) {
        if (!fileExists(path)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        final File file = new File(path);
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }

    }
}
