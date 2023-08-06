package com.qxc.pluginunit;

import java.util.HashMap;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 15:39
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit
 */
public interface PlugInUnitServers {

    /**
     * 导入当前目录中所有的服务
     *
     */
    int loadServers();

    /**
     * 开启服务
     *
     */
    boolean openAllServer();

    /**
     * 查询当前服务的状态
     *
     */
    RunState getServerState(String serveName);

    /**
     * 获取当前所有服务的状态
     *
     */
    HashMap<String, RunState> getAllServerState();

    /**
     * 打开目标服务
     *
     */
    boolean openServer(String serverName);

    /**
     * 关闭目标服务
     *
     */
    boolean closeServer(String serverName);

    /**
     * 重启某个服务
     */
    boolean restart(String serverName);

    /**
     * 重启全部服务
     */
    void restartAll();

    /**
     * 关闭所有服务
     */
    void closeAllServer();

    /**
     * 刷新文件夹查找新服务
     */
    void refresh();

    /**
     * 查询是否存在对应的服务
     *
     */
    PlugInUnitServer containServer(String serverName);

    /**
     * 获取所有服务
     *
     */
    PlugInUnitServer[] getAllServers();
}
