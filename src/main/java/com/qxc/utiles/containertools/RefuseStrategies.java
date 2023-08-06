package com.qxc.utiles.containertools;

import com.qxc.pluginunit.PlugInUnitDomain;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 16:15
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 * @param <T>
 */
public interface RefuseStrategies<T extends PlugInUnitDomain> {
    /**
     * 重新加载对象
     */
    T load();

    /**
     * 重载指定版本
     */
    T load(String version);


    /**
     * 持久化
     *
     */
    void persist(PlugInUnitDomain domain);
}
