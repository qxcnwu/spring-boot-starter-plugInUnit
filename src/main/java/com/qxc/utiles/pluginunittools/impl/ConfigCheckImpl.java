package com.qxc.utiles.pluginunittools.impl;

import com.qxc.utiles.filesystemtools.FileUtiles;
import com.qxc.utiles.pluginunittools.CheckOrder;
import com.qxc.utiles.pluginunittools.CheckType;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import com.qxc.utiles.pluginunittools.ConfigCheck;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 14:38
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools.impl
 */
@CheckType(check = CheckTypeEnum.CONFIG)
public class ConfigCheckImpl implements ConfigCheck {
    /**
     * 检查文件是否存在
     *
     */
    @CheckOrder(0)
    @Override
    public Boolean configContainsCheck(String path) {
        return FileUtiles.fileExists(path);
    }
}
