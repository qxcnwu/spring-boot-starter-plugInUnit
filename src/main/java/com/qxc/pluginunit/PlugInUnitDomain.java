package com.qxc.pluginunit;

import com.qxc.pluginunit.construct.PlugInUnitChecked;

import java.io.Serializable;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 16:00
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit
 */
public interface PlugInUnitDomain extends Serializable,PlugInUnitServer {
    /**
     * 返回版本号
     *
     */
    String getVersion();

    /**
     * 设置版本号
     *
     */
    void setVersion(String version);

    /**
     * 设置文件路径
     */
    void setPath(PlugInUnitChecked plc);

    /**
     * 返回文件路径
     */
    PlugInUnitChecked getPath();
}
