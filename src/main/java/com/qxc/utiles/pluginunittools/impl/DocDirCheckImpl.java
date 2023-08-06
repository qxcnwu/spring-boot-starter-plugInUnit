package com.qxc.utiles.pluginunittools.impl;

import com.qxc.utiles.filesystemtools.FileUtiles;
import com.qxc.utiles.pluginunittools.CheckOrder;
import com.qxc.utiles.pluginunittools.CheckType;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import com.qxc.utiles.pluginunittools.DocDirCheck;

import java.io.File;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 14:38
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools.impl
 */
@CheckType(check = CheckTypeEnum.DOCDIR)
public class DocDirCheckImpl implements DocDirCheck {
    /**
     * 检查readme文件
     *
     */
    @Override
    @CheckOrder(2)
    public Boolean checkReadMe(String path) {
        return FileUtiles.fileExists(path + File.separator + "readme.md");
    }
}
