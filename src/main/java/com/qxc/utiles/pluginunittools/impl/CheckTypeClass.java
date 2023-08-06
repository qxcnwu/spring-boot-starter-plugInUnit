package com.qxc.utiles.pluginunittools.impl;

import com.qxc.utiles.filesystemtools.FileUtiles;
import com.qxc.utiles.pluginunittools.CheckOrder;
import com.qxc.utiles.pluginunittools.CheckType;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 0:35
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 */
@CheckType(check = CheckTypeEnum.AllDIR,uncheck = CheckTypeEnum.CONFIG)
public final class CheckTypeClass {

    /**
     * 查看文件夹是否存在
     */
    @CheckOrder(0)
    public static @NotNull Boolean checkExists(String dir) {
        return FileUtiles.dirExists(dir);
    }

    /**
     * 检查文件夹是否为空
     */
    @CheckOrder(1)
    @Contract(pure = true)
    public static @NotNull Boolean checkDirNotEmpty(String path) {
        return FileUtiles.dirIsNotEmpty(path);
    }
}
