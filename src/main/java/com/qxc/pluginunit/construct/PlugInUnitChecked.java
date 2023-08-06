package com.qxc.pluginunit.construct;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 14:55
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit.construct
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PlugInUnitChecked extends PlugInUnit {
    private transient File parentPath;
    private transient File docPath;
    private transient File serverPath;
    private transient File staticPath;
    private transient File configPath;

    @SuppressWarnings("MissingJavadoc")
    public PlugInUnitChecked(@NotNull PlugInUnit p) {
        super();
        setParentPathName(p.getParentPathName()).setConfigPathName(p.getConfigPathName()).setDocPathName(p.getDocPathName()).setServerPathName(p.getStaticPathName()).setStaticPathName(p.getStaticPathName());
        setParentPath().setConfigPath().setDocPath().setServerPath().setStaticPath();
    }

    @Contract(" -> this")
    private PlugInUnitChecked setParentPath() {
        this.parentPath = new File(getParentPathName());
        return this;
    }

    @Contract(" -> this")
    private PlugInUnitChecked setDocPath() {
        this.docPath = new File(getDocPathName());
        return this;
    }

    @Contract(" -> this")
    private PlugInUnitChecked setServerPath() {
        this.serverPath = new File(getServerPathName());
        return this;
    }

    private void setStaticPath() {
        this.staticPath = new File(getStaticPathName());
    }

    @Contract(" -> this")
    private PlugInUnitChecked setConfigPath() {
        this.configPath = new File(getConfigPathName());
        return this;
    }
}
