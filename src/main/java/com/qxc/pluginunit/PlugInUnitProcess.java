package com.qxc.pluginunit;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 10:22
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit
 */
public interface PlugInUnitProcess {
    /**
     * 执行命令
     */
    boolean run();

    /**
     * 终止命令
     */
    boolean stop();

    /**
     * 重启服务的方法
     */
    default boolean restart(){
        if (stop()) {
            return false;
        }
        return run();
    }

    /**
     * 查询当前服务状态
     */
    RunState getState();

    /**
     * 获取错误以及常规输出流
     */
    boolean getOutStream();

    /**
     * 返回当前日志的路径
     */
    String getOutputPath();

    /**
     * 错误日志的保存路径
     */
    String getOutputErrPath();
}
