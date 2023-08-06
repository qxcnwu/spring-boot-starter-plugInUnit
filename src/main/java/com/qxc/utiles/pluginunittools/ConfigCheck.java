package com.qxc.utiles.pluginunittools;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 14:21
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 */
@CheckType(check = CheckTypeEnum.CONFIG)
public interface ConfigCheck {
    /**
     * 检查文件是否存在
     */
    @CheckOrder(2)
    Boolean configContainsCheck(String path);
}
