package com.qxc.utiles.filesystemtools;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import java.io.IOException;

import static com.qxc.utiles.filesystemtools.FileUtiles.readJson;
import static com.qxc.utiles.filesystemtools.FileUtiles.saveJson;

/**
 * @Author qxc
 * @Date 2023 2023/7/8 17:11
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 */
@Slf4j
public final class JsonUtiles {
    /**
     * 解析json文件
     *
     * @param clazz
     * @throws IOException
     */
    @Contract(pure = true)
    public static Object parseJsonFile(String path, Class<?> clazz) throws IOException {
        String jsonString = readJson(path);
        return JSON.parseObject(jsonString, clazz);
    }

    /**
     * 导出OBJECT到json对象
     *
     * @param path
     */
    @Contract(pure = true)
    public static void saveToJson(String path, Object obj) {
        String objStr = JSON.toJSONString(obj);
        saveJson(path, objStr);
    }
}
