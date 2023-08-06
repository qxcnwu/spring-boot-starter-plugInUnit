package com.qxc.threadexector.registerthreadexector;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 13:26
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.registerthreadexector
 */
public interface RegisterThreadExector {
    /**
     * 提交一个工件
     */
    void submit(NewPlugInUnitWithContext newPlugInUnitWithContext);

    /**
     * 回退功能版本
     */
    void rollback(NewPlugInUnitWithContext newPlugInUnitWithContext);

    /**
     * 关闭当前的注册中心
     */
    boolean shutdown();
}
