package com.qxc.utiles.filesystemtools;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @Author qxc
 * @Date 2023 2023/7/16 10:20
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.filesystemtools
 */
@Slf4j
public final class PathParseUtiles {
    @SuppressWarnings("MissingJavadoc")
    public static @NotNull String getVersionName(@NotNull String path) {
        return path.substring((new File(path).getParent()).length()).replaceAll(File.separator + File.separator, "");
    }

    @SuppressWarnings("MissingJavadoc")
    public static @NotNull String getPlugInUnitName(String path) {
        return getVersionName(new File(path).getParent());
    }
}
