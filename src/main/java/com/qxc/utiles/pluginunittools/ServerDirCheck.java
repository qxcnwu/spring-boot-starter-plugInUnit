package com.qxc.utiles.pluginunittools;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 14:20
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 */
@CheckType(check=CheckTypeEnum.SERVERDIR)
public interface ServerDirCheck {
    /**
     * 检查Server类型
     */
    @CheckOrder(2)
    Boolean checkServerType(String path);
}
