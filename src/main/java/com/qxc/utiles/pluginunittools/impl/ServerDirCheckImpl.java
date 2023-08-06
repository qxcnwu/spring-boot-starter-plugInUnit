package com.qxc.utiles.pluginunittools.impl;

import com.qxc.utiles.pluginunittools.CheckOrder;
import com.qxc.utiles.pluginunittools.CheckType;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import com.qxc.utiles.pluginunittools.ServerDirCheck;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 14:39
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools.impl
 */
@CheckType(check = CheckTypeEnum.SERVERDIR)
public class ServerDirCheckImpl implements ServerDirCheck {
    /**
     * 检查Server类型
     *
     */
    @Override
    @CheckOrder(3)
    public Boolean checkServerType(String path) {
        return true;
    }
}
