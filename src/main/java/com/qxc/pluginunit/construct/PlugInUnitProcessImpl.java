package com.qxc.pluginunit.construct;

import com.qxc.pluginunit.PlugInUnitProcess;
import com.qxc.pluginunit.RunState;
import com.qxc.threadexector.construct.RunableWithName;
import com.qxc.threadexector.ThreadPoolManage;
import com.qxc.utiles.filesystemtools.FileUtiles;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 9:48
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit.construct
 */
@Data
@Accessors(chain = true)
@Slf4j
public class PlugInUnitProcessImpl implements PlugInUnitProcess {
    String serverName;
    String command;
    Map<String, String> envMap;
    String execDir;
    ProcessBuilder processBuilder;
    Process process;
    String outputPath;
    String outputErrPath;
    String serverLogBase;
    ThreadPoolManage pool;

    private final static String OUTPUTPREX = "-getOutput";
    private final static String ERROUTPUTPREX = "-getErrOutput";

    @Contract(pure = true)
    public PlugInUnitProcessImpl(String serverName, String command, String execDir, HashMap<String, String> envMap, String outputPath, String outputErrPath, String serverLogBase, ThreadPoolManage pool) {
        this.serverName = serverName;
        this.command = command;
        this.execDir = execDir;
        this.serverLogBase = serverLogBase;
        this.pool = pool;
        this.outputPath = Paths.get(serverLogBase, outputPath).toString();
        this.outputErrPath = Paths.get(serverLogBase, outputErrPath).toString();
        this.processBuilder = new ProcessBuilder();
        this.envMap = processBuilder.environment();
        if (envMap != null) {
            this.envMap.putAll(envMap);
        }
    }

    @Contract(pure = true)
    public PlugInUnitProcessImpl(String serverName, String command, String execDir, String outputPath, String outputErrPath, String serverLogBase, ThreadPoolManage pool) {
        this(serverName, command, execDir, null, outputPath, outputErrPath, serverLogBase, pool);
    }

    @Contract(pure = true)
    public PlugInUnitProcessImpl(String serverName, String command, String execDir, String serverLogBase, ThreadPoolManage pool) {
        this(serverName, command, execDir, null, "output.log", "error.log", serverLogBase, pool);
    }

    @Contract(pure = true)
    public PlugInUnitProcessImpl(String serverName, String command, String execDir, ThreadPoolManage pool) {
        this(serverName, command, execDir, null, "output.log", "error.log", execDir, pool);
    }

    /**
     * 执行命令
     *
     * @return
     */
    public boolean run() {
        // 查询是否在运行状态
        // 查询当前输出流是否在工作状态
        if (getState() == RunState.RUNNING || pool.contains(serverName + OUTPUTPREX) || pool.contains(serverName + ERROUTPUTPREX)) {
            return false;
        }
        File dir = new File(execDir);
        if (!dir.exists() || !dir.isDirectory()) {
            log.error("no such directory " + execDir);
            return false;
        }
        processBuilder.command(command.split(" ")).directory(dir);
        log.info(command);
        try {
            process = processBuilder.start();
            getOutStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error(command + " run error!!");
            return false;
        }
    }

    /**
     * 终止
     *
     * @return
     */
    public boolean stop() {
        if (process == null) {
            return false;
        }
        log.info("server start stop:" + serverName);
        process.destroy();
        log.info("server try destroy:" + serverName);
        pool.removeThread(serverName + OUTPUTPREX);
        pool.removeThread(serverName + ERROUTPUTPREX);
        log.info("server Stream destroy:" + serverName);
        boolean isLive = process.isAlive();
        process = null;
        return !isLive;
    }


    /**
     * 获取当前的进程状态
     *
     * @return
     */
    public RunState getState() {
        if (process == null) {
            return RunState.WAITING;
        }
        if (process.isAlive()) {
            return RunState.RUNNING;
        }
        try {
            return process.waitFor() != 0 ? RunState.ERROR : RunState.DONE;
        } catch (InterruptedException e) {
            log.error("InterruptedException thread");
            return RunState.ERROR;
        }
    }

    /**
     * 获取错误以及常规输出流
     *
     * @return
     */
    @Override
    public boolean getOutStream() {
        pool.execute(new RunableWithName(getOutputStream(), serverName + OUTPUTPREX));
        final Runnable outputErrorStream = getOutputErrorStream();
        if (outputErrorStream != null) {
            pool.execute(new RunableWithName(outputErrorStream, serverName + ERROUTPUTPREX));
        }
        return true;
    }


    /**
     * 标准输出线程
     *
     * @return
     */
    @Contract(pure = true)
    private @NotNull Runnable getOutputStream() {
        return () -> FileUtiles.writeByteToFile(outputPath, process.getInputStream());
    }

    /**
     * 标准错误线程
     * 错误输出流可能为空因此需要进行判断
     *
     * @return
     */
    @Contract(pure = true)
    private @Nullable Runnable getOutputErrorStream() {
        final InputStream errorStream = process.getErrorStream();
        try {
            if (errorStream == null) {
                return null;
            }
            Runnable runnable = () -> FileUtiles.writeByteToFile(outputErrPath, errorStream);
            return runnable;
        } catch (Exception ignored) {
            log.error(errorStream.toString());
            log.error(ignored.getMessage());
            return null;
        }
    }
}
