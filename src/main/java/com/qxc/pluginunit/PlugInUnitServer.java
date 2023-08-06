package com.qxc.pluginunit;

import com.qxc.pluginunit.construct.PlugInUnitServerImpl;
import org.jetbrains.annotations.Contract;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 21:47
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit
 */
public interface PlugInUnitServer {
    /**
     * 开启服务
     *
     * @return
     */
    boolean open();

    /**
     * 关闭服务
     *
     * @return
     */
    boolean close();

    /**
     * 重启服务
     *
     * @return
     */
    boolean restart();


    /**
     * 解析服务的配置文件
     *
     * @return
     */
    boolean decodeConfig();

    /**
     * 获取服务的优先级
     *
     * @return
     */
    int getRunPriority();

    /**
     * 获取服务的日志文件
     * @param readErr
     * @return
     */
    String getOutputStream(boolean readErr);

    /**
     * 获取当前服务的运行状态
     *
     * @return
     */
    RunState getCurrentState();

    /**
     * 获取当前服务的名称
     *
     * @return
     */
    String getServerName();


    /**
     * 判断是否相同
     *
     * @param obj
     * @return
     */
    @Contract(value = "null -> false", pure = true)
    boolean equals(Object obj);

    /**
     * 获取配置文件
     * @return
     */
    PlugInUnitServerImpl.PlugInUnitServerConfig getConfig();
}
