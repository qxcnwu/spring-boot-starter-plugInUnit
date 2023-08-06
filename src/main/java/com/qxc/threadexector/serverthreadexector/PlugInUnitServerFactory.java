package com.qxc.threadexector.serverthreadexector;

import com.qxc.pluginunit.construct.PlugInUnit;
import com.qxc.pluginunit.construct.PlugInUnitServersImpl;
import com.qxc.pluginunit.PlugInUnitServers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 15:47
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.serverthreadexector
 */
@Component
public class PlugInUnitServerFactory {
    private final ServerThreadExector exector;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PlugInUnitServerFactory(ServerThreadExector exector) {
        this.exector = exector;
    }

    @SuppressWarnings("MissingJavadoc")
    public PlugInUnitServers initServers(@NotNull PlugInUnit plugInUnit, boolean start) {
        return new PlugInUnitServersImpl(plugInUnit.getServerPathName(), exector.getPool(), start);
    }
}
