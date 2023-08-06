package com.qxc.pluginunit;

import com.qxc.pluginunit.construct.PlugInUnitChecked;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 15:34
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit
 */
public interface PlugInUnitContainer {
    @SuppressWarnings("MissingJavadoc")
    boolean addPlugInUnit(String name, PlugInUnitChecked p);

    @SuppressWarnings("MissingJavadoc")
    boolean removePlugInUnit(String name);

    @SuppressWarnings("MissingJavadoc")
    boolean rollbackPlugInUnit(String name);


}
