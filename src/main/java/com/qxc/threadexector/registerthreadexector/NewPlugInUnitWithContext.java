package com.qxc.threadexector.registerthreadexector;

import com.qxc.pluginunit.construct.PlugInUnit;
import com.qxc.pluginunit.construct.PlugInUnitMaker;
import com.qxc.pluginunit.PlugInUnitDocument;
import com.qxc.pluginunit.PlugInUnitServers;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Future;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 13:28
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.registerthreadexector
 */
@Data
@Accessors(chain = true)
public class NewPlugInUnitWithContext {
    private String rootPath;
    private PlugInUnit plugInUnit;
    private String plugInUnitName;
    private PlugInUnitServers plugInUnitServers;
    private PlugInUnitDocument plugInUnitDocument;
    private String versionName;
    private Future<Boolean> future;

    private boolean latest;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public NewPlugInUnitWithContext(String rootPath, String plugInUnitName, String versionName, @NotNull PlugInUnitMaker plugInUnitMaker) {
        this.rootPath = rootPath;
        this.plugInUnitName = plugInUnitName;
        this.versionName = versionName;
        plugInUnit = plugInUnitMaker.makePlugInUnit(rootPath);
        plugInUnit.setDocMDName(plugInUnit.getDocPathName() + File.separator + "readme.md");
    }

    /**
     * 关闭当前插件的工作
     */
    public void close() {
        // 关闭当前的所有服务
        plugInUnitServers.closeAllServer();
    }

    /**
     * 回退后启动需要更新当前的文档以及开启当前的服务
     */
    public void rStart() {
        plugInUnitDocument.updateMarkdown();
        plugInUnitServers.openAllServer();
    }

    @SuppressWarnings("MissingJavadoc")
    public NewPlugInUnitWithContextInner translate() {
        return new NewPlugInUnitWithContextInner(this);
    }

    @SuppressWarnings("MissingJavadoc")
    @Data
    public static class NewPlugInUnitWithContextInner {
        private final String rootPath;
        private final PlugInUnit plugInUnit;
        private final String plugInUnitName;
        private final ArrayList<String> plugInUnitServers = new ArrayList<>();
        private final String versionName;
        private final Future<Boolean> future;
        private final boolean latest;

        @SuppressWarnings("MissingJavadoc")
        @Contract(pure = true)
        public NewPlugInUnitWithContextInner(@NotNull NewPlugInUnitWithContext context) {
            rootPath = context.rootPath;
            plugInUnit = context.plugInUnit;
            plugInUnitName = context.plugInUnitName;
            versionName = context.versionName;
            future = context.future;
            latest = context.latest;
            Arrays.stream(context.plugInUnitServers.getAllServers()).forEach(a -> plugInUnitServers.add(a.getServerName()));
        }
    }
}
