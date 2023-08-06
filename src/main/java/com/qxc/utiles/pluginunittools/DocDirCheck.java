package com.qxc.utiles.pluginunittools;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 14:18
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 */
@CheckType(check=CheckTypeEnum.DOCDIR)
public interface DocDirCheck {
    /**
     * 检查readme文件
     */
    Boolean checkReadMe(String path);
}
