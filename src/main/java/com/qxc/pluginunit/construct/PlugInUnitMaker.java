package com.qxc.pluginunit.construct;

import com.qxc.configuration.PlugInUnitSuffixConfiguration;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 20:32
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.filesystemtools
 */
@Component
public final class PlugInUnitMaker {
    private final PlugInUnitSuffixConfiguration configuration;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public PlugInUnitMaker(PlugInUnitSuffixConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 通过小组件的父目录获取其他的子目录完成组件的配置
     */
    @Contract(pure = true)
    public PlugInUnit makePlugInUnit(String parent) {
        return new PlugInUnit().setParentPathName(parent).setServerPathName(parent + File.separator + configuration.getServer())
                .setStaticPathName(parent + File.separator + configuration.getStatics())
                .setDocPathName(parent + File.separator + configuration.getDoc())
                .setConfigPathName(parent + File.separator + configuration.getConfig())
                .setSrcPathName(parent + File.separator + configuration.getSrc());
    }
}
