package com.qxc.pluginunit.construct;

import com.qxc.pluginunit.PlugInUnitProcess;
import com.qxc.pluginunit.PlugInUnitServer;
import com.qxc.pluginunit.RunState;
import com.qxc.threadexector.ThreadPoolManage;
import com.qxc.utiles.filesystemtools.FileUtiles;
import com.qxc.utiles.filesystemtools.JsonUtiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 21:56
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit.construct
 */
@Slf4j
public class PlugInUnitServerImpl implements PlugInUnitServer {
    private final String configString;
    @Getter
    private String serverName;
    @Getter
    private final String parentDir;
    @Getter
    private PlugInUnitServerConfig config;
    private PlugInUnitProcess plugInUnitProcess;
    private final ThreadPoolManage poolManage;

    @Contract(pure = true)
    public PlugInUnitServerImpl(String configString, String parentDir, ThreadPoolManage poolManage) {
        this.configString = configString;
        this.poolManage = poolManage;
        this.parentDir = parentDir;
        if (!decodeConfig()) {
            throw new RuntimeException("decode json fair!!");
        }
        initPlugInUnitProcessImpl();
    }

    public void initPlugInUnitProcessImpl() {
        serverName = config.serverName;
        String command = config.startCmd;
        command = command.replace("$dir/", parentDir + File.separator);
        String execDir = config.execDir;
        HashMap<String, String> envMap = config.envMap;
        String outputPath = config.outputPath;
        String outputErrPath = config.outputErrPath;
        String serverLogBase = config.serverLogBase;
        plugInUnitProcess = new PlugInUnitProcessImpl(serverName, command, execDir, envMap, outputPath, outputErrPath, serverLogBase, poolManage);
    }

    /**
     * 开启服务
     *
     * @return
     */
    @Override
    public boolean open() {
        return plugInUnitProcess.run();
    }

    /**
     * 关闭服务
     *
     * @return
     */
    @Override
    public boolean close() {
        return plugInUnitProcess.stop();
    }

    /**
     * 重启服务
     *
     * @return
     */
    @Override
    public boolean restart() {
        return plugInUnitProcess.restart();
    }


    /**
     * 解析服务的配置文件
     *
     * @return
     */
    @Override
    public boolean decodeConfig() {
        try {
            config = (PlugInUnitServerConfig) JsonUtiles.parseJsonFile(configString, PlugInUnitServerConfig.class);
            return true;
        } catch (IOException e) {
            log.info("parse json file false!!" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取服务的优先级
     *
     * @return
     */
    @Override
    public int getRunPriority() {
        return config.priorityNumber;
    }

    /**
     * 读取文件
     * @param readErr
     * @return
     */
    public String getOutputStream(boolean readErr) {
        // 如果当前还在运行那么则返回空值
        if (plugInUnitProcess.getState().equals(RunState.RUNNING)) {
            return "";
        }
        // 如果获取的error
        final String path = readErr ? plugInUnitProcess.getOutputErrPath() : plugInUnitProcess.getOutputPath();
        return FileUtiles.readFile(path);
    }

    /**
     * 获取当前服务的运行状态
     *
     * @return
     */
    @Override
    public RunState getCurrentState() {
        return plugInUnitProcess.getState();
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlugInUnitServer)) {
            return false;
        }
        PlugInUnitServer objs = (PlugInUnitServer) obj;
        return config.equals(objs.getConfig());
    }

    @Accessors(chain = true)
    @Data
    @AllArgsConstructor
    public static class PlugInUnitServerConfig implements Serializable {
        private String serverName;
        private String startCmd;
        private String execDir;

        private HashMap<String, String> envMap;

        private String outputPath = "OUTPUT";

        private String outputErrPath = "ERROR";
        private String serverLogBase;
        private int priorityNumber = 2;

        @Contract(pure = true)
        public PlugInUnitServerConfig(String serverName, String startCmd, String execDir, String serverLogBase, int priorityNumber) {
            this.serverName = serverName;
            this.startCmd = startCmd;
            this.execDir = execDir;
            this.serverLogBase = serverLogBase;
            this.priorityNumber = priorityNumber;
        }

        @Contract(value = "null -> false", pure = true)
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PlugInUnitServerConfig)) {
                return false;
            }
            PlugInUnitServerConfig that = (PlugInUnitServerConfig) o;
            return getStartCmd().equals(that.getStartCmd()) && Objects.equals(getExecDir(), that.getExecDir()) && getOutputPath().equals(that.getOutputPath()) && getOutputErrPath().equals(that.getOutputErrPath()) && getServerLogBase().equals(that.getServerLogBase());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getStartCmd(), getExecDir(), getOutputPath(), getOutputErrPath(), getServerLogBase());
        }
    }
}
